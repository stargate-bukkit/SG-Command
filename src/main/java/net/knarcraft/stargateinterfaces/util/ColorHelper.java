package net.knarcraft.stargateinterfaces.util;

import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public class ColorHelper {
    /**
     * Gets the dye-color with the closest similarity of the chat-color
     *
     * @param color <p>Any chat color</p>
     * @return <p>A dye color similar to the chat-color</p>
     */
    public static DyeColor getClosestDyeColor(TextColor color) {
        int minProximity = -1;
        DyeColor closestColor = null;
        for (DyeColor dyeColor : DyeColor.values()) {
            int proximity = getColorProximity(color, dyeColor);
            if (proximity < minProximity || minProximity == -1) {
                minProximity = proximity;
                closestColor = dyeColor;
            }
        }
        return closestColor;
    }

    /**
     * Get an index on how similar the colors are
     *
     * @param color    <p>A chatColor</p>
     * @param dyeColor <p>A dyeColor</p>
     * @return <p> The proximity of the two colors </p>
     */
    private static int getColorProximity(TextColor color, DyeColor dyeColor) {
        Color bukkitColor = dyeColor.getColor();
        return Math.abs(color.red() - bukkitColor.getRed()) + Math.abs(color.green() -
                bukkitColor.getGreen()) + Math.abs(color.blue() - bukkitColor.getBlue());
    }
}
