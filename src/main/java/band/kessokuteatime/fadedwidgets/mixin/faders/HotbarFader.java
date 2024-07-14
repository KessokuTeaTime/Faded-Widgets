package band.kessokuteatime.fadedwidgets.mixin.faders;

import com.mojang.blaze3d.systems.RenderSystem;
import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.gui.hud.spectator.SpectatorMenuState;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.JumpingMount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HotbarFader {
	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void setOpacity(DrawContext context, RenderTickCounter renderTickCounter, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha(context);
	}

	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void tiltHotbarPre(DrawContext context, RenderTickCounter renderTickCounter, CallbackInfo ci) {
		context.getMatrices().push();
		FadedWidgets.tiltBar(context);
	}

	@Inject(method = "renderHotbar", at = @At("TAIL"))
	private void tiltHotbarPost(DrawContext context, RenderTickCounter renderTickCounter, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}

@Mixin(SpectatorHud.class)
class SpectatorHudFader {
	@Redirect(
			method = "renderSpectatorMenu(Lnet/minecraft/client/gui/DrawContext;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;setShaderColor(FFFF)V"
			)
	)
	private void setOpacity(DrawContext context, float red, float green, float blue, float alpha) {
		FadedWidgets.setShaderAlpha(context);
	}

	@Inject(method = "renderSpectatorMenu(Lnet/minecraft/client/gui/DrawContext;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V", at = @At("HEAD"))
	private void tiltSpectatorMenuPre(DrawContext context, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
		context.getMatrices().push();
		FadedWidgets.tiltBar(context);
	}

	@Inject(method = "renderSpectatorMenu(Lnet/minecraft/client/gui/DrawContext;FIILnet/minecraft/client/gui/hud/spectator/SpectatorMenuState;)V", at = @At("TAIL"))
	private void tiltSpectatorMenuPost(DrawContext context, float height, int x, int y, SpectatorMenuState state, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}

@Mixin(InGameHud.class)
class StatusBarsFader {
	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void setOpacity(DrawContext context, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha(context);
	}

	@Inject(method = "renderStatusBars", at = @At("HEAD"))
	private void tiltStatusBarsPre(DrawContext context, CallbackInfo ci) {
		context.getMatrices().push();
		FadedWidgets.tiltBar(context, false);
	}

	@Inject(method = "renderStatusBars", at = @At("TAIL"))
	private void tiltStatusBarsPost(DrawContext context, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}

@Mixin(InGameHud.class)
class ExperienceBarFader {
	@Inject(method = "renderExperienceBar", at = @At("HEAD"))
	private void setOpacity(DrawContext context, int x, CallbackInfo ci) {
		RenderSystem.enableBlend();
		FadedWidgets.setShaderAlpha(context);
	}

	@Inject(method = "renderExperienceBar", at = @At("HEAD"))
	private void tiltExperienceBarPre(DrawContext context, int x, CallbackInfo ci) {
		context.getMatrices().push();
		FadedWidgets.tiltBar(context, false);
	}

	@Inject(method = "renderExperienceBar", at = @At("TAIL"))
	private void tiltExperienceBarPost(DrawContext context, int x, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}

@Mixin(InGameHud.class)
class MountJumpBarFader {
	@Inject(method = "renderMountJumpBar", at = @At("HEAD"))
	private void setOpacity(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
		RenderSystem.enableBlend();
		FadedWidgets.setShaderAlpha(context);
	}

	@Inject(method = "renderMountJumpBar", at = @At("HEAD"))
	private void tiltMountJumpBarPre(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
		context.getMatrices().push();
		FadedWidgets.tiltBar(context, false);
	}

	@Inject(method = "renderMountJumpBar", at = @At("TAIL"))
	private void tiltMountJumpBarPost(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}
