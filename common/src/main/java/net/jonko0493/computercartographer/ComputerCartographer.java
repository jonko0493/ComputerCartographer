package net.jonko0493.computercartographer;

import dev.architectury.platform.Platform;
import net.jonko0493.computercartographer.integration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ComputerCartographer
{
	public static final String MOD_ID = "computer_cartographer";
	public static List<IMapIntegration> integrations = new ArrayList<>();
	public static final Logger Log = LoggerFactory.getLogger(MOD_ID);
	public static void log(String message) {
		Log.info(message);
	}

	public static void init() {
		Log.atInfo().log("Attempting to add cartography peripheral integrations...");
		if (Platform.isModLoaded("bluemap")) {
			BlueMapIntegration blueMapIntegration = new BlueMapIntegration();
			if (blueMapIntegration.init()) {
				log("Registered BlueMap integration!");
				integrations.add(blueMapIntegration);
			}
		}
		if (Platform.isModLoaded("dynmap")) {
			DynmapIntegration dynmapIntegration = new DynmapIntegration();
			if (dynmapIntegration.init()) {
				log("Registered Dynmap integration!");
				integrations.add(dynmapIntegration);
			}
		}
		if (Platform.isModLoaded("pl3xmap")) {
			Pl3xMapIntegration pl3xMapIntegration = new Pl3xMapIntegration();
			if (pl3xMapIntegration.init()) {
				log("Registered Pl3xMap integration!");
				integrations.add(pl3xMapIntegration);
			}
		}
		if (Platform.isModLoaded("squaremap")) {
			SquaremapIntegration squaremapIntegration = new SquaremapIntegration();
			if (squaremapIntegration.init()) {
				log("Registered Squaremap integration!");
				integrations.add(squaremapIntegration);
			}
		}
	}
}
