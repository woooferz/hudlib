package dev.wooferz.hudlib;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.Rect2i;

public class HudAnchor {

    public enum HorizontalAnchor {
        LEFT,
        CENTER,
        RIGHT
    }
    public enum VerticalAnchor {
        TOP,
        MIDDLE,
        BOTTOM
    }

    public HorizontalAnchor horizontalAnchor;
    public VerticalAnchor verticalAnchor;

    public HudAnchor(HorizontalAnchor horizontalAnchor, VerticalAnchor verticalAnchor) {
        this.horizontalAnchor = horizontalAnchor;
        this.verticalAnchor = verticalAnchor;
    }

 /*   public void applyTransform(DrawContext context) {
        applyTransform(context.getMatrices());
    }

    public void applyTransform(MatrixStack stack) {
        stack.push();
        stack.translate(rightAnchor ? (double) MinecraftClient.getInstance().getWindow().getWidth() : 0.0, bottomAnchor ? (double) MinecraftClient.getInstance().getWindow().getHeight() : 0.0, 0.0);
    }
    public void undoTransform(DrawContext context) {
        undoTransform(context.getMatrices());
    }
    public void undoTransform(MatrixStack stack) {
        stack.pop();
    }*/

    public Rect2i convert(Rect2i rect) { // convert anchored coords to x, y normal coords
        Window window = MinecraftClient.getInstance().getWindow();
        Rect2i newRect = new Rect2i(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        if (horizontalAnchor == HorizontalAnchor.RIGHT) {
            newRect.setX(window.getScaledWidth() - rect.getX() - rect.getWidth());
        } else if (horizontalAnchor == HorizontalAnchor.CENTER) {
            newRect.setX((width/2)+rect.getX()-(rect.getWidth()/2));
        }
        if (verticalAnchor == VerticalAnchor.BOTTOM) {
            newRect.setY(window.getScaledHeight()-rect.getY()-rect.getHeight());
        } else if (verticalAnchor == VerticalAnchor.MIDDLE) {
            newRect.setY((height/2)+rect.getY()-(rect.getHeight()/2));
        }
        return newRect;

    }
    public Rect2i convertBack(Rect2i rect) { // convert normal x, y coords to anchored coords
        Window window = MinecraftClient.getInstance().getWindow();
        Rect2i newRect = new Rect2i(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        if (horizontalAnchor == HorizontalAnchor.RIGHT) {
            newRect.setX(window.getScaledWidth() - rect.getX() - rect.getWidth());
        } else if (horizontalAnchor == HorizontalAnchor.CENTER) {
            newRect.setX((rect.getX() + rect.getWidth() / 2) - width/2); //
        }
        if (verticalAnchor == VerticalAnchor.BOTTOM) {
            newRect.setY(window.getScaledHeight()-rect.getY()-rect.getHeight());
        } else if (verticalAnchor == VerticalAnchor.MIDDLE) {
            newRect.setY((rect.getY() + rect.getHeight() / 2) - height/2);
        }
        return newRect;
    }
   /* public Rect2i convertBack(Rect2i rect) {
        Window window = MinecraftClient.getInstance().getWindow();
        Rect2i newRect = new Rect2i(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        if (rightAnchor) {
            newRect.setX(window.getScaledWidth() - rect.getX());
        }
        if (bottomAnchor) {
            newRect.setY(window.getScaledHeight()-rect.getY());
        }
        return newRect;
    }*/
}
