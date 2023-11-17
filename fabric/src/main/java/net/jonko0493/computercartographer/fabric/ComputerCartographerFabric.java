package net.jonko0493.computercartographer.fabric;

import net.jonko0493.computercartographer.ComputerCartographer;
import net.fabricmc.api.ModInitializer;

public class ComputerCartographerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ComputerCartographer.init();
    }
}