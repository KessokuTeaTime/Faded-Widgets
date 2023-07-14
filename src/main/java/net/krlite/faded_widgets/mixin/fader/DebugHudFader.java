package net.krlite.faded_widgets.mixin.fader;

import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugHud.class)
public abstract class DebugHudFader {
	@Shadow protected abstract void renderLeftText(MatrixStack matrices);

	@Shadow protected abstract void renderRightText(MatrixStack matrices);

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;renderLeftText(Lnet/minecraft/client/util/math/MatrixStack;)V"))
	private void tiltLeftText(DebugHud debugHud, MatrixStack matrixStack) {
		double width = MinecraftClient.getInstance().getWindow().getScaledWidth();

		matrixStack.push();
		matrixStack.translate(-width * FadedWidgets.fading(), 0, 0);

		renderLeftText(matrixStack);

		matrixStack.pop();
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;renderRightText(Lnet/minecraft/client/util/math/MatrixStack;)V"))
	private void tiltRightText(DebugHud debugHud, MatrixStack matrixStack) {
		double width = MinecraftClient.getInstance().getWindow().getScaledWidth();

		matrixStack.push();
		matrixStack.translate(width * FadedWidgets.fading(), 0, 0);

		renderRightText(matrixStack);

		matrixStack.pop();
	}
}
