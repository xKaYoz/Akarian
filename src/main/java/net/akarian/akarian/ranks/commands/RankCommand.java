package net.akarian.akarian.ranks.commands;

import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        long start = System.currentTimeMillis();

        if (args.length == 0) {
            RankCommandManager.getInstance().find("help").execute(sender, args);
            log(start);
            return false;
        }

        AkarianCommand subCommand = RankCommandManager.getInstance().find(args[0]);

        if (subCommand == null) {

            for (String s : RankCommandManager.getInstance().getCommands().keySet()) {
                AkarianCommand sc = RankCommandManager.getInstance().getCommands().get(s);

                for (String aliases : RankCommandManager.getInstance().getCommands().get(s).getAliases()) {
                    if (aliases.equalsIgnoreCase(args[0])) {
                        if (sender.hasPermission(sc.getPermission())) {
                            sc.execute(sender, args);
                        } else {
                            Chat.noPermission(sender);
                        }
                        log(start);
                        return false;
                    }
                }
            }

            Chat.sendMessage(sender, "&cInvalid Command. Use /rank help for more info.");
            log(start);
            return false;
        }

        if (sender.hasPermission(subCommand.getPermission())) {
            subCommand.execute(sender, args);
        } else {
            Chat.noPermission(sender);

        }
        log(start);

        return false;
    }

    private void log(long time) {
        Chat.log("Executed in " + (System.currentTimeMillis() - time) + "ms.", true);
    }

}
