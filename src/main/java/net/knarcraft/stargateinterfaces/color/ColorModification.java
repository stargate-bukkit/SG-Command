package net.knarcraft.stargateinterfaces.color;

import net.kyori.adventure.text.format.TextColor;

public record ColorModification(ColorModificationCategory category, TextColor pointerColor, TextColor textColor, ModificationTargetWrapper<?> modificationTargetWrapper) {

}
