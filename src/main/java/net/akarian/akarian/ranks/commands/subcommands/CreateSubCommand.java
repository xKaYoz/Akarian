package net.akarian.akarian.ranks.commands.subcommands;

import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class CreateSubCommand extends AkarianCommand {

    public CreateSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // /rank c <name> <ladder> [prefix]

        if (args.length >= 3) {

            String name = args[1];
            String ladder = args[2];
            String prefix = "";

            if (args.length >= 4) {
                StringBuilder message = new StringBuilder();

                for (int i = 3; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                prefix = message.toString().trim();

            }

            if (RankManager.getRank(name) != null) {
                Chat.sendMessage(sender, "A rank with that name already exists.");
                return;
            }

            if (!(ladder.equalsIgnoreCase("main") || ladder.equalsIgnoreCase("prison") || ladder.equalsIgnoreCase("donator"))) {
                Chat.sendMessage(sender, "Invalid ladder. Use Main, Prison, or Donator");
                return;
            }

            Rank rank = new Rank(UUID.randomUUID(), name, ladder, prefix);

            rank.saveRank();

            Chat.sendMessage(sender, "You have created the rank &e" + name + "&7 in the &e" + rank.getLadder() + "&7 ladder with a prefix of \"&e" + (prefix.equals("") ? "NONE" : prefix) + "&7\".");

            Chat.log(sender.getName() + " has created the rank &e" + name + "&7 in the &e" + rank.getLadder() + "&7 ladder with a prefix of \"&e" + (prefix.equals("") ? "NONE" : prefix) + "&7\".", true);

        } else {
            Chat.sendMessage(sender, "&cIncorrect Usage! /rank create <name> <ladder> [prefix]");
        }
    }
}
