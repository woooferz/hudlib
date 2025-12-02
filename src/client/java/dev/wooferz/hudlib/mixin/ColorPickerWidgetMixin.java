package dev.wooferz.hudlib.mixin;

import dev.isxander.yacl3.api.utils.MutableDimension;
import dev.isxander.yacl3.gui.controllers.ColorPickerWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ColorPickerWidget.class)
public class ColorPickerWidgetMixin {
    @Shadow protected MutableDimension<Integer> alphaGradientDim;

    @Inject(at=@At(value = "HEAD"), method="isHoveringAlphaSlider", cancellable = true, remap = false)
    public void isHoveringAlphaSlider(double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir) {
        if (this.alphaGradientDim == null) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
