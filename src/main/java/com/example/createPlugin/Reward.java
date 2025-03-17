package com.example.createPlugin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Reward {
    private final Material material;
    private final int amount;
    private final ItemStack itemStack;

    // Costruttore con Material e int (vecchio costruttore)
    public Reward(Material material, int amount) {
        this.material = material;
        this.amount = amount;

        // Crea un ItemStack basato sul materiale e sulla quantità
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aPremio: " + material.name());
            item.setItemMeta(meta);
        }
        this.itemStack = item;
    }

    // Nuovo costruttore con Material, int e ItemStack
    public Reward(Material material, int amount, ItemStack itemStack) {
        this.material = material;
        this.amount = amount;
        this.itemStack = itemStack;
    }

    // Metodo per ottenere il materiale del premio
    public Material getMaterial() {
        return material;
    }

    // Metodo per ottenere la quantità del premio
    public int getAmount() {
        return amount;
    }

    // Metodo per ottenere l'ItemStack del premio
    public ItemStack toItemStack() {
        return itemStack;
    }
}