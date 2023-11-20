package net.jonko0493.computercartographer.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class MinecraftServerGetterImpl {
    public static MinecraftServer getServerInstance() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}
