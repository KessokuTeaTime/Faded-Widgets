package net.krlite.fadedwidgets.mixin.fader;

import net.krlite.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class CrosshairFader {
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacityCrosshair(DrawContext context, float tickDelta, CallbackInfo ci) {

		FadedWidgets.setShaderColor(context);
	}
}
