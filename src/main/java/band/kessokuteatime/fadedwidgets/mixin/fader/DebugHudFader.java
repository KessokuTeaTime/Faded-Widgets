package band.kessokuteatime.fadedwidgets.mixin.fader;

import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.MetricsData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public class DebugHudFader {
	@Inject(method = "drawLeftText", at = @At("HEAD"))
	private void tiltLeftTextPre(DrawContext context, CallbackInfo ci) {
		double shift = MinecraftClient.getInstance().getWindow().getScaledWidth();

		context.getMatrices().push();
		context.getMatrices().translate(-shift * FadedWidgets.fading(), 0, 0);
	}

	@Inject(method = "drawLeftText", at = @At("TAIL"))
	private void tiltLeftTextPost(DrawContext context, CallbackInfo ci) {
		context.getMatrices().pop();
	}

	@Inject(method = "drawRightText", at = @At("HEAD"))
	private void tiltRightText(DrawContext context, CallbackInfo ci) {
		double shift = MinecraftClient.getInstance().getWindow().getScaledWidth();

		context.getMatrices().push();
		context.getMatrices().translate(shift * FadedWidgets.fading(), 0, 0);
	}

	@Inject(method = "drawRightText", at = @At("TAIL"))
	private void tiltRightTextPost(DrawContext context, CallbackInfo ci) {
		context.getMatrices().pop();
	}

	@Inject(method = "drawMetricsData", at = @At("HEAD"))
	private void tiltMetricsDataText(DrawContext context, MetricsData metricsData, int x, int width, boolean showFps, CallbackInfo ci) {
		double shift = MinecraftClient.getInstance().getWindow().getScaledWidth();

		context.getMatrices().push();
		context.getMatrices().translate(shift * FadedWidgets.fading(), 0, 0);
	}

	@Inject(method = "drawMetricsData", at = @At("TAIL"))
	private void tiltMetricsDataPost(DrawContext context, MetricsData metricsData, int x, int width, boolean showFps, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}
