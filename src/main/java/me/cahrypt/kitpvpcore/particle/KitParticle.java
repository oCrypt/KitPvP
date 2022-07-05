package me.cahrypt.kitpvpcore.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class KitParticle {
    private final Particle particle;
    private final Particle.DustOptions dustOptions;

    public KitParticle(Particle particle) {
        this.particle = particle;
        this.dustOptions = null;
    }

    public KitParticle(Particle particle, Particle.DustOptions dustOptions) {
        this.particle = particle;
        this.dustOptions = dustOptions;
    }

    public void spawnParticle(World world, Location location) {
        world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 1, dustOptions);
    }
}
