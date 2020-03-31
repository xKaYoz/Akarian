package net.akarian.akarian.ranks.commands;

import lombok.Getter;
import net.akarian.akarian.ranks.commands.subcommands.CreateSubCommand;
import net.akarian.akarian.ranks.commands.subcommands.DeleteSubCommand;
import net.akarian.akarian.ranks.commands.subcommands.HelpSubCommand;
import net.akarian.akarian.utils.AkarianCommand;

import java.util.HashMap;
import java.util.Map;

public class RankCommandManager {

    @Getter
    private static RankCommandManager instance = new RankCommandManager();
    @Getter
    private Map<String, AkarianCommand> commands = new HashMap<>();

    private RankCommandManager() {
        commands.put("help", new HelpSubCommand("help", "rank.help", "/rank help", "Displays useful information about the plugin.", "h"));
        commands.put("create", new CreateSubCommand("create", "rank.create", "/rank create &f<name> &7[prefix]", "Create a rank with an optional prefix.", "c"));
        commands.put("delete", new DeleteSubCommand("delete", "rank.delete", "/rank delete &f<name>", "Delete a rank.", "d"));

    }

    public AkarianCommand find(String command) {
        return commands.get(command);
    }

}
