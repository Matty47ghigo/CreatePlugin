package com.example.createPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand implements CommandExecutor {
    private final CreatePlugin plugin;

    public CrateCommand(CreatePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cQuesto comando può essere usato solo da un giocatore.");
            return true;
        }

        Player player = (Player) sender;

        switch (command.getName().toLowerCase()) {
            case "createcrate":
                return createCrate(sender, args);
            case "open":
                return openCrate(sender, args);
            case "givekey":
                return giveKey(sender, args);
            case "leaderboard":
                return showLeaderboard(sender, args);
            case "reloadcrates":
                return reloadCrates(sender);
            case "setcrate":
                return setCrate(sender, args);
            default:
                sender.sendMessage("§cComando non riconosciuto.");
                return true;
        }
    }

    private boolean createCrate(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cratesystem.create")) {
            sender.sendMessage("§cNon hai il permesso di usare questo comando.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /createcrate <nome_crate>");
            return true;
        }

        String crateName = args[0];
        if (plugin.getCrateManager().getCrate(crateName) != null) {
            sender.sendMessage("§cUna crate con questo nome esiste già.");
            return true;
        }

        plugin.getCrateManager().createCrate(crateName);
        sender.sendMessage("§aCrate '" + crateName + "' creata con successo.");
        return true;
    }

    private boolean openCrate(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cratesystem.open")) {
            sender.sendMessage("§cNon hai il permesso di usare questo comando.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /open <nome_crate>");
            return true;
        }

        Player player = (Player) sender;
        String crateName = args[0];
        Crate crate = plugin.getCrateManager().getCrate(crateName);

        if (crate == null) {
            sender.sendMessage("§cLa crate '" + crateName + "' non esiste.");
            return true;
        }

        plugin.getCrateManager().openCrate(player, crate);
        return true;
    }

    private boolean giveKey(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cratesystem.givekey")) {
            sender.sendMessage("§cNon hai il permesso di usare questo comando.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUso: /givekey <giocatore> <nome_crate>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage("§cIl giocatore specificato non è online.");
            return true;
        }

        String crateName = args[1];
        Crate crate = plugin.getCrateManager().getCrate(crateName);
        if (crate == null) {
            sender.sendMessage("§cLa crate '" + crateName + "' non esiste.");
            return true;
        }

        plugin.getSQLiteManager().addKey(target.getUniqueId(), crateName, 1);
        sender.sendMessage("§aHai dato una chiave per la crate '" + crateName + "' a " + target.getName() + ".");
        target.sendMessage("§aHai ricevuto una chiave per la crate '" + crateName + "'.");
        return true;
    }

    private boolean showLeaderboard(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cratesystem.leaderboard")) {
            sender.sendMessage("§cNon hai il permesso di usare questo comando.");
            return true;
        }

        // Logica per mostrare la classifica delle crate
        sender.sendMessage("§aClassifica delle crate:");
        // Esempio di output (da sostituire con dati reali dal database)
        sender.sendMessage("§71. Giocatore1 - 50 aperture");
        sender.sendMessage("§72. Giocatore2 - 45 aperture");
        return true;
    }

    private boolean reloadCrates(CommandSender sender) {
        if (!sender.hasPermission("cratesystem.reload")) {
            sender.sendMessage("§cNon hai il permesso di usare questo comando.");
            return true;
        }

        plugin.getCrateManager().loadCrates();
        sender.sendMessage("§aLe crate sono state ricaricate con successo.");
        return true;
    }

    private boolean setCrate(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cratesystem.setcrate")) {
            sender.sendMessage("§cNon hai il permesso di usare questo comando.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /setcrate <nome_crate>");
            return true;
        }

        Player player = (Player) sender;
        String crateName = args[0];

        Crate crate = plugin.getCrateManager().getCrate(crateName);
        if (crate == null) {
            sender.sendMessage("§cLa crate '" + crateName + "' non esiste.");
            return true;
        }

        plugin.getCrateManager().enableCrateSelection(player, crate);
        sender.sendMessage("§aClicca con il tasto sinistro su uno Shulker Box per impostare la crate '" + crateName + "'.");
        return true;
    }
}