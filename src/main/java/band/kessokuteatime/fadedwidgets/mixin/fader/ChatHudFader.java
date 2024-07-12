package band.kessokuteatime.fadedwidgets.mixin.fader;

import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudFader {
	@Inject(method = "render", at = @At("HEAD"))
	private void setOpacity(DrawContext context, int i, int j, int k, boolean bl, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha(context);
	}
}
