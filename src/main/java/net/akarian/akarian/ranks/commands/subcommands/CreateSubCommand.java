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

        if (args.length >= 2) {

            String name = args[1];
            String prefix = args.length == 3 ? args[2] : "";

            if (RankManager.getRank(name) != null) {
                Chat.sendMessage(sender, "A rank with that name already exists.");
                return;
            }

            Rank rank = new Rank(UUID.randomUUID(), name, prefix);

            rank.saveRank();

            Chat.sendMessage(sender, "You have created the rank &e" + name + " &7( " + (prefix.equals("") ? "NONE" : prefix) + " &7).");

            Chat.log(sender.getName() + " has created the rank " + name + " &7( " + (prefix.equals("") ? "NONE" : prefix) + " &7).", true);

        }
    }
}
