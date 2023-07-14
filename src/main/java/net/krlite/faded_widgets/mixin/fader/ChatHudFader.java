package net.krlite.faded_widgets.mixin.fader;

import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudFader {
	@Inject(method = "render", at = @At("HEAD"))
	private void setOpacity(MatrixStack matrixStack, int currentTick, int mouseX, int mouseY, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha();
	}
}
