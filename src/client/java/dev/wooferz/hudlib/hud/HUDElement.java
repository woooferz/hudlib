package dev.wooferz.hudlib.hud;

import dev.isxander.yacl3.api.OptionGroup;
import dev.wooferz.hudlib.HudAnchor;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("EmptyMethod")
public abstract class HUDElement implements Comparable<HUDElement> {
    public int defaultX;
    public int defaultY;
    public int defaultWidth;
    public int defaultHeight;
    public int padding;
    public String identifier;
    public String displayName;
    public boolean renderAnyway = false;
    public HudAnchor.HorizontalAnchor defaultHorizontalAnchor;
    public HudAnchor.VerticalAnchor defaultVerticalAnchor;

    public HUDElement(String displayName, int defaultX, int defaultY, int defaultWidth, int defaultHeight, int padding, String modid, String identifier, HudAnchor.HorizontalAnchor defaultHorizontalAnchor, HudAnchor.VerticalAnchor defaultVerticalAnchor) {
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.padding = padding;
        this.identifier = modid + ":" + identifier;
        this.defaultHorizontalAnchor = defaultHorizontalAnchor;
        this.defaultVerticalAnchor = defaultVerticalAnchor;
        this.displayName = displayName;
    }

    public HUDElement(String displayName, int defaultX, int defaultY, int defaultWidth, int defaultHeight, int padding, String modid, String identifier) {
        this(displayName, defaultX, defaultY, defaultWidth, defaultHeight, padding, modid, identifier, HudAnchor.HorizontalAnchor.LEFT, HudAnchor.VerticalAnchor.TOP);
    }

    public boolean canResize() {
        return false;
    }

    public abstract void render(int x, int y, int width, int height, DrawContext context, float tickDelta);

    public void setConfig(HUDConfig config) {
    }

    public HUDConfig getConfig() {
        return null;
    }

    public Class<?> getConfigType() {
        return HUDConfig.class;
    }

    public OptionGroup generateConfig() {
        return null;
    }

    public void onDisable() {
    }

    public void onEnable() {
    }

    @Override
    public int compareTo(@NotNull HUDElement o) {
        return this.identifier.compareTo(o.identifier);
    }

    public void editorOpened() {
    }

    public void editorClosed() {
    }

    public Integer getWidth() {
        return null;
    }

    public Integer getHeight() {
        return null;
    }
}
