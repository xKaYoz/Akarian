package net.akarian.akarian.users.userevents;

import net.akarian.akarian.users.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserRegisterEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        User u = new User().loadUser(p.getUniqueId());

    }

}
