package net.krlite.faded_widgets.mixin.fader;

import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.MetricsData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public class DebugHudFader {
	@Inject(method = "renderLeftText", at = @At("HEAD"))
	private void tiltLeftTextPre(MatrixStack matrixStack, CallbackInfo ci) {
		double shift = MinecraftClient.getInstance().getWindow().getScaledWidth();

		matrixStack.push();
		matrixStack.translate(-shift * FadedWidgets.fading(), 0, 0);
	}

	@Inject(method = "renderLeftText", at = @At("TAIL"))
	private void tiltLeftTextPost(MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.pop();
	}

	@Inject(method = "renderRightText", at = @At("HEAD"))
	private void tiltRightText(MatrixStack matrixStack, CallbackInfo ci) {
		double shift = MinecraftClient.getInstance().getWindow().getScaledWidth();

		matrixStack.push();
		matrixStack.translate(shift * FadedWidgets.fading(), 0, 0);
	}

	@Inject(method = "renderRightText", at = @At("TAIL"))
	private void tiltRightTextPost(MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.pop();
	}

	@Inject(method = "drawMetricsData", at = @At("HEAD"))
	private void tiltMetricsDataText(MatrixStack matrixStack, MetricsData metricsData, int x, int width, boolean showFps, CallbackInfo ci) {
		double shift = MinecraftClient.getInstance().getWindow().getScaledWidth();

		matrixStack.push();
		matrixStack.translate(shift * FadedWidgets.fading(), 0, 0);
	}

	@Inject(method = "drawMetricsData", at = @At("TAIL"))
	private void tiltMetricsDataPost(MatrixStack matrixStack, MetricsData metricsData, int x, int width, boolean showFps, CallbackInfo ci) {
		matrixStack.pop();
	}
}
