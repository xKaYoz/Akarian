package net.akarian.akarian.ranks;

import lombok.Getter;
import net.akarian.akarian.users.User;
import net.akarian.akarian.users.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankManager {

    @Getter
    private static ArrayList<Rank> ranks = new ArrayList<>();

    public static Rank getRank(UUID uuid) {
        for (Rank r : ranks) {
            if (r.getUUID().toString().equals(uuid.toString())) return r;
        }
        return null;
    }

    public static Rank getRank(String rank) {
        for (Rank r : ranks) {
            if (r.getName().equalsIgnoreCase(rank)) return r;
        }
        return null;
    }

    public static void registerRank(Rank r) {
        if (!ranks.contains(r)) {
            ranks.add(r);
        }
    }

    public static void unRegisterRank(Rank r) {
        ranks.remove(r);
    }

    public static List<User> getUsersInRank(Rank rank) {
        List<User> users = new ArrayList<>();
        for (User u : UserManager.getUsers()) {
            if (u.getMainRank() != null && u.getMainRank() == rank) {
                users.add(u);
            } else if (u.getPrisonRank() != null && u.getPrisonRank() == rank) {
                users.add(u);
            } else if (u.getDonatorRank() != null && u.getDonatorRank() == rank) {
                users.add(u);
            }
        }
        return users;
    }
}
