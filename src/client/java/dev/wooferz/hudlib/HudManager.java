package dev.wooferz.hudlib;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.wooferz.hudlib.config.*;
import dev.wooferz.hudlib.hud.HUDConfig;
import dev.wooferz.hudlib.hud.HUDElement;
import dev.wooferz.hudlib.screens.EditScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static dev.wooferz.hudlib.HudLibClient.openEditorKey;
import static dev.wooferz.hudlib.HudLibClient.LOGGER;

public class HudManager {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Color.class,new ColorTypeAdapter())
            .create();

    public static ArrayList<HUDElement> hudElements = new ArrayList<HUDElement>();
    public static HashMap<String, Rect2i> hudPositions = new HashMap<String, Rect2i>();
    public static HashMap<String, Boolean> hudShown = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> hudEnabled = new HashMap<>();
    public static HashMap<String, HudAnchor> hudAnchors = new HashMap<String, HudAnchor>();


    public static boolean isElementsConfigLoaded = false;

    public static void addConfigOptionGroups(ConfigCategory.Builder builder) {
        for (int i = 0; i < hudElements.size(); i++) {
            HUDElement element = hudElements.get(i);
            OptionGroup optionGroup = element.generateConfig();
            if (optionGroup != null) {
                builder.group(optionGroup);
            }
        }
    }

    public static void render(DrawContext context, float tickDelta) {

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

       /* context.drawVerticalLine(width/3, 0, height, 0xFFFFFFFF);
        context.drawVerticalLine(width*2/3, 0, height, 0xFFFFFFFF);
        context.drawHorizontalLine(0, width, height/3, 0xFFFFFFFF);
        context.drawHorizontalLine(0, width, height*2/3, 0xFFFFFFFF);*/

        for (int i = 0; i < hudElements.size(); i++) {
            HUDElement element = hudElements.get(i);
            if ((hudShown.get(element.identifier) || MinecraftClient.getInstance().currentScreen instanceof EditScreen) && hudEnabled.get(element.identifier)) {
                Rect2i positionUnanchored = hudPositions.get(element.identifier);
                Rect2i position = hudAnchors.get(element.identifier).convert(positionUnanchored);
                element.render(position.getX(), position.getY(), position.getWidth(), position.getHeight(), context, tickDelta);
            }
        }

    }
    public static void openEditor(MinecraftClient minecraftClient) {
        if (openEditorKey.wasPressed()) {
            minecraftClient.setScreen(new EditScreen(Text.of("")));
        }
    }
    public static void registerHudElement(HUDElement hudElement) {
        ConfigManager.getInstance().read();
        hudElements.add(hudElement);
        Config config = ConfigManager.getInstance().config;
        Rect2i position;
        boolean shown = true;
        HudAnchor anchor = null;
        boolean enabled = true;

        if (config.getElement(hudElement.identifier) != null) {
            position = config.getElement(hudElement.identifier).position;
            if (!hudElement.canResize()) {
                position.setWidth(hudElement.defaultWidth);
                position.setHeight(hudElement.defaultHeight);
            }
            if (config.getElement(hudElement.identifier).anchor != null) {
                anchor = config.getElement(hudElement.identifier).anchor;
            }
            enabled = config.getElement(hudElement.identifier).enabled;
            shown = config.getElement(hudElement.identifier).shown;
        } else {
            position = new Rect2i(hudElement.defaultX, hudElement.defaultY, hudElement.defaultWidth, hudElement.defaultHeight);
        }
        if (anchor == null) {
            anchor = new HudAnchor(hudElement.defaultHorizontalAnchor, hudElement.defaultVerticalAnchor);
        }
        hudPositions.put(hudElement.identifier, position);
        hudShown.put(hudElement.identifier, shown);
        hudAnchors.put(hudElement.identifier, anchor);
        hudEnabled.put(hudElement.identifier, enabled);

        if (!isElementsConfigLoaded) {
            ElementConfig.HANDLER.load();

            isElementsConfigLoaded = true;
        }

        String elementConfigString = ElementConfig.HANDLER.instance().elementConfigs.get(hudElement.identifier);
        HUDConfig elementConfig = (HUDConfig) gson.fromJson(elementConfigString, hudElement.getConfigType());
        hudElement.setConfig(elementConfig);

        Collections.sort(hudElements);

        LOGGER.info("Successfully registered {}.", hudElement.displayName);
    }

    public static void saveConfig() {
        ArrayList<HUDElement> elements = hudElements;
        for (int i = 0; i < elements.size(); i++) {
            HUDElement element = elements.get(i);
            HUDConfig config = element.getConfig();
            if (config != null) {
                ElementConfig elementConfig = ElementConfig.HANDLER.instance();
                String configString = gson.toJson(config);
                elementConfig.elementConfigs.put(element.identifier, configString);
            }
        }

        ElementConfig.HANDLER.save();
    }

    public static void addGenericConfigOptions(ConfigCategory.Builder genericHudOptions) {
        for (int i = 0; i < hudElements.size(); i++) {
            HUDElement element = hudElements.get(i);
            OptionGroup optionGroup = OptionGroup.createBuilder()
                    .name(Text.of(element.displayName))
                    .option(
                            Option.<Boolean>createBuilder()
                                    .name(Text.of("Enabled"))
                                    .description(OptionDescription.of(Text.of("Turning this off will remove it from the editor.")))
                                    .binding(
                                            true,
                                            () -> hudEnabled.get(element.identifier),
                                            value -> {
                                                hudEnabled.put(element.identifier, value);
                                                saveElementBaseConfig(element);
                                            }

                                    )
                                    .controller(TickBoxControllerBuilder::create)
                                    .build()
                    )
                    .option(
                            ButtonOption.createBuilder()
                                    .name(Text.of("Reset Position"))
                                    .description(OptionDescription.of(Text.of("This will reset its position on the screen to its default.")))
                                    .action(((yaclScreen, buttonOption) -> {
                                        Rect2i position = new Rect2i(element.defaultX, element.defaultY, element.defaultWidth, element.defaultHeight);
                                        HudAnchor anchor = new HudAnchor(element.defaultHorizontalAnchor, element.defaultVerticalAnchor);
                                        hudPositions.put(element.identifier, position);
                                        hudAnchors.put(element.identifier, anchor);
                                    }))
                                    .build()
                    )
                    .build();
            genericHudOptions.group(optionGroup);
        }
    }

    public static void saveElementBaseConfig(HUDElement element) {
        ConfigElementInformation information = new ConfigElementInformation();
        information.shown = HudManager.hudShown.get(element.identifier);
        information.position = HudManager.hudPositions.get(element.identifier);
        information.anchor = HudManager.hudAnchors.get(element.identifier);
        information.enabled = HudManager.hudEnabled.get(element.identifier);

        ConfigManager.getInstance().config.saveElement(element.identifier, information);
        ConfigManager.getInstance().saveConfig();
    }
}
