package me.gustavo.ghautosell.database.connection;

import me.gustavo.ghautosell.GHAutoSell;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

public class DatabaseConnection {

    private final GHAutoSell plugin;
    private static Connection connection;

    public void openConnection() {
        String DB_TYPE = plugin.getConfig().getString("database.type");

        if(DB_TYPE.equalsIgnoreCase("mysql")) {

            String ADDRESS = plugin.getConfig().getString("database.mysql.address");
            String USERNAME = plugin.getConfig().getString("database.mysql.username");
            String PASSWORD = plugin.getConfig().getString("database.mysql.password");
            String DATABASE = plugin.getConfig().getString("database.mysql.database");

            String URL = "jdbc:mysql://" + ADDRESS + "/" + DATABASE + "?characterEncoding=UTF-8&useSSL=false";

            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                createTable();
                plugin.getLogger().info("Conexão com o banco de dados MySQL efetuada com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
                plugin.getLogger().info("Não foi possivel conectar com o MySQL");
            }

        }else if(DB_TYPE.equalsIgnoreCase("sqlite")) {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            File database = new File(plugin.getDataFolder(), "database.db");

            String URL = "jdbc:sqlite:" + database.getAbsolutePath();

            try {
                connection = DriverManager.getConnection(URL);
                createTable();
                plugin.getLogger().info("Conexão com o banco de dados SQLite efetuada com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
                plugin.getLogger().info("Não foi possivel conectar com o SQLite");
            }
        }
    }

    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS players(" +
                            "uuid CHAR(36) PRIMARY KEY," +
                            "username TEXT NOT NULL," +
                            "blocks INTEGER NOT NULL DEFAULT 0)"
            );
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private boolean playerExists(String uuid) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT 1 FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void createPlayerOnDB(Player player) throws SQLException {
        if(playerExists(player.getUniqueId().toString())) {
            return;
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO players (uuid, username, blocks) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setInt(3, 0);
            preparedStatement.executeUpdate();
        }
    }

    public static void updatePlayerBlocks(String uuid, Integer quantity) throws SQLException {

        int currentBlocks = getPlayerBlocks(uuid);
        int newBlockCount = currentBlocks + quantity;

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE players SET blocks = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, newBlockCount);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        }
    }

    public static int getPlayerBlocks(String uuid) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT blocks FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("blocks");
                } else {
                    return 0;
                }
            }
        }
    }

    public DatabaseConnection(GHAutoSell plugin) {
        this.plugin = plugin;
    }

}
