package net.krlite.faded_widgets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.krlite.equator.math.algebra.Curves;
import net.krlite.equator.visual.animation.animated.AnimatedDouble;
import net.krlite.verticality.Verticality;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		return 24 * fading();
	}

	public static void setShaderColor(DrawContext context) {
		float opacity = (float) (1 - fading());
		context.setShaderColor(opacity, opacity, opacity, opacity);
	}

	public static void setShaderAlpha(DrawContext context) {
		context.setShaderColor(1, 1, 1, (float) (1 - fading()));
	}

	public static void tiltBar(DrawContext context, boolean withVerticality) {
		if (withVerticality && FadedWidgets.isVerticalityLoaded() && Verticality.enabled()) {
			context.getMatrices().translate(-FadedWidgets.shift(), 0, 0);
		} else {
			context.getMatrices().translate(0, FadedWidgets.shift(), 0);
		}
	}

	public static void tiltBar(DrawContext context) {
		tiltBar(context, true);
	}
}
