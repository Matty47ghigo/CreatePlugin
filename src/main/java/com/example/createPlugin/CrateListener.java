package com.example.createPlugin;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Location;
import org.bukkit.event.block.Action;

public class CrateListener implements Listener {
    private final CreatePlugin plugin;

    public CrateListener(CreatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        // Verifica se il giocatore ha cliccato con il tasto sinistro su uno Shulker Box
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && block != null && block.getType() == Material.SHULKER_BOX) {
            Crate crate = plugin.getCrateManager().getSelectedCrate(player);
            if (crate == null) {
                return; // Il giocatore non è in modalità di selezione
            }

            // Ottieni la posizione dello Shulker Box
            Location location = block.getLocation();

            // Imposta la posizione della crate nel file di configurazione
            plugin.getCrateManager().setCrateLocation(crate.getName(), location);

            // Crea l'hologramma e le particelle
            createHologram(location, crate);
            createParticles(location, crate);

            // Pulisci la selezione del giocatore
            plugin.getCrateManager().clearSelection(player);
            player.sendMessage("§aCrate '" + crate.getName() + "' impostata con successo!");
        }
    }

    // Crea un hologramma sopra la crate
    private void createHologram(Location location, Crate crate) {
        String hologramText = "§6" + crate.getName() + " §7Crate";
        Location hologramLocation = location.clone().add(0.5, 1.5, 0.5); // Posizione leggermente sopra il blocco
        Hologram hologram = DHAPI.createHologram(crate.getName(), hologramLocation, true,
                java.util.Arrays.asList(
                        "§6" + crate.getName() + " §7Crate",
                        "§ePremi per aprire!"
                )
        );
        plugin.getCrateManager().addHologram(crate.getName(), hologram);
    }

    // Crea particelle intorno alla crate
    private void createParticles(Location location, Crate crate) {
        Animation animation = crate.getAnimation();
        if (animation == null) {
            return;
        }

        plugin.getAnimationManager().playAnimation(null, location, crate);
    }
}