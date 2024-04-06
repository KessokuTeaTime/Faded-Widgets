package band.kessokuteatime.fadedwidgets.mixin.fader;

import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListFader {
	@Inject(method = "render", at = @At("HEAD"))
	private void tiltPlayerListPre(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
		double height = MinecraftClient.getInstance().getWindow().getScaledHeight();

		context.getMatrices().push();
		context.getMatrices().translate(0, -height * FadedWidgets.fading(), 0);
	}
}
