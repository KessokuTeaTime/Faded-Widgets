package net.krlite.fadedwidgets.mixin;

import net.krlite.fadedwidgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow @Final public GameOptions options;

	@Inject(
			method = "<init>",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/MinecraftClient;options:Lnet/minecraft/client/option/GameOptions;",
					shift = At.Shift.AFTER
			)
	)
	private void updateHudHidden(RunArgs args, CallbackInfo ci) {
		FadedWidgets.hudHidden(options.hudHidden);
	}
}
