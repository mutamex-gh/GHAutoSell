package me.gustavo.ghautosell;

import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import lombok.val;
import me.gustavo.ghautosell.commands.Commands;
import me.gustavo.ghautosell.configuration.registry.ConfigRegistry;
import me.gustavo.ghautosell.database.connection.DatabaseConnection;
import me.gustavo.ghautosell.hook.EconomyHook;
import me.gustavo.ghautosell.inventories.SectionConstructor;
import me.gustavo.ghautosell.listener.BlockBreakListener;
import me.gustavo.ghautosell.listener.InventoryListener;
import me.gustavo.ghautosell.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class GHAutoSell extends JavaPlugin {

    private DatabaseConnection databaseConnection;

    @Override
    public void onEnable() {
        try{
            val initTimer = Stopwatch.createStarted();

            saveDefaultConfig();
            new SectionConstructor();

            ConfigRegistry.register();
            EconomyHook.register();

            openDatabaseConnection();

            loadListeners();
            loadCommand();


            initTimer.stop();
            getLogger().log(Level.INFO, "Plugin inicializado com sucesso! ({0})", initTimer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            databaseConnection.closeConnection();
            getLogger().info("Conexão com banco de dados desativado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openDatabaseConnection() {
        databaseConnection = new DatabaseConnection(this);
        databaseConnection.openConnection();
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(databaseConnection), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }

    public void loadCommand() {
        getCommand("autosell").setExecutor(new Commands());
    }

    public static GHAutoSell getInstance() {
        return getPlugin(GHAutoSell.class);
    }
}
