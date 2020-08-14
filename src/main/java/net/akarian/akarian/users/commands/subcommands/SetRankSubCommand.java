package net.akarian.akarian.users.commands.subcommands;

import net.akarian.akarian.Akarian;
import net.akarian.akarian.mysql.MySQLManager;
import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.users.User;
import net.akarian.akarian.users.UserManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetRankSubCommand extends AkarianCommand {

    public SetRankSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // /user <player> setrank <ladder> <rank>

        if (args.length == 4) {

            Rank rank = RankManager.getRank(args[3]);
            Player player = Bukkit.getPlayer(args[0]);

            if (args[2].equalsIgnoreCase("main")) {


                if (rank == null) {
                    Chat.sendMessage(sender, "That rank does not exist.");
                    return;
                }

                if (!rank.getLadder().equalsIgnoreCase(args[2])) {
                    Chat.sendMessage(sender, "That rank is not in the specified ladder, it's in the " + rank.getLadder() + " ladder.");
                    return;
                }

                if (player != null) {
                    User user = UserManager.getUser(player.getUniqueId());

                    user.setMainRank(rank);
                }

                MySQLManager sql = Akarian.getInstance().getMySQL();

                try {
                    PreparedStatement updateRank = sql.getConnection().prepareStatement("UPDATE " + sql.getUsersTable() + " SET MAIN_RANK=? WHERE UUID=?");

                    updateRank.setString(1, rank.getUUID().toString());
                    updateRank.setString(2, Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());

                    updateRank.executeUpdate();
                    updateRank.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else if (args[2].equalsIgnoreCase("prison")) {

                if (rank == null) {
                    Chat.sendMessage(sender, "That rank does not exist.");
                    return;
                }

                if (!rank.getLadder().equalsIgnoreCase(args[2])) {
                    Chat.sendMessage(sender, "That rank is not in the specified ladder, it's in the " + rank.getLadder() + " ladder.");
                    return;
                }

                if (player != null) {
                    User user = UserManager.getUser(player.getUniqueId());

                    user.setPrisonRank(rank);
                }

                MySQLManager sql = Akarian.getInstance().getMySQL();

                try {
                    PreparedStatement updateRank = sql.getConnection().prepareStatement("UPDATE " + sql.getUsersTable() + " SET PRISON_RANK=? WHERE UUID=?");

                    updateRank.setString(1, rank.getUUID().toString());
                    updateRank.setString(2, Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());

                    updateRank.executeUpdate();
                    updateRank.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else if (args[2].equalsIgnoreCase("donator")) {

                if (rank == null) {
                    Chat.sendMessage(sender, "That rank does not exist.");
                    return;
                }

                if (!rank.getLadder().equalsIgnoreCase(args[2])) {
                    Chat.sendMessage(sender, "That rank is not in the specified ladder, it's in the " + rank.getLadder() + " ladder.");
                    return;
                }

                if (player != null) {
                    User user = UserManager.getUser(player.getUniqueId());

                    user.setDonatorRank(rank);
                }

                MySQLManager sql = Akarian.getInstance().getMySQL();

                try {
                    PreparedStatement updateRank = sql.getConnection().prepareStatement("UPDATE " + sql.getUsersTable() + " SET DONATOR_RANK=? WHERE UUID=?");

                    updateRank.setString(1, rank.getUUID().toString());
                    updateRank.setString(2, Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());

                    updateRank.executeUpdate();
                    updateRank.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            Chat.sendMessage(sender, "You have set &e" + Bukkit.getOfflinePlayer(args[0]).getName() + "'s &7rank to &e" + rank.getName() + " &7in the &e" + rank.getLadder() + "&7 ladder.");
            Chat.log("&e" + sender.getName() + "&7 has set &e" + Bukkit.getOfflinePlayer(args[0]).getName() + "'s &7rank to &e" + rank.getName() + " &7in the &e" + rank.getLadder() + "&7 ladder.", true);
        } else {
            Chat.sendMessage(sender, "&cIncorrect Usage! /user <player> setrank <ladder> <rank>");
        }
    }
}
