package me.gustavo.ghautosell.commands;

import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.inventories.AutoSellInventory;
import me.gustavo.ghautosell.inventories.InventoryItens;
import me.gustavo.ghautosell.inventories.SectionConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if(player.hasPermission("ghautosell.openinventory")) {
            try {
                SectionConstructor.updateLore(player);
                SectionConstructor.updateLoreAfterReward(player);
                InventoryItens.setProfile(player);

                player.openInventory(AutoSellInventory.getInventory());
            } catch (SQLException ignore) {}
        }else {
            player.sendMessage(ConfigMessages.get(ConfigMessages::noPermission));
            return true;
        }
        return false;
    }
}
