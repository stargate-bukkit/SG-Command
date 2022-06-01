package net.knarcraft.stargatecommand.listener;

import net.TheDgtl.Stargate.event.StargateCloseEvent;
import net.TheDgtl.Stargate.event.StargateDeactivateEvent;
import net.TheDgtl.Stargate.event.StargatePortalEvent;
import net.TheDgtl.Stargate.network.portal.Portal;
import net.knarcraft.stargatecommand.manager.OverrideManager;
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
