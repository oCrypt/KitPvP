package me.cahrypt.kitpvpcore.particle;

import org.bukkit.Location;
import org.bukkit.World;

public class SphereParticleSystem extends ParticleSystem {
    private final KitParticle kitParticle;
    private final double size;

    public SphereParticleSystem(KitParticle kitParticle, double sphereSize) {
        this.kitParticle = kitParticle;
        this.size = sphereSize;
    }

    protected void spawnParticle(World world, Location location, double sphereSize) throws IllegalArgumentException {
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double sphereRadius = sphereSize * Math.sin(i);
            double locY = sphereSize * Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                double locX = Math.cos(a) * sphereRadius;
                double locZ = Math.sin(a) * sphereRadius;
                location.add(locX, locY, locZ);
                kitParticle.spawnParticle(world, location);
                location.subtract(locX, locY, locZ);
            }
        }
    }

    @Override
    public void spawnParticle(World world, Location location) {
        spawnParticle(world, location, size);
    }
}
