package Bab5.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Player {
   private static String currentUsername;
   private Connection conn;

   public static void setUsername(String username) {
       currentUsername = username;
   }

   public static String getUsername() {
       return currentUsername;
   }

   public static void updateScore(String username)  {
       try (Connection conn = DatabaseConnector.getConnection()) {
           PreparedStatement statement = conn.prepareStatement("UPDATE players SET score = score + 1 WHERE username = ?");
           statement.setString(1, username);
           statement.executeUpdate();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
