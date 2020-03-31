package net.akarian.akarian.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    private static String prefix = "&c&lAkarian";

    public static String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String formatMoney(Object obj) {

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        return decimalFormat.format(obj);

    }

    public static ArrayList<String> formatList(List<String> list) {

        ArrayList<String> formatted = new ArrayList<>();

        for (String str : list) {
            formatted.add(format(str));
        }

        return formatted;
    }

    public static void noPermission(CommandSender sender) {
        sender.sendMessage(format("&cYou do not have permission to execute this command!"));
    }

    public static void sendRawMessage(CommandSender p, String str) {
        if (!(p instanceof Player))
            System.out.println(format(str));
        else
            p.sendMessage(format(str));
    }

    public static void sendMessage(CommandSender p, String str) {
        p.sendMessage(format(prefix + " &8» &7" + str));
    }

    public static void broadcastRawMessage(String str) {
        Bukkit.broadcastMessage(format(str));
    }

    public static void broadcastMessage(String str) {
        Bukkit.broadcastMessage(format(prefix + " &8» &7" + str));
    }


    public static void log(String str, boolean players) {
        System.out.println(format("&c&lAkarian Debug &7» &e" + str));

        if (players) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.isOp()) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    p.sendMessage(Chat.format("&8[&e" + dtf.format(now) + "&8] [&c&lAkarian Debug&8] &e" + str));
                }
            }
        }
    }

}
