package net.jonko0493.computercartographer.forge;

import dev.architectury.platform.forge.EventBuses;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ComputerCartographer.MOD_ID)
public class ComputerCartographerForge {
    public ComputerCartographerForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ComputerCartographer.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ComputerCartographer.init();
    }
}