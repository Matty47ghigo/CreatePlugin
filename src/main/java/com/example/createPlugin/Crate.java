package com.example.createPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Crate {
    private final String name;
    private final Animation animation;
    private final List<Reward> rewards = new ArrayList<>();
    private Location location;

    public Crate(CreatePlugin plugin, String name) {
        this.name = name;

        // Carica l'animazione dalla configurazione
        ConfigurationSection animationSection = plugin.getConfig().getConfigurationSection("crates." + name + ".animations");
        if (animationSection != null) {
            this.animation = new Animation(animationSection);
        } else {
            this.animation = null;
            Bukkit.getLogger().warning("Nessuna animazione trovata per la crate: " + name);
        }

        // Carica i premi dalla configurazione
        loadRewards(plugin);
    }

    // Ottiene il nome della crate
    public String getName() {
        return name;
    }

    // Ottiene l'animazione associata alla crate
    public Animation getAnimation() {
        return animation;
    }

    // Ottiene un premio casuale
    public Reward getRandomReward() {
        if (rewards.isEmpty()) {
            Bukkit.getLogger().warning("Nessun premio disponibile per la crate: " + name);
            return null;
        }
        return rewards.get(new Random().nextInt(rewards.size()));
    }

    // Carica i premi dalla configurazione
    private void loadRewards(CreatePlugin plugin) {
        ConfigurationSection rewardsSection = plugin.getConfig().getConfigurationSection("crates." + name + ".rewards");
        if (rewardsSection == null) {
            Bukkit.getLogger().warning("Nessuna sezione 'rewards' trovata per la crate: " + name);
            return;
        }

        for (String key : rewardsSection.getKeys(false)) {
            String materialName = rewardsSection.getString(key + ".material");
            int amount = rewardsSection.getInt(key + ".amount");

            Material material;
            try {
                material = Material.valueOf(materialName.toUpperCase());
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Materiale non valido: " + materialName);
                continue; // Salta questo premio se il materiale non è valido
            }

            ItemStack itemStack = createItemStack(material, amount);
            if (itemStack != null) {
                rewards.add(new Reward(material, amount, itemStack));
            }
        }
    }

    // Crea un oggetto ItemStack basato sul materiale e sulla quantità
    private ItemStack createItemStack(Material material, int amount) {
        ItemStack itemStack = new ItemStack(material, amount);

        // Ottieni l'ItemMeta per personalizzare l'oggetto
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            // Imposta il nome dell'oggetto
            meta.setDisplayName("§a" + material.name().replace("_", " ").toLowerCase());

            // Imposta una descrizione (lore) opzionale
            List<String> lore = Arrays.asList("§7Premio dalla crate!", "§bRaro e prezioso!");
            meta.setLore(lore);

            // Applica le modifiche all'ItemStack
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    // Imposta la posizione della crate
    public void setLocation(Location location) {
        this.location = location;
    }

    // Ottiene la posizione della crate
    public Location getLocation() {
        return location;
    }
}