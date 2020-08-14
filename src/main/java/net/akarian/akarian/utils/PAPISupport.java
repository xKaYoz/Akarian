package net.akarian.akarian.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.akarian.akarian.Akarian;
import net.akarian.akarian.users.User;
import net.akarian.akarian.users.UserManager;
import org.bukkit.entity.Player;

public class PAPISupport extends PlaceholderExpansion {

    Akarian plugin = Akarian.getInstance();

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "ak";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        User u = UserManager.getUser(player.getUniqueId());
        assert u != null;

        // %ak_main_prf%
        if (identifier.equals("main_prf")) {
            if (u.getMainRank() == null) {
                return "";
            }
            return u.getMainRank().getPrefix();
        }

        // %ak_prison_prf%
        if (identifier.equals("prison_prf")) {
            if (u.getPrisonRank() == null) {
                return "";
            }
            return u.getPrisonRank().getPrefix();
        }

        // %ak_donator_prf%
        if (identifier.equals("donator_prf")) {
            if (u.getDonatorRank() == null) {
                return "";
            }
            return u.getDonatorRank().getPrefix();
        }


        // We return null if an invalid placeholder was provided
        return null;

    }
}
