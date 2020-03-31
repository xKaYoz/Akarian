package net.akarian.akarian.ranks.commands.subcommands;

import net.akarian.akarian.Akarian;
import net.akarian.akarian.mysql.MySQLManager;
import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.users.User;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteSubCommand extends AkarianCommand {

    public DeleteSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String name = args[1];
        Rank rank = RankManager.getRank(name);

        if (rank == null) {
            Chat.sendMessage(sender, "That rank does not exist.");
            return;
        }

        if (name.equalsIgnoreCase("member")) {
            Chat.sendMessage(sender, "You cannot delete the Member rank.");
            return;
        }

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            PreparedStatement deleteRank = sql.getConnection().prepareStatement("DELETE FROM " + sql.getRanksTable() + " WHERE UUID=?");

            deleteRank.setString(1, rank.getUUID().toString());

            deleteRank.executeUpdate();
            deleteRank.close();

            PreparedStatement deletePermissions = sql.getConnection().prepareStatement("DELETE FROM " + sql.getRanksTable() + "_permissions WHERE UUID=?");

            deletePermissions.setString(1, rank.getUUID().toString());

            deletePermissions.executeUpdate();
            deletePermissions.close();

            RankManager.unRegisterRank(rank);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<User> users = RankManager.getUsersInRank(rank);

        for (User u : users) {
            Chat.sendMessage(sender, "Your rank has been deleted. You have been set to the Member rank.");
        }

        Chat.sendMessage(sender, "You have deleted the rank &e" + rank.getName() + "&7. Had &e" + users.size() + " user" + (users.size() == 1 ? "" : "s") + "&7.");

        Chat.log(sender.getName() + " has deleted the rank " + name + " Had " + users.size() + " user" + (users.size() == 1 ? "" : "s") + ".", true);


    }
}
