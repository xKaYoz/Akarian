package net.akarian.akarian.users;

import lombok.Getter;

import java.util.ArrayList;
import java.util.UUID;

public class UserManager {

    @Getter
    private static ArrayList<User> users = new ArrayList<>();

    public static User getUser(UUID uuid) {
        for (User u : users) {
            if (u.getUuid().toString().equals(uuid.toString())) return u;
        }
        return null;
    }

    public static void registerUser(User u) {
        if (!users.contains(u)) {
            users.add(u);
        }
    }

    public static void unRegisterUser(User u) {
        users.remove(u);
    }

}
