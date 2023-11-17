package com.jonko0493.cartographyperipheral.fabric;

import com.jonko0493.cartographyperipheral.CartographyPeripheral;
import net.fabricmc.api.ModInitializer;

public class CartographyPeripheralFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CartographyPeripheral.init();
        System.out.println("test test test cartography");
    }
}