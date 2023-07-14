package net.krlite.faded_widgets.mixin.fader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class HandFader {
	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	private void tiltHand(MatrixStack matrixStack, Camera camera, float tickDelta, CallbackInfo ci) {
		matrixStack.translate(0, -0.7 * FadedWidgets.fading(), 0);
	}
}
