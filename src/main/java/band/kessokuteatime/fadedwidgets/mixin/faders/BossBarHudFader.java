package band.kessokuteatime.fadedwidgets.mixin.faders;

import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudFader {
	@Inject(method = "render", at = @At("HEAD"))
	private void setOpacity(DrawContext context, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha(context);
	}
}
