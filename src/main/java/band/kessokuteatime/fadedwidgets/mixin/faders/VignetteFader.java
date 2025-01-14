package band.kessokuteatime.fadedwidgets.mixin.faders;

import net.krlite.equator.math.algebra.Theory;
import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public class VignetteFader {
	@ModifyArgs(
			method = "renderVignetteOverlay",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;setShaderColor(FFFF)V"
			),
			slice = @Slice(
					to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableDepthTest()V", remap = false)
			)
	)
	private void setOpacityVignette(Args args) {
		float r = args.get(0), g = args.get(1), b = args.get(2);
		args.set(0, (float) Theory.lerp(0, r, 1 - FadedWidgets.fading())); // red
		args.set(1, (float) Theory.lerp(0, g, 1 - FadedWidgets.fading())); // green
		args.set(2, (float) Theory.lerp(0, b, 1 - FadedWidgets.fading())); // blue
	}
}
