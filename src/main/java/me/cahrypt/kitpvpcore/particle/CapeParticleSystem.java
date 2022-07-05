package me.cahrypt.kitpvpcore.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class CapeParticleSystem extends ParticleSystem {
    private final KitParticle[][] capePattern;
    private final double spacing;

    public CapeParticleSystem(KitParticle[][] capePattern, double spacing) {
        this.capePattern = capePattern;
        this.spacing = spacing;
    }

    private Vector getBackVector(Location loc) {
        float newZ = (float) (loc.getZ() + (Math.sin(Math.toRadians(loc.getYaw() + 90))));
        float newX = (float) (loc.getX() + (Math.cos(Math.toRadians(loc.getYaw() + 90))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    @Override
    public void spawnParticle(World world, Location location) {
        double defX = location.getX() - (spacing * capePattern[0].length / 2) + spacing;
        double x = defX;
        double y = location.getY() + 2;
        double angle = -((location.getYaw() + 180) / 60) + (location.getYaw() < -180 ? 3.25 : 2.985);

        for (KitParticle[] kitParticleLine : capePattern) {
            for (KitParticle kitParticle : kitParticleLine) {
                if (kitParticle == null) {
                    x += spacing;
                    continue;
                }

                Location targetLoc = location.clone();
                targetLoc.setX(x);
                targetLoc.setY(y);

                Vector v = rotateAroundAxisY(targetLoc.toVector().subtract(location.toVector()), angle);
                Vector v2 = getBackVector(location).setY(0).multiply(-0.2);

                location.add(v).add(v2);
                kitParticle.spawnParticle(world, location);
                location.subtract(v).subtract(v2);

                x += spacing;
            }
            y -= spacing;
            x = defX;
        }
    }
}
