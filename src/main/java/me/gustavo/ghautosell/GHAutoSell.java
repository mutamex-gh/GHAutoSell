package me.gustavo.ghautosell;

import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import lombok.val;
import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.listener.BlockBreakListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GHAutoSell extends JavaPlugin {

    private static Economy econ = null;

    @Override
    public void onEnable() {
        val initTimer = Stopwatch.createStarted();

        setupEconomy();
        registerConfigInjector();

        loadAllowedToBreak();
        loadListeners();

        initTimer.stop();
        getLogger().log(Level.INFO, "Plugin inicializado com sucesso! ({0})", initTimer);
    }

    public static Map<Material, Double> loadAllowedToBreak() {
        Map<Material, Double> itemPrices = new HashMap<>();

        if (getInstance().getConfig().contains("config.configuration.allowed-itens")) {
            for (String materialName : getInstance().getConfig().getConfigurationSection("config.configuration.allowed-itens").getKeys(false)) {
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    double price = getInstance().getConfig().getDouble("config.configuration.allowed-itens." + materialName);
                    itemPrices.put(material, price);
                }
            }
        }
        return itemPrices;
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(
                new BlockBreakListener(),
                this
        );
    }

    public void registerConfigInjector() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(this);

        configurationInjector.saveDefaultConfiguration(
                this,
                "config.yml"
        );

        configurationInjector.injectConfiguration(
                ConfigMessages.instance()
        );
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static GHAutoSell getInstance() {
        return getPlugin(GHAutoSell.class);
    }
}
