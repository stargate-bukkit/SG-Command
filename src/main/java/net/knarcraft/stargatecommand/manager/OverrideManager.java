package net.knarcraft.stargatecommand.manager;

import org.sgrewritten.stargate.network.portal.Portal;

import java.util.ArrayList;
import java.util.List;

/**
 * A manager for managing overridden destinations
 */
public final class OverrideManager {

    private static final List<Portal> overriddenPortals = new ArrayList<>();

    private OverrideManager() {

    }

    /**
     * Stores an over-ridden destination
     *
     * @param portal <p>The portal whose destination was overridden</p>
     */
    public static void storeOverriddenDestination(Portal portal) {
        overriddenPortals.add(portal);
    }

    /**
     * Removes an over-ridden destination for a portal
     *
     * @param portal <p>The portal to remove the overridden destination for</p>
     */
    public static void removeOverriddenDestination(Portal portal) {
        overriddenPortals.remove(portal);
    }

    /**
     * Gets whether the given portal's destination is currently overridden
     *
     * @param portal <p>The portal with a possibly overridden destination</p>
     */
    public static boolean hasOverriddenDestination(Portal portal) {
        return overriddenPortals.contains(portal);
    }

}