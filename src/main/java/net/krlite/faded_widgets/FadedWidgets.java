package net.krlite.faded_widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.krlite.equator.math.algebra.Curves;
import net.krlite.equator.visual.animation.animated.AnimatedDouble;
import net.krlite.equator.visual.animation.base.Animation;
import net.krlite.verticality.Verticality;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Unique;

import java.net.HttpURLConnection;

public class FadedWidgets implements ModInitializer {
	public static final String NAME = "Faded Widgets", ID = "faded-widgets";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	private static boolean hudHidden;
	private static boolean isVerticalityLoaded = false;
	private static final AnimatedDouble fading = new AnimatedDouble(0, 1, 400, Curves.Sinusoidal.EASE);

	static {
		fading.sensitive(true);
		fading.speedDirection(false);

		fading.onPlay(() -> {
			if (MinecraftClient.getInstance().options != null)
				if (!fading.isPositive()) MinecraftClient.getInstance().options.hudHidden = false;
		});

		fading.onTermination(() -> {
			if (MinecraftClient.getInstance().options != null)
				if (fading.isPositive()) MinecraftClient.getInstance().options.hudHidden = true;
		});
	}

	@Override
	public void onInitialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (fading.isPositive() != hudHidden) {
				fading.speedDirection(hudHidden);
				fading.play();
			}
		});

		isVerticalityLoaded = FabricLoader.getInstance().isModLoaded("verticality");
	}

	public static double fading() {
		return fading.value();
	}

	public static boolean hudHidden() {
		return hudHidden;
	}

	public static void hudHidden(boolean hudHidden) {
		FadedWidgets.hudHidden = hudHidden;
	}

	public static void switchHudHidden() {
		hudHidden(!hudHidden);
	}

	public static boolean isVerticalityLoaded() {
		return isVerticalityLoaded;
	}

	public static double shift() {
		return 24 * fading.value();
	}

	public static void setShaderColor() {
		float opacity = (float) (1 - fading());
		RenderSystem.setShaderColor(opacity, opacity, opacity, opacity);
	}

	public static void setShaderAlpha() {
		RenderSystem.setShaderColor(1, 1, 1, (float) (1 - fading()));
	}

	public static int getTextColor(int color) {
		int alpha = color > 0xFFFFFF ? color >> 24 & 0xFF : 0xFF;
		return color & 0xFFFFFF | (int) Math.max(0x11, alpha * (1 - fading())) << 24;
	}

	public static int getColor(int color) {
		int alpha = color > 0xFFFFFF ? color >> 24 & 0xFF : 0xFF;
		return color & 0xFFFFFF | (int) (alpha * (1 - fading())) << 24;
	}

	public static void tiltBar(MatrixStack matrixStack, boolean withVerticality) {
		if (withVerticality && isVerticalityLoaded() && Verticality.enabled()) {
			matrixStack.translate(-shift(), 0, 0);
		} else {
			matrixStack.translate(0, shift(), 0);
		}
	}

	public static void tiltBar(MatrixStack matrixStack) {
		tiltBar(matrixStack, true);
	}
}
