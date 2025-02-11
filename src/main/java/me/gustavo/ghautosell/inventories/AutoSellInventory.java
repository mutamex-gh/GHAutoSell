package me.gustavo.ghautosell.inventories;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class AutoSellInventory {

    @Getter public static Inventory inventory;

    static {
        inventory = Bukkit.createInventory(
                null,
                27,
                "Auto-sell inventario");
    }

}
