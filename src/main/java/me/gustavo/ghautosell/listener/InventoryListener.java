package me.gustavo.ghautosell.listener;

import lombok.val;
import me.gustavo.ghautosell.inventories.AutoSellInventory;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        val item = event.getCurrentItem();

        if(event.getInventory().getTitle().equalsIgnoreCase("Auto-sell inventario")) {
            if(item == null || item.getType() == Material.AIR) return;

            event.setCancelled(true);
        }
    }
}
