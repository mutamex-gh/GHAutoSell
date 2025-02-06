package me.gustavo.ghautosell.hook;

import me.gustavo.ghautosell.GHAutoSell;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHook {

    public static Economy econ = null;

    public static void register() {
        setupEconomy();
    }

    private static boolean setupEconomy() {
        if (GHAutoSell.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = GHAutoSell.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
