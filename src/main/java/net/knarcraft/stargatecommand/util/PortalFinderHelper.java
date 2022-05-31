package net.knarcraft.stargatecommand.util;

import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

/**
 * A helper class for helping with finding portals in the world
 */
public final class PortalFinderHelper {

    private PortalFinderHelper() {

    }

    /**
     * Find the portal a living entity is looking at using a single traced ray
     *
     * @param registryAPI  <p>The registry used for looking up portals</p>
     * @param livingEntity <p>The living entity to ray trace from</p>
     * @param rayLength    <p>The maximum length of the ray before giving up</p>
     * @return <p>The portal the player is looking at, or null if no portal was found</p>
     */
    public static Portal findPortalByRaytrace(RegistryAPI registryAPI, LivingEntity livingEntity, int rayLength) {
        Location entityLocation = livingEntity.getLocation().add(0, livingEntity.getEyeHeight(), 0);
        Vector entityDirection = livingEntity.getLocation().getDirection();
        Portal foundPortal = null;

        //Follow the ray outwards from the entity until a portal is found
        for (int i = 0; i < rayLength; i++) {
            entityLocation.add(entityDirection);
            foundPortal = registryAPI.getPortal(entityLocation);
            //Stop if a portal is found. Also, don't trace through solid blocks.
            Material traceMaterial = entityLocation.getBlock().getType();
            if (foundPortal != null || (!traceMaterial.isAir() && traceMaterial != Material.WATER)) {
                break;
            }
        }
        return foundPortal;
    }

}
