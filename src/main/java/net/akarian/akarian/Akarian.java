package net.akarian.akarian;

import lombok.Getter;
import net.akarian.akarian.mysql.MySQLManager;
import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.ranks.commands.RankCommand;
import net.akarian.akarian.ranks.commands.RankCommandManager;
import net.akarian.akarian.users.User;
import net.akarian.akarian.users.UserManager;
import net.akarian.akarian.users.commands.UserCommand;
import net.akarian.akarian.users.commands.UserCommandManager;
import net.akarian.akarian.users.userevents.UserRegisterEvent;
import net.akarian.akarian.users.userevents.UserUnregisterEvent;
import net.akarian.akarian.utils.Chat;
import net.akarian.akarian.utils.PAPISupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class Akarian extends JavaPlugin {

    @Getter
    private static Akarian instance;
    @Getter
    private MySQLManager mySQL;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        mySQL = new MySQLManager();

        mySQL.setup();

        loadRanks();
        loadUsers();

        registerEvents();
        registerCommands();

        new PAPISupport().register();

        Chat.log("Loaded " + (RankCommandManager.getInstance().getCommands().size() + UserCommandManager.getInstance().getCommands().size()) + " commands", true);

        Chat.log("Started up in " + (System.currentTimeMillis() - start) + "ms", true);

    }

    private void loadRanks() {

        long start = System.currentTimeMillis();
        int ranks = 0;

        try {
            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM " + mySQL.getRanksTable());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Rank.loadRank(UUID.fromString(rs.getString(1)));
                ranks++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (RankManager.getRank("Member") == null) {
            new Rank(UUID.randomUUID(), "Member", "MAIN", "&7&lMember").saveRank();
        }

        Chat.log("Loaded " + ranks + " ranks in " + (System.currentTimeMillis() - start) + "ms", true);

    }

    private void saveRanks() {
        long start = System.currentTimeMillis();
        int ranks = 0;

        for (Rank r : RankManager.getRanks()) {
            ranks++;
            r.saveRank();
        }

        Chat.log("Saved " + ranks + " ranks in " + (System.currentTimeMillis() - start) + "ms", true);
    }

    private void loadUsers() {

        long start = System.currentTimeMillis();
        int accounts = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            accounts++;
            new User().loadUser(p.getUniqueId());

        }

        Chat.log("Loaded " + accounts + " accounts in " + (System.currentTimeMillis() - start) + "ms", true);

    }

    private void saveUsers() {

        long start = System.currentTimeMillis();
        int accounts = 0;
        for (User u : UserManager.getUsers()) {
            accounts++;
            u.saveUser();
            u.unloadPermissions();
        }

        Chat.log("Saved " + accounts + " accounts in " + (System.currentTimeMillis() - start) + "ms", true);
    }

    @Override
    public void onDisable() {
        saveRanks();
        saveUsers();
        mySQL.shutdown();
        instance = null;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new UserRegisterEvent(), this);
        pm.registerEvents(new UserUnregisterEvent(), this);
    }

    private void registerCommands() {
        this.getCommand("rank").setExecutor(new RankCommand());
        this.getCommand("user").setExecutor(new UserCommand());
    }
}
