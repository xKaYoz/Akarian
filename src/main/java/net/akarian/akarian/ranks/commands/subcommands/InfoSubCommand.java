package net.akarian.akarian.ranks.commands.subcommands;

import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.CommandSender;

public class InfoSubCommand extends AkarianCommand {

    public InfoSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 2) {

            Rank rank = RankManager.getRank(args[1]);

            if (rank == null) {
                Chat.sendRawMessage(sender, "That rank does not exist.");
                return;
            }

            Chat.sendRawMessage(sender, "&8&m&l----------------------------");
            Chat.sendRawMessage(sender, "&c&l" + rank.getName() + " &f&lInformation");
            Chat.sendRawMessage(sender, "");
            Chat.sendRawMessage(sender, "&cUUID &8- &f" + rank.getUUID());
            Chat.sendRawMessage(sender, "&cPrefix &8- &r" + rank.getPrefix());
            Chat.sendRawMessage(sender, "&cMembers &8- &fTODO");
            Chat.sendRawMessage(sender, "&cOnline Members &8- &f" + RankManager.getUsersInRank(rank).size());
            Chat.sendRawMessage(sender, "&c# of Permissions &8- &f" + rank.getPermissions().size());
            Chat.sendRawMessage(sender, "&cPermissions &8- &f" + rank.getPermissions().toString());
            Chat.sendRawMessage(sender, "&8&m&l----------------------------");

        } else {
            Chat.sendMessage(sender, "&cIncorrect usage! /rank info <rank>");
            return;
        }

    }
}
