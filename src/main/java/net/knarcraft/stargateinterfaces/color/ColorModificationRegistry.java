package net.knarcraft.stargateinterfaces.color;

import org.jetbrains.annotations.Nullable;
import org.sgrewritten.stargate.api.network.portal.RealPortal;

import java.util.ArrayList;
import java.util.List;

public class ColorModificationRegistry {

    private final List<ColorModification> colorModificationList = new ArrayList<>();

    public boolean contains(ColorModification colorModification) {
        return colorModificationList.contains(colorModification);
    }

    public void remove(ColorModification colorModification) {
        colorModificationList.remove(colorModification);
    }

    public void addOrUpdate(ColorModification colorModification) {
        if (contains(colorModification)) {
            colorModificationList.remove(colorModification);
        }
        colorModificationList.add(colorModification);
    }

    public @Nullable ColorModification getColorModificationForPortal(RealPortal portal) {
        ColorModification output = null;
        for(ColorModification colorModification : colorModificationList){
            if(colorModification.appliesTo(portal)){
                output = colorModification;
            }
        }
        return output;
    }
}
