package net.jonko0493.cartographyperipheral.integration;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

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
            e.printStackTrace();
        }
    }

    @Override
    public boolean init() {
        enabled = dynmapAPI != null;
        return enabled;
    }
}
