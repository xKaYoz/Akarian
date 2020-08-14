package net.akarian.akarian.users;

import lombok.Getter;
import lombok.Setter;
import net.akarian.akarian.Akarian;
import net.akarian.akarian.mysql.MySQLManager;
import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.ranks.RankManager;
import net.akarian.akarian.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class User {

    @Getter
    private UUID uuid;

    @Getter
    @Setter
    private Rank mainRank;

    @Getter
    @Setter
    private Rank prisonRank;

    @Getter
    @Setter
    private Rank donatorRank;

    @Getter
    @Setter
    private int tokens;

    @Getter
    @Setter
    private PermissionAttachment attachment;

    public User() {
    }

    public User(UUID uuid) {
        this.uuid = uuid;

        attachment = Bukkit.getPlayer(uuid).addAttachment(Akarian.getInstance());

        loadPermissions();

        UserManager.registerUser(this);

    }

    public void loadPermissions() {

        if (mainRank != null) {
            for (String s : mainRank.getPermissions()) {
                attachment.setPermission(s, true);
            }
        }

    }

    public void unloadPermissions() {

        for (String s : attachment.getPermissions().keySet()) {
            attachment.unsetPermission(s);
        }

    }

    public User loadUser(UUID uuid) {

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            PreparedStatement statement = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getUsersTable() + " WHERE UUID=?");

            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                User user = new User(uuid);

                if (rs.getString(2) != null) {
                    user.setMainRank(RankManager.getRank(UUID.fromString(rs.getString(2))));

                    for (String permission : user.getMainRank().getPermissions()) {
                        user.getAttachment().setPermission(permission, true);
                    }
                } else {
                    user.setMainRank(RankManager.getRank("Member"));

                    for (String permission : user.getMainRank().getPermissions()) {
                        user.getAttachment().setPermission(permission, true);
                    }
                }
                if (rs.getString(3) != null) {
                    user.setPrisonRank(RankManager.getRank(UUID.fromString(rs.getString(3))));

                    for (String permission : user.getPrisonRank().getPermissions()) {
                        user.getAttachment().setPermission(permission, true);
                    }
                }
                if (rs.getString(4) != null) {
                    user.setDonatorRank(RankManager.getRank(UUID.fromString(rs.getString(4))));

                    for (String permission : user.getDonatorRank().getPermissions()) {
                        user.getAttachment().setPermission(permission, true);
                    }
                }

                user.setTokens(rs.getInt(5));

                rs.close();

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {

            long start = System.currentTimeMillis();

            PreparedStatement create = sql.getConnection().prepareStatement(
                    "INSERT INTO " + sql.getUsersTable() +
                            "(UUID,MAIN_RANK,PRISON_RANK,DONATOR_RANK,TOKENS) VALUES (?,?,?,?,?)");

            create.setString(1, uuid.toString());
            if (mainRank == null) {
                create.setString(2, null);
            } else {
                create.setString(2, mainRank.getUUID().toString());
            }
            if (prisonRank == null) {
                create.setString(3, null);
            } else {
                create.setString(3, prisonRank.getUUID().toString());
            }
            if (donatorRank == null) {
                create.setString(4, null);
            } else {
                create.setString(4, donatorRank.getUUID().toString());
            }
            create.setInt(5, 0);

            create.executeUpdate();

            create.close();

            Chat.log("Created in " + (System.currentTimeMillis() - start) + "ms", true);

            return new User(uuid);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void saveUser() {

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            PreparedStatement statement = sql.getConnection().prepareStatement("UPDATE " + sql.getUsersTable() + " SET MAIN_RANK=?, PRISON_RANK=?, DONATOR_RANK=? WHERE UUID=?");

            if (mainRank == null) {
                statement.setString(1, null);
            } else {
                statement.setString(1, mainRank.getUUID().toString());
            }
            if (prisonRank == null) {
                statement.setString(2, null);
            } else {
                statement.setString(2, prisonRank.getUUID().toString());
            }
            if (donatorRank == null) {
                statement.setString(3, null);
            } else {
                statement.setString(3, donatorRank.getUUID().toString());
            }

            statement.setString(4, uuid.toString());

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
