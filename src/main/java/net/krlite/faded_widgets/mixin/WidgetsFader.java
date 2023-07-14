package net.krlite.faded_widgets.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.equator.math.algebra.Theory;
import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public class WidgetsFader {
	@ModifyArgs(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
			),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F")
			)
	)
	private void setOpacity(Args args) {
		float r = args.get(0), g = args.get(1), b = args.get(2), a = args.get(3);
		args.set(0, (float) Theory.lerp(0, r, 1 - FadedWidgets.fading())); // red
		args.set(1, (float) Theory.lerp(0, g, 1 - FadedWidgets.fading())); // green
		args.set(2, (float) Theory.lerp(0, b, 1 - FadedWidgets.fading())); // blue
		args.set(3, (float) Theory.lerp(0, a, 1 - FadedWidgets.fading())); // alpha
	}

	@ModifyArgs(
			method = "renderVignetteOverlay",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
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
					target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
					shift = At.Shift.BEFORE
			)
	)
	private void tiltHotbarPre(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
					shift = At.Shift.AFTER
			)
	)
	private void tiltHotbarPost(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.pop();
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
					shift = At.Shift.BEFORE
			)
	)
	private void tiltHotbarItemPre(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		MatrixStack modelViewStack = RenderSystem.getModelViewStack();

		modelViewStack.push();
		double shift = 24 * FadedWidgets.fading();

		if (FadedWidgets.isVerticalityLoaded()) { // Support for Verticality
			modelViewStack.translate(-shift, 0, 0);
		} else {
			modelViewStack.translate(0, shift, 0);
		}

		RenderSystem.applyModelViewMatrix();
	}

	@Inject(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
					shift = At.Shift.AFTER
			)
	)
	private void tiltHotbarItemPost(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		MatrixStack modelViewStack = RenderSystem.getModelViewStack();

		modelViewStack.pop();
		RenderSystem.applyModelViewMatrix();
	}
}
