package com.example.createPlugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class CratePlaceholderExpansion extends PlaceholderExpansion {
    private final CreatePlugin plugin;

    public CratePlaceholderExpansion(CreatePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "cratesystem"; // Identificatore dei placeholder
    }

    @Override
    public String getAuthor() {
        return "Matty47ghigo"; // Il tuo nome o nickname
    }

    @Override
    public String getVersion() {
        return "1.0.0"; // Versione della tua espansione
    }

    @Override
    public boolean persist() {
        return true; // Mantieni l'espansione attiva
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.startsWith("leaderboard_")) {
            String[] parts = params.split("_");
            if (parts.length < 4) return null;

            String crateName = parts[1];
            int page = Integer.parseInt(parts[2]);
            int row = Integer.parseInt(parts[3]);

            // Ottieni la leaderboard dal database (da implementare)
            return plugin.getSQLiteManager().getLeaderboard(crateName, page, row);
        }
        return null; // Ritorna null se il placeholder non è riconosciuto
    }
}