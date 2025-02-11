package me.gustavo.ghautosell.inventories;

import lombok.val;
import me.gustavo.ghautosell.GHAutoSell;
import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.database.connection.DatabaseConnection;
import me.gustavo.ghautosell.manager.RewardsButton;
import me.gustavo.ghautosell.utils.ColorUtils;
import me.gustavo.ghautosell.utils.FormatNumbers;
import me.gustavo.ghautosell.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SectionConstructor {

    public static final Map<String, RewardsButton> rewardsButton = new LinkedHashMap<>();

    static {
        ConfigurationSection itemSection = GHAutoSell.getInstance().getConfig().getConfigurationSection("rewards");
        Inventory inventory = AutoSellInventory.getInventory();

        for (String key : itemSection.getKeys(false)) {
            try {
                Material material = Material.valueOf(itemSection.getString(key + ".material"));
                String name = itemSection.getString(key + ".name");
                List<String> lore = itemSection.getStringList(key + ".lore");
                int slot = itemSection.getInt(key + ".slot");
                int necessaryToBreak = itemSection.getInt(key + ".necessary");
                List<String> command = itemSection.getStringList(key + ".command");

                ItemBuilder item = new ItemBuilder(material)
                        .changeItemMeta(itemMeta -> {
                            itemMeta.setDisplayName(ColorUtils.colored(name));
                            itemMeta.setLore(ColorUtils.colored(lore));
                        })
                        .setNBTTag("rewardsnbt", key);

                rewardsButton.put(key, new RewardsButton(key, item.wrap(), lore, slot, necessaryToBreak, command));
            } catch (Exception e) {
                GHAutoSell.getInstance().getLogger().severe("Erro ao carregar item para chave: " + key + ". Erro: " + e.getMessage());
            }
        }

        rewardsButton.forEach((k, button) -> {
            if (button.getSlot() >= 0 && button.getSlot() < inventory.getSize()) {
                inventory.setItem(button.getSlot(), button.getItemStack());
                GHAutoSell.getInstance().getLogger().info("Item adicionado no slot: " + button.getSlot() + " (" + button.getItemStack().getType() + ")");
            } else {
                GHAutoSell.getInstance().getLogger().severe("Slot inválido para o botão " + k + ": " + button.getSlot());
            }
        });
    }

    public static void checkAndGiveReward(Player player) throws SQLException {
        int playerBlocks = Integer.parseInt(String.valueOf(DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString())));

        for (Map.Entry<String, RewardsButton> entry : rewardsButton.entrySet()) {
            RewardsButton button = entry.getValue();
            if (playerBlocks == button.getNecessaryToBreak()) {
                executeReward(player, button);
            } else if (playerBlocks > button.getNecessaryToBreak()) {
                updateLoreAfterReward(player);
            }
        }
    }

    private static void executeReward(Player player, RewardsButton reward) throws SQLException {
        for (String command : reward.getCommand()) {
            String formattedCommand = command.replace("{player}", player.getName());

            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    formattedCommand
            );
        }

        player.sendMessage(ColorUtils.colored(String.join("\n", ConfigMessages.get(ConfigMessages::rewardMessage)))
                .replace("{amount}", FormatNumbers.format(DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString()))));
    }

    public static void updateLore(Player player) throws SQLException {
        for (String key : rewardsButton.keySet()) {
            RewardsButton button = rewardsButton.get(key);
            List<String> updatedLore = new ArrayList<>();

            for (String line : button.getLore()) {
                String newLore = line
                        .replace("{current}", FormatNumbers.format(DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString())))
                        .replace("{necessary}", FormatNumbers.format(button.getNecessaryToBreak()))
                        .replace("{received}", "");


                updatedLore.add(newLore);
            }

            ItemStack item = button.getItemStack();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setLore(ColorUtils.colored(updatedLore));
                item.setItemMeta(meta);
            }
            Inventory inventory = AutoSellInventory.getInventory();
            inventory.setItem(button.getSlot(), item);
        }
    }

    public static void updateLoreAfterReward(Player player) throws SQLException {
        for (String key : rewardsButton.keySet()) {
            RewardsButton button = rewardsButton.get(key);
            List<String> updatedLore = new ArrayList<>();

            int necessary = button.getNecessaryToBreak();
            int value = 0;

            if (DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString()) > necessary) {
                int a = value + necessary;

                for (String line : button.getLore()) {
                    String newLore = line
                            .replace("{current}", FormatNumbers.format(a))
                            .replace("{necessary}", FormatNumbers.format(necessary))
                            .replace("{received}", ColorUtils.colored("&cVocê ja recebeu esta recompensa!"));

                    updatedLore.add(newLore);
                }
            }else {
                for (String line : button.getLore()) {
                    String newLore = line
                            .replace("{current}", FormatNumbers.format(DatabaseConnection.getPlayerBlocks(player.getUniqueId().toString())))
                            .replace("{necessary}", FormatNumbers.format(button.getNecessaryToBreak()))
                            .replace("{received}", "");

                    updatedLore.add(newLore);
                }
            }

            ItemStack item = button.getItemStack();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setLore(ColorUtils.colored(updatedLore));
                item.setItemMeta(meta);
            }
            Inventory inventory = AutoSellInventory.getInventory();
            inventory.setItem(button.getSlot(), item);
        }
    }

}
