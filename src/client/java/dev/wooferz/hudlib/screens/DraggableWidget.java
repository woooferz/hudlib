package dev.wooferz.hudlib.screens;

import dev.wooferz.hudlib.HudAnchor;
import dev.wooferz.hudlib.HudManager;
import dev.wooferz.hudlib.hud.HUDElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;

public class DraggableWidget extends ClickableWidget {

    double realX;
    double realY;
    double realWidth;
    double realHeight;
    HUDElement element;
    Boolean enabled;
    boolean pressed = false;
    boolean resizing = false;
    EditScreen screen;
    //float scale = 0.5f;

    public DraggableWidget(int x, int y, int width, int height, Boolean enabled, HUDElement element, EditScreen screen) {
        super(x - element.padding, y - element.padding, width + (element.padding * 2), height + (element.padding * 2), Text.empty());
        this.realX = x;
        this.realY = y;
        this.realWidth = width;
        this.realHeight = height;
        this.element = element;
        this.enabled = enabled;
        this.screen = screen;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int borderColor = 0xFFFFFFFF;
        int disabledOverlay = 0x55000000;

        if (!this.enabled) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), disabledOverlay);
        }

            context.drawBorder(getX(), getY(), getWidth(), getHeight(), borderColor);

        if (element.canResize()) {
            context.fill(getX() + getWidth() - 5, getY() + getHeight() - 5, getX() + getWidth(), getY() + getHeight(), borderColor);
        }
    }

    private boolean isResizing(double mouseX, double mouseY) {
        return mouseX >= getX() + getWidth() - 5 && mouseY >= getY() + getHeight() - 5 && mouseX <= getX() + getWidth() && mouseY <= getY() + getHeight();
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        Window window = MinecraftClient.getInstance().getWindow();
        int wwidth = window.getScaledWidth();
        int wheight = window.getScaledHeight();

        if (resizing) {
            this.realWidth += deltaX;
            this.realHeight += deltaY;

            if (realWidth < 5) {
                realWidth = 5;
            }
            if (realHeight < 5 ) {
                realHeight = 5;
            }

            setWidth((int) realWidth);
            height = ((int) realHeight);
        } else {
            this.realX += deltaX;
            this.realY += deltaY;

            if (this.realX + this.realWidth > wwidth) {
                this.realX = wwidth - this.realWidth;
            }
            if (this.realY + this.realHeight > wheight) {
                this.realY = wheight - this.realHeight;
            }
            if (this.realX < 0) {
                this.realX = 0;
            }
            if (this.realY < 0) {
                this.realY = 0;
            }

            int snappedX = (int) realX;
            int snappedY = (int) realY;

            // LOGGER.info(String.valueOf((snappedX + snappedX + width) / 2));
            // LOGGER.info(String.valueOf(wwidth));
            if (!screen.isCtrlHeld) {
                if (Math.abs(((snappedX + snappedX + width) / 2) - (wwidth / 2)) < 10) {
                    snappedX = (wwidth / 2) - (width / 2);
                }
                if (Math.abs(((snappedY + snappedY + height) / 2) - (wheight / 2)) < 10) {
                    snappedY = (wheight / 2) - (height / 2);
                }
            }
            setX(snappedX);
            setY(snappedY);
        }
        pressed = false;

        Rect2i position = getRect();
        Rect2i fixedPosition = HudManager.hudAnchors.get(element.identifier).convertBack(position);

        HudManager.hudPositions.put(element.identifier, fixedPosition);
    }

    public Rect2i getRect() {
        return new Rect2i(this.getX() + element.padding, this.getY() + element.padding, this.getWidth() - (element.padding * 2), this.getHeight() - (element.padding * 2));
    }


    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        pressed = true;
        resizing = isResizing(mouseX, mouseY) && element.canResize();

        super.onClick(mouseX, mouseY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (pressed && !(resizing)) {
            enabled = !enabled;
            if (enabled) {
                element.onEnable();
            } else {
                element.onDisable();
            }
            HudManager.hudShown.put(element.identifier, enabled);
        }
        if (!pressed && !resizing) {
            HudAnchor anchor = HudManager.hudAnchors.get(element.identifier);
            Window window = MinecraftClient.getInstance().getWindow();
            int centerX = (getWidth() + getX() + getX()) / 2;
            int centerY = (getHeight() + getY() + getY()) / 2;
            int width = window.getScaledWidth();
            int height = window.getScaledHeight();


            if (centerX < (width / 3)) {
                anchor.horizontalAnchor = HudAnchor.HorizontalAnchor.LEFT;
            } else if (centerX < (2 * width / 3)) {
                anchor.horizontalAnchor = HudAnchor.HorizontalAnchor.CENTER;
            } else {
                anchor.horizontalAnchor = HudAnchor.HorizontalAnchor.RIGHT;
            }
            if (centerY < (height / 3)) {
                anchor.verticalAnchor = HudAnchor.VerticalAnchor.TOP;
            } else if (centerY < (2 * height / 3)) {
                anchor.verticalAnchor = HudAnchor.VerticalAnchor.MIDDLE;
            } else {
                anchor.verticalAnchor = HudAnchor.VerticalAnchor.BOTTOM;
            }
            HudManager.hudPositions.put(element.identifier, anchor.convertBack(getRect()));
        }
        super.onRelease(mouseX, mouseY);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
