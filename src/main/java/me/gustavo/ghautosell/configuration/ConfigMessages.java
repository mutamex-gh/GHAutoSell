package me.gustavo.ghautosell.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigSection("config")
@ConfigFile("config.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigMessages implements ConfigurationInjectable {

    @Getter private static final ConfigMessages instance = new ConfigMessages();

    //messages
    @ConfigField("messages.only-players") private String onlyPlayers;
    @ConfigField("messages.no-permission") private String noPermission;
    @ConfigField("messages.blockbreak-actionbar-message") private String blockBreakMessage;

    //configuration
    @ConfigField("configuration.enable") private boolean enableAutoSell;
    @ConfigField("configuration.multiplier") private double multiplier;

    //worlds
    @ConfigField("allow-worlds") private List<String> allowedWorlds;

    public static <T> T get(Function<ConfigMessages, T> function) {
        return function.apply(instance);
    }
}
