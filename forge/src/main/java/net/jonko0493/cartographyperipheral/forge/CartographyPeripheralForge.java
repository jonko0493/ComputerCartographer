package net.jonko0493.cartographyperipheral.forge;

import dev.architectury.platform.forge.EventBuses;
import net.jonko0493.cartographyperipheral.CartographyPeripheral;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CartographyPeripheral.MOD_ID)
public class CartographyPeripheralForge {
    public CartographyPeripheralForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CartographyPeripheral.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CartographyPeripheral.init();
    }
}