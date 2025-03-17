package com.example.createPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class AnimationManager {
    private final CreatePlugin plugin;

    public AnimationManager(CreatePlugin plugin) {
        this.plugin = plugin;
    }

    public void playAnimation(Player player, Location location, Crate crate) {
        Animation animation = crate.getAnimation();
        if (animation == null) {
            Bukkit.getLogger().warning("Nessuna animazione trovata per la crate: " + crate.getName());
            return;
        }

        Particle particle = animation.getParticle();
        Color color = animation.getColor();
        double size = animation.getSize();

        // Esempio di animazione: cerchio di particelle
        for (double angle = 0; angle < 360; angle += 10) {
            double radians = Math.toRadians(angle);
            double x = Math.cos(radians) * size;
            double z = Math.sin(radians) * size;

            location.add(x, 0, z);

            // Gestisci la particella SOUL con DustOptions
            if (particle == Particle.SOUL) {
                Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f); // Colore e dimensione
                player.spawnParticle(particle, location, 1, dustOptions);
            } else {
                // Per altre particelle, usa il metodo standard
                player.spawnParticle(particle, location, 1);
            }

            location.subtract(x, 0, z);
        }
    }
}