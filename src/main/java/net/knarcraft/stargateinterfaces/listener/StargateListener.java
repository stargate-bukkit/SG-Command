package net.knarcraft.stargateinterfaces.listener;

import org.sgrewritten.stargate.api.event.StargateCloseEvent;
import org.sgrewritten.stargate.api.event.StargateDeactivateEvent;
import org.sgrewritten.stargate.api.event.StargatePortalEvent;
import org.sgrewritten.stargate.api.network.portal.Portal;

import net.knarcraft.stargateinterfaces.manager.OverrideManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * A listener for listening to Stargate events
 */
public class StargateListener implements Listener {

    @EventHandler
    public void teleportListener(StargatePortalEvent event) {
        removeOverriddenDestination(event.getPortal());
    }

    @EventHandler
    public void deactivateListener(StargateDeactivateEvent event) {
        removeOverriddenDestination(event.getPortal());
    }

    @EventHandler
    public void closeListener(StargateCloseEvent event) {
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
