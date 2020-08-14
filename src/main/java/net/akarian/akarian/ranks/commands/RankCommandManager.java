package net.akarian.akarian.ranks.commands;

import lombok.Getter;
import net.akarian.akarian.ranks.commands.subcommands.*;
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
        commands.put("create", new CreateSubCommand("create", "rank.create", "/rank create &f<name> <ladder> &7[prefix]", "Create a rank with an optional prefix.", "c"));
        commands.put("delete", new DeleteSubCommand("delete", "rank.delete", "/rank delete &f<name>", "Delete a rank.", "d"));
        commands.put("permission", new PermissionSubCommand("permission", "rank.permission", "/rank permission &f<add/remove> <rank> <permission>", "Edit the permissions of the rank.", "permissions", "p"));
        commands.put("info", new InfoSubCommand("info", "rank.info", "/rank info &f<name>", "Information about a rank.", "i"));
        commands.put("prefix", new PrefixSubCommand("prefix", "rank.prefix", "/rank prefix &f<name> <prefix>", "Set the rank's prefix.", "pr"));

    }

    public AkarianCommand find(String command) {
        return commands.get(command);
    }

}
