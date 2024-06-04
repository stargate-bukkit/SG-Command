package net.knarcraft.stargateinterfaces.color;

import org.sgrewritten.stargate.network.portal.GlobalPortalId;

public class ColorUtils {

    private ColorUtils(){
        throw new IllegalStateException("Utility class");
    }

    public static String convertIdToString(GlobalPortalId id){
        return id.networkId() + ">" + id.portalId();
    }
}
