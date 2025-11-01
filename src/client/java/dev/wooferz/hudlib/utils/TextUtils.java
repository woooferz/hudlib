package dev.wooferz.hudlib.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class TextUtils {

    public static void drawText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, boolean drawShadow, boolean centered, boolean chroma) {

        int cx = x;
        if (centered) {
            cx = x - textRenderer.getWidth(text) / 2;
        }
        if (!chroma) {
            context.drawText(textRenderer, text, cx, y, color, drawShadow);
        } else {
            drawChromaText(context, textRenderer, text, cx, y, drawShadow);
        }

    }
    private static void drawChromaText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, boolean drawShadow) {

        int rx = x;

        for (char c : text.toCharArray()) {
            long dif = (rx * 10L) - (y * 10L);
            long l = System.currentTimeMillis() - dif;
            float ff = 2000.0F;
            int i = Color.HSBtoRGB(((float) (l % (int) ff))/2000F, 0.8F, 0.8F);

            String tmp = String.valueOf(c);
            context.drawText(textRenderer, tmp, rx, y, i, drawShadow);
            rx += textRenderer.getWidth(tmp);
        }

    }

}
