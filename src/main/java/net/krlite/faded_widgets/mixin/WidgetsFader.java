package net.krlite.faded_widgets.mixin;

import net.krlite.equator.math.algebra.Theory;
import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public class WidgetsFader {
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/SpectatorHud;renderSpectatorMenu(Lnet/minecraft/client/gui/DrawContext;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacitySpectatorMenu(DrawContext context, float tickDelta, CallbackInfo ci) {
		context.setShaderColor(1, 1, 1, (float) (1 - FadedWidgets.fading()));
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/DrawContext;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacityHotbar(DrawContext context, float tickDelta, CallbackInfo ci) {
		context.setShaderColor(1, 1, 1, (float) (1 - FadedWidgets.fading()));
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacityCrosshair(DrawContext context, float tickDelta, CallbackInfo ci) {
		float opacity = (float) (1 - FadedWidgets.fading());
		context.setShaderColor(1, 1, 1, opacity);
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V",
					shift = At.Shift.AFTER
			)
	)
	private void setOpacity(DrawContext context, float tickDelta, CallbackInfo ci) {
		context.setShaderColor(1, 1, 1, (float) (1 - FadedWidgets.fading()));
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void setOpacityPost(DrawContext context, float tickDelta, CallbackInfo ci) {
		context.setShaderColor(1, 1, 1, 1);
	}

	@ModifyArgs(
			method = "renderVignetteOverlay",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;setShaderColor(FFFF)V"
			),
			slice = @Slice(
					to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableDepthTest()V")
			)
	)
	private void setVignetteOpacity(Args args) {
		float r = args.get(0), g = args.get(1), b = args.get(2);
		args.set(0, (float) Theory.lerp(0, r, 1 - FadedWidgets.fading())); // red
		args.set(1, (float) Theory.lerp(0, g, 1 - FadedWidgets.fading())); // green
		args.set(2, (float) Theory.lerp(0, b, 1 - FadedWidgets.fading())); // blue
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
					shift = At.Shift.BEFORE
			)
	)
	private void tiltHotbarPre(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.getMatrices().push();
		context.getMatrices().translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
					shift = At.Shift.AFTER
			)
	)
	private void tiltHotbarPost(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.getMatrices().pop();
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
					shift = At.Shift.BEFORE
			)
	)
	private void tiltHotbarItemPre(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.getMatrices().push();
		double shift = 24 * FadedWidgets.fading();

		if (FadedWidgets.isVerticalityLoaded()) { // Support for Verticality
			context.getMatrices().translate(-shift, 0, 0);
		} else {
			context.getMatrices().translate(0, shift, 0);
		}
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
					shift = At.Shift.AFTER
			)
	)
	private void tiltHotbarItemPost(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}
