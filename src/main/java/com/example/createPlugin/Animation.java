package com.example.createPlugin;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

public class Animation {
    private final String type;
    private final Particle particle;
    private final Color color;
    private final double size;

    public Animation(ConfigurationSection section) {
        if (section == null) {
            throw new IllegalArgumentException("La sezione di configurazione per l'animazione non può essere null.");
        }

        this.type = section.getString("type", "DEFAULT_TYPE");
        String particleName = section.getString("particle", "REDSTONE");
        String colorHex = section.getString("color", "FFFFFF");
        this.size = section.getDouble("size", 2.0);

        this.particle = Particle.valueOf(particleName.toUpperCase());
        this.color = Color.fromRGB(Integer.parseInt(colorHex, 16));
    }

    public String getType() {
        return type;
    }

    public Particle getParticle() {
        return particle;
    }

    public Color getColor() {
        return color;
    }

    public double getSize() {
        return size;
    }
}