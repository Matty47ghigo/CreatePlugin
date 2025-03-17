package com.example.createPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateManager {
    private final CreatePlugin plugin;
    private final Map<String, Crate> crates = new HashMap<>();
    private final Map<Player, Crate> selectingPlayers = new HashMap<>();
    private final Map<String, Hologram> holograms = new HashMap<>();

    public CrateManager(CreatePlugin plugin) {
        this.plugin = plugin;
        loadCrates();
    }

    // Carica le crate dal file di configurazione
    public void loadCrates() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("crates");
        if (section == null) {
            Bukkit.getLogger().warning("Nessuna sezione 'crates' trovata nella configurazione.");
            return;
        }

        crates.clear();

        for (String key : section.getKeys(false)) {
            Crate crate = new Crate(plugin, key);

            // Carica la posizione della crate
            ConfigurationSection locationSection = section.getConfigurationSection(key + ".location");
            if (locationSection != null) {
                String worldName = locationSection.getString("world");
                double x = locationSection.getDouble("x");
                double y = locationSection.getDouble("y");
                double z = locationSection.getDouble("z");

                Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
                crate.setLocation(location);
            }

            crates.put(key, crate);
            Bukkit.getLogger().info("Crate '" + key + "' caricata con successo.");
        }
    }

    // Crea una nuova crate nel file di configurazione
    public void createCrate(String crateName) {
        if (crates.containsKey(crateName)) {
            Bukkit.getLogger().warning("La crate '" + crateName + "' esiste già.");
            return;
        }

        // Aggiungi la crate alla configurazione
        String path = "crates." + crateName;
        plugin.getConfig().set(path + ".name", crateName);
        plugin.getConfig().set(path + ".hologram.enabled", true);
        plugin.getConfig().set(path + ".hologram.text", new String[]{"&e&l" + crateName, "&7Apri per ottenere premi fantastici!"});
        plugin.getConfig().set(path + ".rewards", new String[]{});
        plugin.saveConfig();

        // Ricarica le crate
        loadCrates();
        Bukkit.getLogger().info("Crate '" + crateName + "' creata con successo.");
    }

    // Ottiene una crate dal nome
    public Crate getCrate(String name) {
        return crates.get(name);
    }

    // Imposta la posizione di una crate nel file di configurazione
    public void setCrateLocation(String crateName, Location location) {
        String path = "crates." + crateName + ".location";
        plugin.getConfig().set(path + ".world", location.getWorld().getName());
        plugin.getConfig().set(path + ".x", location.getX());
        plugin.getConfig().set(path + ".y", location.getY());
        plugin.getConfig().set(path + ".z", location.getZ());
        plugin.saveConfig();
    }

    // Apre una crate per un giocatore
    public void openCrate(Player player, Crate crate) {
        if (crate == null) {
            player.sendMessage("§cQuesta crate non esiste.");
            return;
        }

        // Crea un inventario simulato per la crate
        Inventory inventory = Bukkit.createInventory(null, 27, "§6§l" + crate.getName());

        // Aggiungi oggetti di esempio all'inventario
        Reward reward = crate.getRandomReward();
        if (reward != null) {
            ItemStack rewardItem = reward.toItemStack(); // Converti il Reward in ItemStack
            inventory.setItem(13, rewardItem); // Posiziona il premio al centro dell'inventario
        }

        // Apri l'inventario del giocatore
        player.openInventory(inventory);
        player.sendMessage("§aHai aperto la crate '" + crate.getName() + "'.");
    }

    // Aggiunge una chiave a un giocatore nel database
    public void addKey(UUID playerId, String crateName, int amount) {
        plugin.getSQLiteManager().addKey(playerId, crateName, amount);
    }

    // Aggiunge un hologramma associato a una crate
    public void addHologram(String crateName, Hologram hologram) {
        holograms.put(crateName, hologram);
    }

    // Rimuove un hologramma associato a una crate
    public void removeHologram(String crateName) {
        Hologram hologram = holograms.remove(crateName);
        if (hologram != null) {
            hologram.delete();
        }
    }

    // Rimuove tutti gli hologrammi
    public void clearHolograms() {
        for (Hologram hologram : holograms.values()) {
            hologram.delete();
        }
        holograms.clear();
    }

    // Abilita la modalità di selezione per un giocatore
    public void enableCrateSelection(Player player, Crate crate) {
        selectingPlayers.put(player, crate);
    }

    // Ottiene la crate selezionata da un giocatore
    public Crate getSelectedCrate(Player player) {
        return selectingPlayers.get(player);
    }

    // Cancella la selezione di una crate per un giocatore
    public void clearSelection(Player player) {
        selectingPlayers.remove(player);
    }
}