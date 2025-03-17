package com.example.createPlugin;

import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLiteManager {
    private final CreatePlugin plugin;
    private Connection connection;

    public SQLiteManager(CreatePlugin plugin) {
        this.plugin = plugin;
        setupDatabase();
    }

    // Inizializza il database e crea la tabella se non esiste
    private void setupDatabase() {
        try {
            // Carica il driver SQLite
            Class.forName("org.sqlite.JDBC");

            // Crea una connessione al database
            String dbPath = plugin.getDataFolder().getPath() + "/database.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            // Crea la tabella delle chiavi se non esiste
            String createTable = "CREATE TABLE IF NOT EXISTS keys (" +
                    "uuid VARCHAR(36) NOT NULL," +
                    "crate_name VARCHAR(255) NOT NULL," +
                    "amount INTEGER NOT NULL," +
                    "PRIMARY KEY (uuid, crate_name)" +
                    ");";
            connection.createStatement().execute(createTable);

            // Crea la tabella per la leaderboard (opzionale)
            String createLeaderboardTable = "CREATE TABLE IF NOT EXISTS crate_keys (" +
                    "player_id TEXT NOT NULL," +
                    "crate_name TEXT NOT NULL," +
                    "key_count INTEGER DEFAULT 0," +
                    "PRIMARY KEY (player_id, crate_name)" +
                    ");";
            connection.createStatement().execute(createLeaderboardTable);

            Bukkit.getLogger().info("Database SQLite creato/caricato con successo.");
        } catch (Exception e) {
            Bukkit.getLogger().severe("Errore durante l'inizializzazione del database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Chiude la connessione al database
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Bukkit.getLogger().info("Connessione al database SQLite chiusa.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Errore durante la chiusura del database SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Aggiunge chiavi virtuali al giocatore
    public void addKey(UUID playerId, String crateName, int amount) {
        try {
            // Verifica se il giocatore ha già chiavi per questa crate
            String query = "SELECT amount FROM keys WHERE uuid = ? AND crate_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerId.toString());
            statement.setString(2, crateName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Aggiorna il numero di chiavi esistente
                int currentAmount = resultSet.getInt("amount");
                int newAmount = currentAmount + amount;

                String updateQuery = "UPDATE keys SET amount = ? WHERE uuid = ? AND crate_name = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, newAmount);
                updateStatement.setString(2, playerId.toString());
                updateStatement.setString(3, crateName);
                updateStatement.executeUpdate();
            } else {
                // Inserisci una nuova riga per il giocatore e la crate
                String insertQuery = "INSERT INTO keys (uuid, crate_name, amount) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, playerId.toString());
                insertStatement.setString(2, crateName);
                insertStatement.setInt(3, amount);
                insertStatement.executeUpdate();
            }

            Bukkit.getLogger().info("Aggiunte " + amount + " chiavi per la crate '" + crateName + "' al giocatore " + playerId);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Errore durante l'aggiunta delle chiavi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Controlla se il giocatore ha almeno una chiave per la crate
    public boolean hasKey(UUID playerId, String crateName) {
        String query = "SELECT amount FROM keys WHERE uuid = ? AND crate_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerId.toString());
            statement.setString(2, crateName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int keyCount = resultSet.getInt("amount");
                return keyCount > 0;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Errore durante il controllo delle chiavi: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Rimuove una chiave dal giocatore
    public void removeKey(UUID playerId, String crateName) {
        String query = "UPDATE keys SET amount = amount - 1 WHERE uuid = ? AND crate_name = ? AND amount > 0";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerId.toString());
            statement.setString(2, crateName);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                Bukkit.getLogger().info("Rimossa una chiave per la crate '" + crateName + "' dal giocatore " + playerId);
            } else {
                Bukkit.getLogger().warning("Impossibile rimuovere una chiave per la crate '" + crateName + "' dal giocatore " + playerId);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Errore durante la rimozione della chiave: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Ottiene la leaderboard per una crate specifica
    public String getLeaderboard(String crateName, int page, int row) {
        StringBuilder leaderboard = new StringBuilder();
        leaderboard.append("§6Leaderboard per la crate '").append(crateName).append("' (Pagina ").append(page).append("):\n");

        String query = "SELECT player_id, key_count FROM crate_keys WHERE crate_name = ? ORDER BY key_count DESC LIMIT ? OFFSET ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, crateName);
            statement.setInt(2, row); // Numero di righe per pagina
            statement.setInt(3, (page - 1) * row); // Paginazione
            ResultSet resultSet = statement.executeQuery();

            int rank = (page - 1) * row + 1;
            while (resultSet.next()) {
                String playerId = resultSet.getString("player_id");
                int keyCount = resultSet.getInt("key_count");
                leaderboard.append("§7").append(rank++).append(". §b").append(playerId).append(": §a").append(keyCount).append(" chiavi\n");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Errore durante il recupero della leaderboard: " + e.getMessage());
            e.printStackTrace();
        }

        return leaderboard.toString();
    }
}