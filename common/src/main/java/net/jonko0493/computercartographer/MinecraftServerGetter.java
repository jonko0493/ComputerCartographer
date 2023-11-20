package net.jonko0493.computercartographer;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;

public class MinecraftServerGetter {
    @ExpectPlatform
    public static MinecraftServer getServerInstance() {
        throw new AssertionError();
    }
}
