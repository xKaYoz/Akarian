package net.akarian.akarian.ranks.commands.subcommands;

import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.CommandSender;

public class PrefixSubCommand extends AkarianCommand {

    public PrefixSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // /rank prefix <rank> <prefix>

        if (args.length == 3) {

            Rank rank = RankManager.getRank(args[1]);
            String prefix;
            StringBuilder message = new StringBuilder();

            for (int i = 2; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            prefix = message.toString().trim();

            if (rank == null) {
                Chat.sendMessage(sender, "That rank does not exist.");
                return;
            }

            rank.setPrefix(prefix);

            Chat.sendMessage(sender, "You have changed the rank &e" + rank.getName() + "'s&7 prefix to \"&r" + prefix + "&7\"&7.");

            Chat.log(sender.getName() + "&7 has changed the rank &e" + rank.getName() + "'s&7 prefix to \"&r" + prefix + "&7\"&7.", true);


        } else {
            Chat.sendMessage(sender, "&cIncorrect Usage! /rank prefix <rank> <prefix>");
        }


    }
}
