package me.gustavo.ghautosell.listener;

import lombok.val;
import me.gustavo.ghautosell.GHAutoSell;
import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.configuration.ConfigValues;
import me.gustavo.ghautosell.hook.EconomyHook;
import me.gustavo.ghautosell.utils.ActionBarUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.SplittableRandom;

public class BlockBreakListener implements Listener {

    public static SplittableRandom rand = new SplittableRandom();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        val isEnable = ConfigMessages.get(ConfigMessages::enableAutoSell);
        val multiplier = ConfigMessages.get(ConfigMessages::multiplier);
        val worlds = ConfigMessages.get(ConfigMessages::allowedWorlds);
        Map<Material, Double> allowedToBreak = ConfigValues.loadAllowedToBreak();

        if(!allowedToBreak.containsKey(block.getType())) return;
        if(!worlds.contains(block.getWorld().getName())) return;

        if (isEnable) {
            DecimalFormat df = new DecimalFormat("#,###,###,##0.##");
            double price = allowedToBreak.get(block.getType());

            if (player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                price = rand.nextInt(player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) * price;
            }

            double multipliedPrice = price * multiplier;
            EconomyHook.getEconomy().depositPlayer(player, multipliedPrice);

            ActionBarUtils.sendActionBar(
                    player,
                    ConfigMessages.get(ConfigMessages::blockBreakMessage)
                            .replace("{block}", block.getType().toString())
                            .replace("{coins}", df.format(multipliedPrice))
            );

            block.setType(Material.AIR);
        }
    }
}