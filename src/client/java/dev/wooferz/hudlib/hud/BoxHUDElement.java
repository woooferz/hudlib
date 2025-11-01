package dev.wooferz.hudlib.hud;

import dev.wooferz.hudlib.HudLibClient;
import net.minecraft.client.gui.DrawContext;

public class BoxHUDElement extends HUDElement{

    public BoxHUDElement(int defaultX, int defaultY, int defaultWidth, int defaultHeight, int padding) {
        super("Box Display", defaultX, defaultY, defaultWidth, defaultHeight, padding, HudLibClient.MOD_ID, "box-hud");
    }

    @Override
    public void render(int x, int y, int width, int height, DrawContext context, float tickDelta) {
        context.fill(x, y, x + width, y + height, 0xFF3380ff);
    }

    @Override
    public boolean canResize() {
        return true;
    }
}
