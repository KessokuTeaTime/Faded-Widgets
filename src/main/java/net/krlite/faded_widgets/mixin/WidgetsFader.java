package net.krlite.faded_widgets.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.equator.math.algebra.Theory;
import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public class WidgetsFader {
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/SpectatorHud;renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacitySpectatorMenu(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
		FadedWidgets.setShaderColor();
	}

	@Redirect(
			method = "renderHotbar",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
			)
	)
	private void setOpacityHotbar(float red, float green, float blue, float alpha) {
		FadedWidgets.setShaderAlpha();
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacityCrosshair(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
		FadedWidgets.setShaderColor();
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/util/math/MatrixStack;III)V",
					shift = At.Shift.BEFORE
			)
	)
	private void setOpacityChatHud(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha();
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

@Mixin(ChatHud.class)
class ChatHudFader {
	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"
			)
	)
	private int setTextOpacity(TextRenderer textRenderer, MatrixStack matrixStack, OrderedText text, float x, float y, int color) {
		return textRenderer.drawWithShadow(matrixStack, text, x, y, FadedWidgets.getTextColor(color));
	}

	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
			)
	)
	private int setTextOpacityOrdered(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
		return textRenderer.drawWithShadow(matrixStack, text, x, y, FadedWidgets.getTextColor(color));
	}
}

@Mixin(BossBarHud.class)
class BossBarHudFader {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))
	private void setOpacity(float red, float green, float blue, float alpha) {
		FadedWidgets.setShaderAlpha();
	}

	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
			)
	)
	private int setTextOpacity(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
		return textRenderer.drawWithShadow(matrixStack, text, x, y, FadedWidgets.getTextColor(color));
	}
}
