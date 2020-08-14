package net.akarian.akarian.users.commands.subcommands;

import net.akarian.akarian.users.User;
import net.akarian.akarian.users.UserManager;
import net.akarian.akarian.utils.AkarianCommand;
import net.akarian.akarian.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class InfoSubCommand extends AkarianCommand {
    public InfoSubCommand(String name, String permission, String usage, String description, String... aliases) {
        super(name, permission, usage, description, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        User user;
        boolean tempUser = false;

        if (UserManager.getUser(uuid) == null) {
            user = new User().loadUser(uuid);
            tempUser = true;
        } else {
            user = UserManager.getUser(uuid);
        }

        Chat.sendRawMessage(sender, "&8&m&l----------------------------");
        Chat.sendRawMessage(sender, "&c&l" + Bukkit.getOfflinePlayer(uuid).getName() + "'s &f&lInformation");
        Chat.sendRawMessage(sender, "");
        Chat.sendRawMessage(sender, "&cUUID &8- &f" + uuid.toString());
        Chat.sendRawMessage(sender, "&cMain Rank &8- &r" + (user.getMainRank() == null ? "None" : user.getMainRank().getName()));
        Chat.sendRawMessage(sender, "&cPrison Rank &8- &r" + (user.getPrisonRank() == null ? "None" : user.getPrisonRank().getName()));
        Chat.sendRawMessage(sender, "&cDonator Rank &8- &f" + (user.getDonatorRank() == null ? "None" : user.getDonatorRank().getName()));
        Chat.sendRawMessage(sender, "&c# of Punishments &8- &fTODO");
        Chat.sendRawMessage(sender, "&cTokens &8- &f" + user.getTokens());
        Chat.sendRawMessage(sender, "&8&m&l----------------------------");

        if (tempUser) {
            UserManager.unRegisterUser(user);
        }

    }
}
