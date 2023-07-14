package net.krlite.faded_widgets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.equator.math.algebra.Curves;
import net.krlite.equator.visual.animation.animated.AnimatedDouble;
import net.krlite.equator.visual.animation.base.Animation;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FadedWidgets implements ModInitializer {
	public static final String NAME = "Faded Widgets", ID = "faded-widgets";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	private static boolean hudHidden;
	private static final AnimatedDouble fading = new AnimatedDouble(1, 0, 400, Curves.Sinusoidal.EASE);

	static {
		fading.sensitive(true);
		fading.speedDirection(false);

		fading.onStart(() -> {
			if (!fading.isPositive()) MinecraftClient.getInstance().options.hudHidden = false;
		});

		fading.onCompletion(() -> {
			if (fading.isPositive()) MinecraftClient.getInstance().options.hudHidden = true;
		});
	}

	@Override
	public void onInitialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (fading.isPositive() == hudHidden) {
				fading.speedDirection(!hudHidden);
				fading.play();
			}
		});
	}

	public static double fading() {
		return fading.value();
	}

	public static void switchHudHidden() {
		hudHidden = !hudHidden;
	}
}
