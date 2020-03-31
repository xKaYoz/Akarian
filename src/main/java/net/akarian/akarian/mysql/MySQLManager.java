package net.akarian.akarian.mysql;

import lombok.Getter;
import lombok.Setter;
import net.akarian.akarian.Akarian;
import net.akarian.akarian.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLManager {

    @Setter
    @Getter
    private Connection connection;
    @Getter
    private String host, database, username, password, usersTable, ranksTable;
    private int port;

    public void setup() {
        FileConfiguration config = Akarian.getInstance().getConfig();

        host = config.getString("MYSQL.host");
        database = config.getString("MYSQL.database");
        username = config.getString("MYSQL.username");
        password = config.getString("MYSQL.password");
        usersTable = "akarian_" + config.getString("MYSQL.usersTable");
        ranksTable = "akarian_" + config.getString("MYSQL.ranksTable");
        port = config.getInt("MYSQL.port");

        CommandSender sender = Bukkit.getConsoleSender();

        Chat.sendRawMessage(sender, "------------ &c&lAkarian Core MySQL Manager &f------------");
        Chat.sendRawMessage(sender, "");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Chat.sendRawMessage(sender, "&aConnecting to the MySQL database...");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false", this.username, this.password));
            Chat.sendRawMessage(sender, "");
            Chat.sendRawMessage(sender, "&aAkarian has successfully established a connection to the MySQL database.");

            Statement s = connection.createStatement();

            Chat.sendRawMessage(sender, "");
            Chat.sendRawMessage(sender, "&aAkarian will now check the tables...");
            Chat.sendRawMessage(sender, "");

            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + usersTable + " (UUID varchar(255) NOT NULL PRIMARY KEY, MAIN_RANK varchar(255), PRISON_RANK varchar(255), DONATOR_RANK varchar(255), TOKENS BIGINT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + ranksTable + " (UUID varchar(255) NOT NULL PRIMARY KEY, NAME varchar(255), PREFIX varchar(255), PARENT varchar(255))");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + ranksTable + "_permissions (UUID varchar(255) NOT NULL PRIMARY KEY, PERMISSION varchar(255))");

            Chat.sendRawMessage(sender, "&aAkarian has updated all tables. The plugin will now enable.");
            Chat.sendRawMessage(sender, "");
            Chat.sendRawMessage(sender, "&8&m&l---------------------------------------------");

            startConnectionTimer();


        } catch (Exception e) {
            e.printStackTrace();
            Chat.sendRawMessage(sender, "&c&lAn error has occurred while connecting to the database. Please see stacktrace above.");
            Chat.log("&c&lAkarian has encountered an error connecting to the MySQL database. Please check console.", true);
        }
    }

    public boolean shutdown() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void startConnectionTimer() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Akarian.getInstance(), this::reconnect, 0, 20 * 60 * 60);

    }

    public void reconnect() {
        try {
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false", this.username, this.password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
