package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

import java.util.Arrays;

public class DynmapIntegration implements IMapIntegration {
    private static DynmapCommonAPI dynmapAPI;
    private boolean enabled = false;

    static {
        try {
            DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {
                @Override
                public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
                    DynmapIntegration.dynmapAPI = dynmapCommonAPI;
                }
            });
        } catch (Exception e) {
            ComputerCartographer.log(e.getMessage());
            ComputerCartographer.log(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public boolean init() {
        enabled = dynmapAPI != null;
        return enabled;
    }
}
