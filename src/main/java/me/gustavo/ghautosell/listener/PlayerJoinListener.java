package me.gustavo.ghautosell.listener;

import me.gustavo.ghautosell.database.connection.DatabaseConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    private final DatabaseConnection databaseConnection;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        try{
            databaseConnection.createPlayerOnDB(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerJoinListener(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
