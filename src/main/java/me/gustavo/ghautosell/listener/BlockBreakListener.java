package me.gustavo.ghautosell.listener;

import lombok.val;
import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.configuration.ConfigValues;
import me.gustavo.ghautosell.database.connection.DatabaseConnection;
import me.gustavo.ghautosell.hook.EconomyHook;
import me.gustavo.ghautosell.inventories.SectionConstructor;
import me.gustavo.ghautosell.utils.ActionBarUtils;
import me.gustavo.ghautosell.utils.FormatNumbers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.Map;
import java.util.SplittableRandom;

public class BlockBreakListener implements Listener {

    public static SplittableRandom rand = new SplittableRandom();

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws SQLException {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        val isEnable = ConfigMessages.get(ConfigMessages::enableAutoSell);
        val multiplier = ConfigMessages.get(ConfigMessages::multiplier);
        val worlds = ConfigMessages.get(ConfigMessages::allowedWorlds);
        Map<Material, Double> allowedToBreak = ConfigValues.loadAllowedToBreak();

        if(!allowedToBreak.containsKey(block.getType())) return;
        if(!worlds.contains(block.getWorld().getName())) return;

        if (isEnable) {
            double price = allowedToBreak.get(block.getType());
            double alternativePrice = allowedToBreak.get(block.getType());

            if (player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                price = rand.nextInt(player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) * price;

                if(price == 0) {
                    price = alternativePrice;
                }
            }


            double multipliedPrice = price * multiplier;
            EconomyHook.getEconomy().depositPlayer(player, multipliedPrice);

            DatabaseConnection.updatePlayerBlocks(player.getUniqueId().toString(), 1);
            SectionConstructor.checkAndGiveReward(player);

            ActionBarUtils.sendActionBar(
                    player,
                    ConfigMessages.get(ConfigMessages::blockBreakMessage)
                            .replace("{material}", block.getType().toString())
                            .replace("{coins}", FormatNumbers.format(multipliedPrice))
                            .replace("{fortune}", String.valueOf(player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)))
                            .replace("{amountbreak}", FormatNumbers.format(DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString())))
            );

            block.setType(Material.AIR);
        }
    }
}