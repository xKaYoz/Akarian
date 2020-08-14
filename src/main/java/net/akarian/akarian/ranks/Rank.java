package net.akarian.akarian.ranks;

import lombok.Getter;
import lombok.Setter;
import net.akarian.akarian.Akarian;
import net.akarian.akarian.mysql.MySQLManager;
import net.akarian.akarian.users.User;
import org.bukkit.permissions.PermissionAttachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rank {

    @Getter
    @Setter
    private UUID UUID;
    @Getter
    @Setter
    private String prefix;
    @Getter
    @Setter
    private String ladder;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Rank parent;
    @Getter
    @Setter
    private List<String> permissions = new ArrayList<>();

    public Rank(UUID uuid, String name, String ladder, String prefix) {
        this.UUID = uuid;
        this.name = name;
        this.ladder = ladder.toUpperCase();
        this.prefix = prefix;

        RankManager.registerRank(this);
    }

    public static Rank loadRank(UUID uuid) {

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            PreparedStatement get = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getRanksTable() + " WHERE UUID=?");

            get.setString(1, uuid.toString());

            ResultSet rs = get.executeQuery();

            while (rs.next()) {

                Rank rank = new Rank(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4));

                PreparedStatement permissions = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getRanksTable() + "_permissions WHERE UUID=?");

                permissions.setString(1, uuid.toString());

                ResultSet ps = permissions.executeQuery();

                while (ps.next()) {
                    rank.getPermissions().add(ps.getString(2));
                }

                return rank;

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
        MySQLManager sql = Akarian.getInstance().getMySQL();

        for (User user : RankManager.getUsersInRank(this)) {

            PermissionAttachment attachment = user.getAttachment();
            attachment.unsetPermission(permission);

        }

        try {
            PreparedStatement delete = sql.getConnection().prepareStatement("DELETE FROM " + sql.getRanksTable() + "_permissions WHERE UUID=?");

            delete.setString(1, getUUID().toString());

            delete.executeUpdate();
            delete.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addPermission(String permission) {
        this.permissions.add(permission);

        for (User user : RankManager.getUsersInRank(this)) {

            PermissionAttachment attachment = user.getAttachment();
            attachment.setPermission(permission, true);

        }

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            PreparedStatement check = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getRanksTable() + "_permissions WHERE UUID=? AND PERMISSION=?");

            check.setString(1, getUUID().toString());
            check.setString(2, permission);

            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                PreparedStatement insert = sql.getConnection().prepareStatement("INSERT INTO " + sql.getRanksTable() + "_permissions (UUID,PERMISSION) VALUES (?,?)");

                insert.setString(1, getUUID().toString());
                insert.setString(2, permission);

                insert.executeUpdate();
                insert.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveRank() {

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            PreparedStatement get = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getRanksTable() + " WHERE UUID=?");

            get.setString(1, getUUID().toString());

            ResultSet rs = get.executeQuery();

            while (rs.next()) {
                PreparedStatement update = sql.getConnection().prepareStatement("UPDATE " + sql.getRanksTable() + " SET NAME=?, LADDER=?, PREFIX=?, PARENT=? WHERE UUID=?");

                update.setString(1, name);
                update.setString(2, ladder);
                update.setString(3, prefix);
                if (parent == null) {
                    update.setString(4, null);
                } else {
                    update.setString(4, parent.getUUID().toString());
                }
                update.setString(5, getUUID().toString());

                update.executeUpdate();
                update.close();

                savePermissions();

                return;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement insert = sql.getConnection().prepareStatement("INSERT INTO " + sql.getRanksTable() + " (UUID,NAME,LADDER,PREFIX,PARENT) VALUES (?,?,?,?,?)");

            insert.setString(1, getUUID().toString());
            insert.setString(2, name);
            insert.setString(3, ladder);
            insert.setString(4, prefix);
            if (parent == null) {
                insert.setString(5, null);
            } else {
                insert.setString(5, parent.getUUID().toString());
            }

            insert.executeUpdate();
            insert.close();

            savePermissions();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void savePermissions() {

        MySQLManager sql = Akarian.getInstance().getMySQL();

        for (String permission : permissions) {
            try {
                PreparedStatement check = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getRanksTable() + "_permissions WHERE UUID=? AND PERMISSION=?");

                check.setString(1, getUUID().toString());
                check.setString(2, permission);

                ResultSet rs = check.executeQuery();

                if (!rs.next()) {
                    PreparedStatement insert = sql.getConnection().prepareStatement("INSERT INTO " + sql.getRanksTable() + "_permissions (UUID,PERMISSION) VALUES (?,?)");

                    insert.setString(1, getUUID().toString());
                    insert.setString(2, permission);

                    insert.executeUpdate();
                    insert.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
