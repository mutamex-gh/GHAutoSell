package me.gustavo.ghautosell.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionBarUtils {

    public static void sendPacket(Player player, Object packet) {

        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Modificado para funcionar na versão spigot-1.8.8
     */
    public static void sendActionBar(Player player, String message) {
        try {
            Class<?> chatComponent = getNMSClass("IChatBaseComponent");
            Class<?> chatSerializer = getNMSClass("ChatComponentText");
            Class<?> packetActionbar = getNMSClass("PacketPlayOutChat");

            Object actionbar = chatSerializer.getConstructor(String.class).newInstance(message);

            Constructor<?> ConstructorActionbar = packetActionbar.getConstructor(chatComponent, byte.class);
            Object packet = ConstructorActionbar.newInstance(actionbar, (byte) 2);

            sendPacket(player, packet);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
