package net.jonko0493.computercartographer.forge;

import dan200.computercraft.api.ForgeComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.architectury.platform.forge.EventBuses;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.peripheral.ComputerizedCartographerPeripheral;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nonnull;

@Mod(ComputerCartographer.MOD_ID)
public class ComputerCartographerForge {
    public ComputerCartographerForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ComputerCartographer.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ComputerCartographer.init();

        ForgeComputerCraftAPI.registerPeripheralProvider(new ComputerCartographerPeripheralProviderForge());
    }
}