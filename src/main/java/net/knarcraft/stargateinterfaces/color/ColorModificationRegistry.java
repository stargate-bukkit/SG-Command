package net.knarcraft.stargateinterfaces.color;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sgrewritten.stargate.api.network.portal.RealPortal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorModificationRegistry {

    private final List<ColorModification> colorModificationList = new ArrayList<>();

    public boolean contains(@NotNull ColorModification colorModification) {
        return colorModificationList.contains(Objects.requireNonNull(colorModification));
    }

    public void remove(@NotNull ColorModification colorModification) {
        colorModificationList.remove(Objects.requireNonNull(colorModification));
    }

    public void addOrUpdate(@NotNull ColorModification colorModification) {
        if (contains(Objects.requireNonNull(colorModification))) {
            colorModificationList.remove(colorModification);
        }
        colorModificationList.add(colorModification);
    }

    public @Nullable ColorModification getColorModificationForPortal(RealPortal portal) {
        ColorModification output = new ColorModification(ColorModificationCategory.GLOBAL,null,null,new ModificationTargetWrapper<>("all"),null);
        for(ColorModification colorModification : colorModificationList){
            if(colorModification.appliesTo(portal)){
                output = output.mergeColors(colorModification);
            }
        }
        return output;
    }

    public @Nullable ColorModification getColorModification(ColorModificationCategory colorModificationCategory,
                                                            ModificationTargetWrapper<?> modificationTargetWrapper){
        for(ColorModification colorModification : colorModificationList){
            if(colorModification.category() == colorModificationCategory && colorModification.modificationTargetWrapper().equals(modificationTargetWrapper)){
                return colorModification;
            }
        }
        return null;
    }
}
