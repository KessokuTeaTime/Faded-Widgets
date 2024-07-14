package band.kessokuteatime.fadedwidgets.mixin;

import band.kessokuteatime.fadedwidgets.FadedWidgets;
import net.minecraft.client.Keyboard;
import net.minecraft.client.option.GameOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Redirect(
			method = "onKey",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z",
					opcode = Opcodes.PUTFIELD
			)
	)
	private void onKey(GameOptions options, boolean value) {
		FadedWidgets.switchHudHidden();
	}
}
