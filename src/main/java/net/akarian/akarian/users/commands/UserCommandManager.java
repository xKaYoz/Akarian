package net.akarian.akarian.users.commands;

import lombok.Getter;
import net.akarian.akarian.users.commands.subcommands.HelpSubCommand;
import net.akarian.akarian.users.commands.subcommands.InfoSubCommand;
import net.akarian.akarian.users.commands.subcommands.SetRankSubCommand;
import net.akarian.akarian.utils.AkarianCommand;

import java.util.HashMap;
import java.util.Map;

public class UserCommandManager {

    @Getter
    private static UserCommandManager instance = new UserCommandManager();
    @Getter
    private Map<String, AkarianCommand> commands = new HashMap<>();

    private UserCommandManager() {
        commands.put("help", new HelpSubCommand("help", "user.help", "/user help", "Displays useful information about the plugin.", "h"));
        commands.put("setrank", new SetRankSubCommand("setrank", "user.setrank", "/user &f<name> &csetrank &f<ladder> <rank>", "Set the rank of a player.", "sr"));
        commands.put("info", new InfoSubCommand("info", "user.info", "/user &f<name> &cinfo", "Shows the information about a player", "i  "));

    }

    public AkarianCommand find(String command) {
        return commands.get(command);
    }

}
