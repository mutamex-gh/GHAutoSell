package me.gustavo.ghautosell.configuration.registry;

import me.gustavo.ghautosell.configuration.ConfigValues;

public class ConfigRegistry {

    public static void register() {
        ConfigValues.loadAllowedToBreak();
    }
}
