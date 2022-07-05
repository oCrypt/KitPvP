package me.cahrypt.kitpvpcore.particle;

import org.bukkit.Location;
import org.bukkit.World;

public class CircleParticleSystem extends ParticleSystem {
    private final KitParticle kitParticle;
    private final double radius;


    public CircleParticleSystem(KitParticle kitParticle, double circleRadius) {
        this.kitParticle = kitParticle;
        this.radius = circleRadius;
    }

    public void spawnParticle(World world, Location location, double radius) {
        for (int i = 0; i <= 2*Math.PI*radius; i += 1) {
            double locX = (radius * Math.cos(i)) + location.getX();
            double locZ = (radius * Math.sin(i)) + location.getZ();
            Location newLoc = new Location(world, locX, location.getY() + 0.5, locZ);
            kitParticle.spawnParticle(world, newLoc);
        }
    }

    @Override
    public void spawnParticle(World world, Location location) throws IllegalArgumentException {
        spawnParticle(world, location, radius);
    }
}
