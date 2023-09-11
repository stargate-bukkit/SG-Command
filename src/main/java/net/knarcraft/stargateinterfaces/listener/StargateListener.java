package net.knarcraft.stargateinterfaces.listener;

import org.sgrewritten.stargate.api.event.portal.StargateClosePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateDeactivatePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateTeleportPortalEvent;
import org.sgrewritten.stargate.api.network.portal.Portal;

import net.knarcraft.stargateinterfaces.manager.OverrideManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * A listener for listening to Stargate events
 */
public class StargateListener implements Listener {

    @EventHandler
    public void teleportListener(StargateTeleportPortalEvent event) {
        removeOverriddenDestination(event.getPortal());
    }

    @EventHandler
    public void deactivateListener(StargateDeactivatePortalEvent event) {
        removeOverriddenDestination(event.getPortal());
    }

    @EventHandler
    public void closeListener(StargateClosePortalEvent event) {
        removeOverriddenDestination(event.getPortal());
    }

    /**
     * Removes the override for the given portal, if this plugin has overridden its destination
     *
     * @param portal <p>The portal to remove the override from</p>
     */
    private void removeOverriddenDestination(Portal portal) {
        boolean isOverridden = OverrideManager.hasOverriddenDestination(portal);
        if (isOverridden) {
            portal.overrideDestination(null);
            OverrideManager.removeOverriddenDestination(portal);
        }
    }

}
