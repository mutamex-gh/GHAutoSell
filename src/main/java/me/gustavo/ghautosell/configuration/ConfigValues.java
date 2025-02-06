package me.gustavo.ghautosell.configuration;

import me.gustavo.ghautosell.GHAutoSell;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class ConfigValues {

    public static Map<Material, Double> loadAllowedToBreak() {
        Map<Material, Double> itemPrices = new HashMap<>();

        if (GHAutoSell.getInstance().getConfig().contains("config.configuration.allowed-itens")) {
            for (String materialName : GHAutoSell.getInstance().getConfig().getConfigurationSection("config.configuration.allowed-itens").getKeys(false)) {
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    double price = GHAutoSell.getInstance().getConfig().getDouble("config.configuration.allowed-itens." + materialName);
                    itemPrices.put(material, price);
                }
            }
        }
        return itemPrices;
    }
}
