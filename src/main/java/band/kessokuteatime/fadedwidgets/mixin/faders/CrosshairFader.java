package band.kessokuteatime.fadedwidgets.mixin.faders;

import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class CrosshairFader {
	@Inject(method = "renderCrosshair", at = @At("HEAD"))
	private void setOpacityCrosshair(DrawContext context, RenderTickCounter renderTickCounter, CallbackInfo ci) {
		FadedWidgets.setShaderColor(context);
	}
}
