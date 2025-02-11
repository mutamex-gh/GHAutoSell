package me.gustavo.ghautosell.configuration.registry;

import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import me.gustavo.ghautosell.GHAutoSell;
import me.gustavo.ghautosell.configuration.ConfigMessages;
import me.gustavo.ghautosell.configuration.ConfigValues;

public class ConfigRegistry {

    public static void register() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(GHAutoSell.getInstance());

        ConfigValues.loadAllowedToBreak();

        configurationInjector.saveDefaultConfiguration(
                GHAutoSell.getInstance(),
                "config.yml"
        );

        configurationInjector.injectConfiguration(
                ConfigMessages.instance()
        );
    }
}
