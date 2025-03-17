package com.example.createPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CreatePlugin extends JavaPlugin {
    private SQLiteManager sqliteManager;
    private CrateManager crateManager;
    private AnimationManager animationManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Inizializza i manager
        sqliteManager = new SQLiteManager(this);
        crateManager = new CrateManager(this);
        animationManager = new AnimationManager(this);

        // Registra i comandi
        getCommand("createcrate").setExecutor(new CrateCommand(this));
        getCommand("open").setExecutor(new CrateCommand(this));
        getCommand("givekey").setExecutor(new CrateCommand(this));
        getCommand("leaderboard").setExecutor(new CrateCommand(this));
        getCommand("reloadcrates").setExecutor(new CrateCommand(this));
        getCommand("setcrate").setExecutor(new CrateCommand(this));

        // Registra gli eventi
        getServer().getPluginManager().registerEvents(new CrateListener(this), this);
    }

    @Override
    public void onDisable() {
        if (sqliteManager != null) {
            sqliteManager.close();
        }
        crateManager.clearHolograms();
    }

    public SQLiteManager getSQLiteManager() {
        return sqliteManager;
    }

    public CrateManager getCrateManager() {
        return crateManager;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}