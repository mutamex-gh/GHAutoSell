package me.gustavo.ghautosell;

import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import lombok.val;
import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.configuration.registry.ConfigRegistry;
import me.gustavo.ghautosell.hook.EconomyHook;
import me.gustavo.ghautosell.listener.BlockBreakListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class GHAutoSell extends JavaPlugin {

    private static Economy econ = null;

    @Override
    public void onEnable() {
        val initTimer = Stopwatch.createStarted();

        EconomyHook.register();
        ConfigRegistry.register();

        registerConfigInjector();
        loadListeners();

        initTimer.stop();
        getLogger().log(Level.INFO, "Plugin inicializado com sucesso! ({0})", initTimer);
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

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(
                new BlockBreakListener(),
                this
        );
    }

    public static GHAutoSell getInstance() {
        return getPlugin(GHAutoSell.class);
    }
}
