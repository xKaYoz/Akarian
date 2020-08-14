package net.akarian.akarian.ranks.commands.subcommands;

import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.CommandSender;

public class PermissionSubCommand extends AkarianCommand {

    public PermissionSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 4 && args[1].equalsIgnoreCase("add")) {
            Rank rank = RankManager.getRank(args[2]);
            String permission = args[3];

            if (rank == null) {
                Chat.sendMessage(sender, "That rank does not exist.");
                return;
            }

            if (rank.getPermissions().contains(permission)) {
                Chat.sendMessage(sender, "That rank already has that permission.");
                return;
            }

            rank.addPermission(permission);

            Chat.sendMessage(sender, "&7You have added the permission &e" + permission + " &7to the rank &e" + rank.getName() + "&7.");
            Chat.log("&e" + sender.getName() + " &7has added the permission &e" + permission + " &7to the rank &e" + rank.getName() + "&7.", true);

        } else if (args.length == 4 && args[1].equalsIgnoreCase("remove")) {
            Rank rank = RankManager.getRank(args[2]);
            String permission = args[3];

            if (rank == null) {
                Chat.sendMessage(sender, "That rank does not exist.");
                return;
            }

            if (!rank.getPermissions().contains(permission)) {
                Chat.sendMessage(sender, "That rank does not have that permission.");
                return;
            }

            rank.removePermission(permission);

            Chat.sendMessage(sender, "&7You have removed the permission &e" + permission + " &7to the rank &e" + rank.getName() + "&7.");
            Chat.log("&e" + sender.getName() + " &7has removed the permission &e" + permission + " &7to the rank &e" + rank.getName() + "&7.", true);

        }

    }
}
