package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
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
            ComputerCartographer.log("Squaremap is not loaded.");
            enabled = false;
        }
        return enabled;
    }
}
