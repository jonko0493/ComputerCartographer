package com.jonko0493.cartographyperipheral;

import com.jonko0493.cartographyperipheral.integration.*;
import dev.architectury.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CartographyPeripheral
{
	public static final String MOD_ID = "cartography_peripheral";
	public static List<IMapIntegration> integrations = new ArrayList<>();
	public static Logger Log;

	public static void init() {
		Log = LoggerFactory.getLogger("CartographyPeripheral");
		Log.atInfo().log("Attempting to add cartography peripheral integrations...");
		if (Platform.isModLoaded("bluemap")) {
			BlueMapIntegration blueMapIntegration = new BlueMapIntegration();
			if (blueMapIntegration.init()) {
				Log.atInfo().log("Registered BlueMap integration!");
				integrations.add(blueMapIntegration);
			}
		}
		if (Platform.isModLoaded("dynmap")) {
			DynmapIntegration dynmapIntegration = new DynmapIntegration();
			if (dynmapIntegration.init()) {
				Log.atInfo().log("Registered Dynmap integration!");
				integrations.add(dynmapIntegration);
			}
		}
		if (Platform.isModLoaded("pl3xmap")) {
			Pl3xMapIntegration pl3xMapIntegration = new Pl3xMapIntegration();
			if (pl3xMapIntegration.init()) {
				Log.atInfo().log("Registered Pl3xMap integration!");
				integrations.add(pl3xMapIntegration);
			}
		}
		if (Platform.isModLoaded("squaremap")) {
			SquaremapIntegration squaremapIntegration = new SquaremapIntegration();
			if (squaremapIntegration.init()) {
				Log.atInfo().log("Registered Squaremap integration!");
				integrations.add(squaremapIntegration);
			}
		}
	}
}
