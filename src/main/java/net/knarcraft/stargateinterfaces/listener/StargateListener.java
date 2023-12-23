package net.knarcraft.stargateinterfaces.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.api.event.gate.StargateSignFormatGateEvent;
import org.sgrewritten.stargate.api.event.portal.StargateClosePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateDeactivatePortalEvent;
import org.sgrewritten.stargate.api.event.portal.StargateTeleportPortalEvent;
import org.sgrewritten.stargate.api.network.portal.Portal;

import net.knarcraft.stargateinterfaces.manager.OverrideManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.sgrewritten.stargate.api.network.portal.PortalPosition;
import org.sgrewritten.stargate.api.network.portal.RealPortal;
import org.sgrewritten.stargate.api.network.portal.format.SignLine;
import org.sgrewritten.stargate.api.network.portal.format.StargateComponent;

import java.util.List;

/**
 * A listener for listening to Stargate events
 */
public class StargateListener implements Listener {

    private final StargateAPI stargateAPI;

    public StargateListener(StargateAPI stargateAPI){
        this.stargateAPI = stargateAPI;
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

    @EventHandler
    public void lineFormatListener(StargateSignFormatGateEvent event){
        PortalPosition portalPosition = event.getPortalPosition();
        RealPortal portal = stargateAPI.getRegistry().getPortalFromPortalPosition(portalPosition);
        if(portal == null || portal.isDestroyed()){
            return;
        }
        String metaData = portalPosition.getMetaData(portal);
        modifySignLineFromMetaData(event.getLines(),metaData);
    }

    private void modifySignLineFromMetaData(SignLine[] lines, String metaData) {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(metaData);
        JsonElement jsonElement = jsonObject.get("COLOR_OVERRIDES");
        if(!(jsonElement instanceof JsonObject colorOverrides)){
            return;
        }
        String pointerColorString = colorOverrides.get("pointer").getAsString();
        String textColorString = colorOverrides.get("text").getAsString();
        for(int i = 0; i < 4; i++){
            SignLine line = lines[i];
            List<StargateComponent> componentList =  line.getComponents();
            StargateComponent sgComponent = componentList.get(0);
            Component component;
            if(componentList.size() > 1){
                component = sgComponent.getText().color(TextColor.fromHexString(pointerColorString));
                sgComponent.setText(component);
                sgComponent = componentList.get(1);
                component = sgComponent.getText().color(TextColor.fromHexString(textColorString));
                sgComponent.setText(component);
                sgComponent = componentList.get(2);
                component = sgComponent.getText().color(TextColor.fromHexString(pointerColorString));
            } else {
                component = sgComponent.getText().color(TextColor.fromHexString(textColorString));
            }
            sgComponent.setText(component);
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
