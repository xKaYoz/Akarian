package net.akarian.akarian.ranks.commands.subcommands;

import net.akarian.akarian.ranks.commands.RankCommandManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpSubCommand extends AkarianCommand {

    public HelpSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        int page = 1;

        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                Chat.sendMessage(sender, "&cThe second argument must be an integer.");
                return;
            }
        }

        List<AkarianCommand> commands = new ArrayList<>();

        for (AkarianCommand command : RankCommandManager.getInstance().getCommands().values()) {
            if (sender.hasPermission(command.getPermission())) commands.add(command);
        }

        if (commands.size() == 0) {
            Chat.sendRawMessage(sender, "&8&m----------------------------------------");
            Chat.sendRawMessage(sender, "&c&l  Akarian &f&lRank Help Menu");
            Chat.sendRawMessage(sender, "");
            Chat.sendRawMessage(sender, "&cYou do not have any Clans permissions.");
            Chat.sendRawMessage(sender, "&8&m----------------------------------------");
            return;
        }

        Chat.sendRawMessage(sender, commands.size() + " commands.");

        Chat.sendRawMessage(sender, "&8&m----------------------------------------");
        Chat.sendRawMessage(sender, "&c&l  Akarian &f&lRank Help Menu &7(" + (page) + "/" + (commands.size() % 10 == 0 ? commands.size() / 10 : (commands.size() / 10) + 1) + ")");
        Chat.sendRawMessage(sender, "");
        Chat.sendRawMessage(sender, "&f  <> &8- &cRequired Commands");
        Chat.sendRawMessage(sender, "&7  [] &8- &cOptional Commands.");
        Chat.sendRawMessage(sender, "");
        int from = 0;
        int to = 0;

        if (page == 1) {
            to = 10;
        } else if (page == 2) {
            from = 10;
            to = 20;
        }

        if (commands.size() >= 10) {
            for (int i = from; i < (Math.min(commands.size(), to)); i++) {
                AkarianCommand command = (AkarianCommand) commands.toArray()[i];

                if (command == null) break;

                Chat.sendRawMessage(sender, "  &c" + command.getUsage() + " &8- &7" + command.getDescription());

            }
        } else {
            for (AkarianCommand command : commands) {
                Chat.sendRawMessage(sender, "  &c" + command.getUsage() + " &8- &7" + command.getDescription());
            }
        }
        Chat.sendRawMessage(sender, "&8&m----------------------------------------");

    }
}
