package net.akarian.akarian.users;

import lombok.Getter;
import lombok.Setter;
import net.akarian.akarian.Akarian;
import net.akarian.akarian.mysql.MySQLManager;
import net.akarian.akarian.ranks.Rank;
import net.akarian.akarian.utils.Chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class User {

    @Getter
    public UUID uuid;

    @Getter
    @Setter
    public Rank mainRank;

    @Getter
    @Setter
    public Rank prisonRank;

    @Getter
    @Setter
    public Rank donatorRank;

    public User() {
    }

    public User(UUID uuid) {
        this.uuid = uuid;

        UserManager.registerUser(this);

    }

    public User loadUser(UUID uuid) {

        MySQLManager sql = Akarian.getInstance().getMySQL();

        try {
            long start = System.currentTimeMillis();
            PreparedStatement statement = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getUsersTable() + " WHERE UUID=?");

            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                User user = new User(uuid);

                if (rs.getString(2) != null) {

                    //Clan clan = ClanManager.getClan(UUID.fromString(rs.getString(2)));

                    //user.setClan(clan);
                }

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
