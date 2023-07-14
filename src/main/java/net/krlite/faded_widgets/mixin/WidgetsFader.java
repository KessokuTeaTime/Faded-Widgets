package net.krlite.faded_widgets.mixin;

import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class WidgetsFader {
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacity(DrawContext context, float tickDelta, CallbackInfo ci) {
		context.setShaderColor(1, 1, 1, (float) FadedWidgets.fading());
	}
}
