package net.knarcraft.stargateinterfaces.color;

import net.knarcraft.stargateinterfaces.util.ColorHelper;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.sgrewritten.stargate.api.gate.GateAPI;
import org.sgrewritten.stargate.api.network.portal.PortalPosition;
import org.sgrewritten.stargate.api.network.portal.PositionType;
import org.sgrewritten.stargate.api.network.portal.RealPortal;

public record ColorModification(ColorModificationCategory category, TextColor pointerColor, TextColor textColor,
                                ModificationTargetWrapper<?> modificationTargetWrapper, DyeColor backgroundColor) {

    public ColorModification createModifiedInstance(ColorSelectionType colorSelectionType, TextColor color) {
        TextColor pointerColor = this.pointerColor;
        TextColor textColor = this.textColor;
        DyeColor backgroundColor = this.backgroundColor;
        switch (colorSelectionType) {
            case TEXT -> textColor = color;
            case POINTER -> pointerColor = color;
            case ALL -> {
                textColor = color;
                pointerColor = color;
                backgroundColor = ColorHelper.getClosestDyeColor(color);
            }
            case BACKGROUND -> ColorHelper.getClosestDyeColor(color);
        }
        return new ColorModification(this.category, pointerColor, textColor, this.modificationTargetWrapper, backgroundColor);
    }

    public boolean appliesTo(RealPortal realPortal) {
        return switch (category) {
            case NETWORK -> modificationTargetWrapper.isOfTarget(realPortal.getNetwork().getName());
            case MATERIAL -> {
                GateAPI gateAPI = realPortal.getGate();
                for (PortalPosition portalPosition : gateAPI.getPortalPositions()) {
                    if (portalPosition.getPositionType() != PositionType.SIGN) {
                        continue;
                    }
                    Location signLocation = gateAPI.getLocation(portalPosition.getRelativePositionLocation());
                    yield modificationTargetWrapper.isOfTarget(signLocation.getBlock().getType());
                }
                yield false;
            }
            case GATE -> modificationTargetWrapper.isOfTarget(realPortal.getGate().getFormat().getFileName());
            case GLOBAL -> true;
            case PORTAL -> false;
        };
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ColorModification colorModification)) {
            return false;
        }
        return colorModification.category.equals(this.category) && colorModification.textColor.equals(this.textColor) &&
                colorModification.pointerColor.equals(this.pointerColor) && colorModification.modificationTargetWrapper.getTargetString().equals(this.modificationTargetWrapper.getTargetString());
    }

    @Override
    public int hashCode() {
        return category.hashCode() * 11 + modificationTargetWrapper.getTargetString().hashCode();
    }
}
