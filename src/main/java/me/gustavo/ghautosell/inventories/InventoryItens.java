package me.gustavo.ghautosell.inventories;

import me.gustavo.ghautosell.GHAutoSell;
import me.gustavo.ghautosell.database.connection.DatabaseConnection;
import me.gustavo.ghautosell.utils.ColorUtils;
import me.gustavo.ghautosell.utils.FormatNumbers;
import me.gustavo.ghautosell.utils.ItemBuilderGB;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class InventoryItens {

    public static DatabaseConnection databaseConnection;

    public static void setProfile(Player player) throws SQLException {
        databaseConnection = new DatabaseConnection(GHAutoSell.getInstance());

        ItemBuilderGB getProfile = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtils.colored("&aPerfil"))
                .lore(ColorUtils.colored(
                        "&7Veja suas estatisticas:",
                        " ",
                        "  &fSeus blocos: &e" + FormatNumbers.format(DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString()))
                ))
                .durability((short) 3)
                .skullOwner(player.getName());

        AutoSellInventory.getInventory().setItem(10, getProfile.build());

        getProfile.build();
    }
}
