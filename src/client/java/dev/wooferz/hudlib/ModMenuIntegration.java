package dev.wooferz.hudlib;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;


public class ModMenuIntegration implements ModMenuApi {

  //  YetAnotherConfigLib configMenu;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::finishScreen;
    }

    public static Screen finishScreen(Screen parent) {
        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
                .title(Text.of("Used for narration. Could be used to render a title in the future."));
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
                .name(Text.of("HUD Elements"));

        ConfigCategory.Builder genericHudOptions = ConfigCategory.createBuilder()
                        .name(Text.of("Generic HUD Options"));

        HudManager.addConfigOptionGroups(categoryBuilder);

        HudManager.addGenericConfigOptions(genericHudOptions);

        builder.category(categoryBuilder.build()).save(HudManager::saveConfig);
        builder.category(genericHudOptions.build());

        return builder.build().generateScreen(parent);
    }
}
