package com.jonko0493.cartographyperipheral.integration;

import com.jonko0493.cartographyperipheral.CartographyPeripheral;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public class SquaremapIntegration implements IMapIntegration {
    private Squaremap api;
    private boolean enabled = false;

    @Override
    public boolean init() {
        try {
            api = SquaremapProvider.get();
            enabled = true;
        } catch (Exception e) {
            CartographyPeripheral.Log.atInfo().log("Squaremap is not loaded.");
            enabled = false;
        }
        return enabled;
    }
}
