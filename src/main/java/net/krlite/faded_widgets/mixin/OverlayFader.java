package net.krlite.faded_widgets.mixin;

import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class OverlayFader {
	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/util/math/MatrixStack;)V"
			)
	)
	private void tiltToast(ToastManager toastManager, MatrixStack matrixStack) {
		matrixStack.push();
		matrixStack.translate(160 * FadedWidgets.fading(), 0, 0);

		toastManager.draw(matrixStack);

		matrixStack.pop();
	}

	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	private void tiltHand(MatrixStack matrixStack, Camera camera, float tickDelta, CallbackInfo ci) {
		matrixStack.translate(0.2 * FadedWidgets.fading(), -0.7 * FadedWidgets.fading(), 0);
	}
}
