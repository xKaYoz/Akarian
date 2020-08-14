package net.akarian.akarian.users.commands;

import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UserCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        long start = System.currentTimeMillis();

        if (args.length == 0) {
            UserCommandManager.getInstance().find("help").execute(sender, args);
            log(start);
            return false;
        }

        if (args.length == 1) {
            UserCommandManager.getInstance().find("info").execute(sender, args);
            log(start);
            return false;
        }

        AkarianCommand subCommand = UserCommandManager.getInstance().find(args[1]);

        if (subCommand == null) {

            for (String s : UserCommandManager.getInstance().getCommands().keySet()) {
                AkarianCommand sc = UserCommandManager.getInstance().getCommands().get(s);

                for (String aliases : UserCommandManager.getInstance().getCommands().get(s).getAliases()) {
                    if (aliases.equalsIgnoreCase(args[1])) {
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

            Chat.sendMessage(sender, "&cInvalid Command. Use /user help for more info.");
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
