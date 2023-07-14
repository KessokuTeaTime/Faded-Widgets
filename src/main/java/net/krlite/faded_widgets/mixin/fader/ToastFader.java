package net.krlite.faded_widgets.mixin.fader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class ToastFader {
	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/util/math/MatrixStack;)V"
			)
	)
	private void tiltToast(ToastManager toastManager, MatrixStack matrixStack) {
		MatrixStack modelViewStack = RenderSystem.getModelViewStack();

		modelViewStack.push();
		modelViewStack.translate(160 * FadedWidgets.fading(), 0, 0);
		RenderSystem.applyModelViewMatrix();

		toastManager.draw(matrixStack);

		modelViewStack.pop();
		RenderSystem.applyModelViewMatrix();
	}
}
