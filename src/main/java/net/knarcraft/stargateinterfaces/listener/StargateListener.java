package net.knarcraft.stargateinterfaces.listener;

import net.knarcraft.stargateinterfaces.StargateInterfaces;
import net.knarcraft.stargateinterfaces.color.ColorModification;
import net.knarcraft.stargateinterfaces.color.ColorModificationRegistry;
import net.knarcraft.stargateinterfaces.manager.OverrideManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.api.container.Holder;
import org.sgrewritten.stargate.api.event.portal.StargateClosePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateDeactivatePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateSignFormatPortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateTeleportPortalEvent;
import org.sgrewritten.stargate.api.network.portal.Portal;
import org.sgrewritten.stargate.api.network.portal.PortalPosition;
import org.sgrewritten.stargate.api.network.portal.RealPortal;
import org.sgrewritten.stargate.api.network.portal.formatting.AdventureStargateComponent;
import org.sgrewritten.stargate.api.network.portal.formatting.SignLine;
import org.sgrewritten.stargate.api.network.portal.formatting.StargateComponent;

import java.util.List;

/**
 * A listener for listening to Stargate events
 */
public class StargateListener implements Listener {

    private final StargateAPI stargateAPI;
    private final ColorModificationRegistry registry;
    private final StargateInterfaces plugin;

    public StargateListener(StargateAPI stargateAPI, ColorModificationRegistry registry, StargateInterfaces plugin) {
        this.stargateAPI = stargateAPI;
        this.registry = registry;
        this.plugin = plugin;
    }

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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void lineFormatListener(StargateSignFormatPortalEvent event) {
        PortalPosition portalPosition = event.getPortalPosition();
        RealPortal portal = portalPosition.getPortal();
        if (portal == null || portal.isDestroyed()) {
            return;
        }
        ColorModification colorModification = registry.getColorModificationForPortal(portal);
        formatLines(colorModification, event.getSign(), event.getLines());
    }

    private void formatLines(ColorModification colorModification, Sign sign, SignLine[] lines) {
        for (SignLine signLine : lines) {
            List<Holder<StargateComponent>> stargateComponents = signLine.getComponents();
            if (stargateComponents.size() == 3) {
                setColor(stargateComponents.get(0), colorModification.pointerColor());
                setColor(stargateComponents.get(1), colorModification.textColor());
                setColor(stargateComponents.get(2), colorModification.pointerColor());
            } else {
                for (Holder<StargateComponent> stargateComponent : stargateComponents) {
                    setColor(stargateComponent, colorModification.textColor());
                }
            }
        }
        if (colorModification.backgroundColor() == null) {
            return;
        }
        sign.setColor(colorModification.backgroundColor());
    }

    private void setColor(Holder<StargateComponent> component, @Nullable TextColor color) {
        if (color != null) {
            component.value = new AdventureStargateComponent(Component.text(component.value.plainText()).color(color));
        }
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
