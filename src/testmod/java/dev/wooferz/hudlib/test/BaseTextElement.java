package dev.wooferz.hudlib.test;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.wooferz.hudlib.HudAnchor;
import dev.wooferz.hudlib.hud.HUDConfig;
import dev.wooferz.hudlib.hud.HUDElement;
import dev.wooferz.hudlib.utils.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

public class BaseTextElement extends HUDElement {

    public BaseTextConfig config = new BaseTextConfig();

    public BaseTextElement(String displayName, int defaultX, int defaultY, int defaultWidth, int defaultHeight, int padding, String identifier, HudAnchor.HorizontalAnchor defaultHorizontalAnchor, HudAnchor.VerticalAnchor defaultVerticalAnchor) {
        super(displayName, defaultX, defaultY, defaultWidth, defaultHeight, padding, HudLibTest.MOD_ID, identifier, defaultHorizontalAnchor, defaultVerticalAnchor);
    }

    @Override
    public void render(int i, int i1, int i2, int i3, DrawContext drawContext, float v) {

    }

    public void renderText(String text, int x, int y, int w, int h, DrawContext drawContext, float v, boolean centered) {
        renderText(text, x, y, w, h, drawContext, v, centered, 0);
    }

    public void renderText(String text, int x, int y, int w, int h, DrawContext context, float v, boolean centered, int paddingX) {

        int padding = 4;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;


        if (!config.chroma) {
            if (centered) {
                context.drawCenteredTextWithShadow(textRenderer, text, (w/2)+x, y + padding + 1, config.color.getRGB());
            } else {
                context.drawTextWithShadow(textRenderer, text, x + paddingX, y + padding + 1, config.color.getRGB());
            }
        } else {
            if (centered) {
                TextUtils.drawText(context, textRenderer, text, (w/2)+x,  y + padding + 1, 0x000000, true, true, true);
            } else {
                TextUtils.drawText(context, textRenderer, text, x + paddingX, y + padding + 1, 0x000000, true, false, true);
            }
        }

    }


    public void renderBox(DrawContext context, int x, int y, int w, int h) {
        int argb = (config.bgColor.getAlpha() << 24) | (config.bgColor.getRed() << 16) | (config.bgColor.getGreen() << 8) | config.bgColor.getBlue();

        context.fill(x, y, x + w, y + h, argb);
    }

    @Override
    public Class<?> getConfigType() {
        return BaseTextConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof BaseTextConfig) {
                this.config = (BaseTextConfig) config;
            }
        }
    }



    @Override
    public OptionGroup generateConfig() {
        return OptionGroup.createBuilder()
                .name(Text.of(displayName))
                .option(Option.<Color>createBuilder()
                        .name(Text.of("Text Color"))
                        .binding(Color.WHITE,
                                () -> config.color,
                                newColor -> config.color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .option(Option.<Color>createBuilder()
                        .name(Text.of("Background Color"))
                        .binding(new Color(0x99000000, true),
                                () -> config.bgColor,
                                newColor -> config.bgColor = newColor)
                        .controller(opt -> ColorControllerBuilder.create(opt)
                                .allowAlpha(true))
                        .build()
                )
                .option(
                        Option.<Boolean>createBuilder()
                                .name(Text.of("Chroma"))
                                .binding(
                                        false,
                                        () -> config.chroma,
                                        newValue -> config.chroma = newValue
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                )
                .build();
    }
}