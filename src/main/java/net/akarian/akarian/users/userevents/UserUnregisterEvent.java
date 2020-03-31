package net.akarian.akarian.users.userevents;

import net.akarian.akarian.users.User;
import net.akarian.akarian.users.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserUnregisterEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        User u = UserManager.getUser(p.getUniqueId());

        u.saveUser();
        UserManager.unRegisterUser(u);

    }

}
