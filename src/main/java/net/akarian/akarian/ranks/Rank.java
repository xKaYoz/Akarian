package net.akarian.akarian.ranks;

import lombok.Getter;
import lombok.Setter;
import net.akarian.akarian.Akarian;
import net.akarian.akarian.mysql.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rank {

    @Getter
    @Setter
    UUID UUID;
    @Getter
    @Setter
    String prefix;
    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    Rank parent;
    @Getter
    @Setter
    List<String> permissions = new ArrayList<>();

    public Rank(UUID uuid, String name, String prefix) {
        this.UUID = uuid;
        this.name = name;
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

                Rank rank = new Rank(java.util.UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3));

                return rank;

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
        //TODO Update all users with this rank

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
                PreparedStatement update = sql.getConnection().prepareStatement("UPDATE " + sql.getRanksTable() + " SET NAME=?, PREFIX=?, PARENT=? WHERE UUID=?");

                update.setString(1, name);
                update.setString(2, prefix);
                if (parent == null) {
                    update.setString(3, null);
                } else {
                    update.setString(3, parent.getUUID().toString());
                }
                update.setString(4, getUUID().toString());

                update.executeUpdate();
                update.close();

                savePermissions();

                return;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement insert = sql.getConnection().prepareStatement("INSERT INTO " + sql.getRanksTable() + " (UUID,NAME,PREFIX,PARENT) VALUES (?,?,?,?)");

            insert.setString(1, getUUID().toString());
            insert.setString(2, name);
            insert.setString(3, prefix);
            if (parent == null) {
                insert.setString(4, null);
            } else {
                insert.setString(4, parent.getUUID().toString());
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
