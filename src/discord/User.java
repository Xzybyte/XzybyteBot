package discord;

import main.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String id;
    private int tokens;

    public User(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getTokens() {
        return tokens;
    }

    public void gainTokens(int tokens) {
        this.tokens += tokens;
    }

    public static void InsertUser(String id) {
        try {
            Connection con = Main.getInstance().getMySQL().getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO user(id) VALUES (?)");
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User LoadUser(String id) {
        try {
            User user = new User(id);
            Connection con = Main.getInstance().getMySQL().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return null;
            }
            user.tokens = rs.getInt("tokens");
            rs.close();
            ps.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser() {
        try {
            Connection con = Main.getInstance().getMySQL().getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE user SET tokens = ? WHERE id = ?");
            ps.setInt(1, tokens);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Long> cd = new HashMap<>();

    public static void addToCD(String author) {
        cd.put(author, System.currentTimeMillis());
    }

    public static boolean getCD(String author) {
        for (Map.Entry<String, Long> entry : cd.entrySet()) {
            if (entry.getKey().equals(author)) {
                if (entry.getValue() + 3000 > System.currentTimeMillis()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static long returnCD(String author) {
        long time = 0;
        for (Map.Entry<String, Long> entry : cd.entrySet()) {
            if (entry.getKey().equals(author)) {
                time = entry.getValue();
                break;
            }
        }
        return time;
    }
}
