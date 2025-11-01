package dev.wooferz.hudlib.test;

import dev.wooferz.hudlib.HudAnchor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

public class CoordinateElement extends BaseTextElement{

    public CoordinateElement() {
        super("Coordinates HUD", 5, 5 + 17 + 10 + 17 + 10 + 17 + 10 + 17 + 10, 55, 17 * 3, 1, "coords-hud", HudAnchor.HorizontalAnchor.LEFT, HudAnchor.VerticalAnchor.TOP);
    }

    @Override
    public void render(int i, int i1, int i2, int i3, DrawContext drawContext, float v) {

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }

        renderText("X: " + (int) player.getX(), i, i1, i2, i3, drawContext, v, false, 5);
        renderText("Y: " + (int) player.getY(), i, i1 + 17, i2, i3, drawContext, v, false, 5);
        renderText("Z: " + (int) player.getZ(), i , i1 + 17 + 17, i2, i3, drawContext, v, false, 5);



        renderBox(drawContext, i, i1, getWidth(), 17 * 3);

    }

    @Override
    public Integer getWidth() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return null;
        }

        int xLength = String.valueOf((int) player.getX()).length();
        int yLength = String.valueOf((int) player.getY()).length();
        int zLength = String.valueOf((int) player.getZ()).length();


        int normalWidth = 55;
        int extraWidth = 0;

        int maxCoordLength = Math.max(Math.max(xLength, yLength), zLength);
        if (maxCoordLength > 6) {
            extraWidth = (maxCoordLength - 6) * mc.textRenderer.getWidth("0") + 5;
        }

        return normalWidth + extraWidth;

    }
}
