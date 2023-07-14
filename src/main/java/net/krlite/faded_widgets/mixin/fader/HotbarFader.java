package net.krlite.faded_widgets.mixin.fader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.gui.hud.spectator.SpectatorMenuState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.JumpingMount;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HotbarFader {
	@Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", shift = At.Shift.AFTER))
	private void setOpacity(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha();
	}

	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void tiltHotbarPre(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void tiltHotbarPost(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.pop();
	}
}

@Mixin(InGameHud.class)
class HotbarItemsFader {
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

@Mixin(SpectatorHud.class)
class SpectatorHudFader {
	@Redirect(
			method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
			)
	)
	private void setOpacity(float red, float green, float blue, float alpha) {
		FadedWidgets.setShaderColor();
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

	@Inject(method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V", at = @At("HEAD"))
	private void tiltSpectatorMenuPre(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V", at = @At("TAIL"))
	private void tiltSpectatorMenuPost(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
		matrixStack.pop();
	}
}

@Mixin(InGameHud.class)
class StatusBarsFader {
	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void setOpacity(MatrixStack matrixStack, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha();
	}

	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void tiltStatusBarsPre(MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(method = "renderStatusBars", at = @At("TAIL"))
	private void tiltStatusBarsPost(MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.pop();
	}
}

@Mixin(InGameHud.class)
class ExperienceBarFader {
	@Inject(method = "renderExperienceBar", at = @At("HEAD"))
	private void setOpacity(MatrixStack matrixStack, int x, CallbackInfo ci) {
		RenderSystem.enableBlend();
		FadedWidgets.setShaderAlpha();
	}

	@Redirect(
			method = "renderExperienceBar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"
			)
	)
	private int setTextOpacity(TextRenderer textRenderer, MatrixStack matrixStack, String text, float x, float y, int color) {
		return textRenderer.draw(matrixStack, text, x, y, FadedWidgets.getTextColor(color));
	}

	@Inject(method = "renderExperienceBar", at = @At("HEAD"))
	private void tiltExperienceBarPre(MatrixStack matrixStack, int x, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(method = "renderExperienceBar", at = @At("TAIL"))
	private void tiltExperienceBarPost(MatrixStack matrixStack, int x, CallbackInfo ci) {
		matrixStack.pop();
	}
}

@Mixin(InGameHud.class)
class MountJumpBarFader {
	@Inject(method = "renderMountJumpBar", at = @At("HEAD"))
	private void setOpacity(JumpingMount mount, MatrixStack matrixStack, int x, CallbackInfo ci) {
		RenderSystem.enableBlend();
		FadedWidgets.setShaderAlpha();
	}

	@Inject(method = "renderMountJumpBar", at = @At("HEAD"))
	private void tiltMountJumpBarPre(JumpingMount mount, MatrixStack matrixStack, int x, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, 24 * FadedWidgets.fading(), 0);
	}

	@Inject(method = "renderMountJumpBar", at = @At("TAIL"))
	private void tiltMountJumpBarPost(JumpingMount mount, MatrixStack matrixStack, int x, CallbackInfo ci) {
		matrixStack.pop();
	}
}
