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
	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void setOpacity(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha();
	}

	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void tiltHotbarPre(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.push();
		FadedWidgets.tiltBar(matrixStack);
	}

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void tiltHotbarPost(float tickDelta, MatrixStack matrixStack, CallbackInfo ci) {
		matrixStack.pop();
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
		FadedWidgets.setShaderAlpha();
	}

	@Inject(method = "renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V", at = @At("HEAD"))
	private void tiltSpectatorMenuPre(MatrixStack matrixStack, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
		matrixStack.push();
		FadedWidgets.tiltBar(matrixStack);
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
		FadedWidgets.tiltBar(matrixStack, false);
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

	@Inject(method = "renderExperienceBar", at = @At("HEAD"))
	private void tiltExperienceBarPre(MatrixStack matrixStack, int x, CallbackInfo ci) {
		matrixStack.push();
		FadedWidgets.tiltBar(matrixStack, false);
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
		FadedWidgets.tiltBar(matrixStack, false);
	}

	@Inject(method = "renderMountJumpBar", at = @At("TAIL"))
	private void tiltMountJumpBarPost(JumpingMount mount, MatrixStack matrixStack, int x, CallbackInfo ci) {
		matrixStack.pop();
	}
}
