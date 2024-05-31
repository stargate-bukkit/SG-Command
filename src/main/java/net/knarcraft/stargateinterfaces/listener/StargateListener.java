package net.knarcraft.stargateinterfaces.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.knarcraft.stargateinterfaces.StargateInterfaces;
import net.knarcraft.stargateinterfaces.color.ColorModification;
import net.knarcraft.stargateinterfaces.color.ColorModificationRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.Nullable;
import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.api.event.portal.StargateSignFormatPortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateClosePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateDeactivatePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateTeleportPortalEvent;
import org.sgrewritten.stargate.api.network.portal.Portal;

import net.knarcraft.stargateinterfaces.manager.OverrideManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.sgrewritten.stargate.api.network.portal.PortalPosition;
import org.sgrewritten.stargate.api.network.portal.RealPortal;
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

    public StargateListener(StargateAPI stargateAPI, ColorModificationRegistry registry, StargateInterfaces plugin){
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
    public void lineFormatListener(StargateSignFormatPortalEvent event){
        PortalPosition portalPosition = event.getPortalPosition();
        RealPortal portal = portalPosition.getPortal();
        if(portal == null || portal.isDestroyed()){
            return;
        }
        ColorModification colorModification = registry.getColorModificationForPortal(portal);
        formatLines(colorModification, event.getSign(), event.getLines());
    }

    private void formatLines(ColorModification colorModification, Sign sign, SignLine[] lines) {
        for(SignLine signLine : lines){
            List<StargateComponent> stargateComponents = signLine.getComponents();
            if(stargateComponents.size() == 3){
                setColor(stargateComponents.get(0),colorModification.pointerColor());
                setColor(stargateComponents.get(1),colorModification.textColor());
                setColor(stargateComponents.get(2),colorModification.pointerColor());
            } else {
                for(StargateComponent stargateComponent : stargateComponents){
                    setColor(stargateComponent,colorModification.textColor());
                }
            }
        }
        if(colorModification.backgroundColor() != null){
            sign.setColor(colorModification.backgroundColor());
        }
        Bukkit.getScheduler().runTask(plugin, () -> sign.update());
    }

    private void setColor(StargateComponent component, @Nullable TextColor color){
        if(color != null) {
            component.setText(component.getText().color(color));
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
