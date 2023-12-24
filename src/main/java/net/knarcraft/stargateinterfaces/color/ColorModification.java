package net.knarcraft.stargateinterfaces.color;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.sgrewritten.stargate.api.gate.GateAPI;
import org.sgrewritten.stargate.api.network.portal.PortalPosition;
import org.sgrewritten.stargate.api.network.portal.PositionType;
import org.sgrewritten.stargate.api.network.portal.RealPortal;

public record ColorModification(ColorModificationCategory category, TextColor pointerColor, TextColor textColor, ModificationTargetWrapper<?> modificationTargetWrapper) {


    public boolean appliesTo(RealPortal realPortal){
        return switch (category){
            case NETWORK -> modificationTargetWrapper.isOfTarget(realPortal.getNetwork().getName());
            case SIGN -> {
                GateAPI gateAPI = realPortal.getGate();
                for(PortalPosition portalPosition : gateAPI.getPortalPositions()){
                    if(portalPosition.getPositionType() != PositionType.SIGN){
                        continue;
                    }
                    Location signLocation = gateAPI.getLocation(portalPosition.getRelativePositionLocation());
                    yield modificationTargetWrapper.isOfTarget(signLocation.getBlock().getType());
                }
                yield false;
            }
            case GATE -> modificationTargetWrapper.isOfTarget(realPortal.getGate().getFormat().getFileName());
            case GLOBAL -> true;
        };
    }

    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }
        if(!(other instanceof ColorModification colorModification)){
            return false;
        }
        return colorModification.category.equals(this.category) && colorModification.textColor.equals(this.textColor) &&
                colorModification.pointerColor.equals(this.pointerColor) && colorModification.modificationTargetWrapper.getTargetString().equals(this.modificationTargetWrapper.getTargetString());
    }

    @Override
    public int hashCode(){
        return category.hashCode()*11 + modificationTargetWrapper.getTargetString().hashCode();
    }
}
