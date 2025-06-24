package Bab5.src;

import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://mysql-ac852dc-achmadzulfikar-fp.f.aivencloud.com:10410/tictactoe?useSSL=true&requireSSL=true",
                    "avnadmin",
                    "AVNS_AV26kMKYitWxS0e67mI"
            );

            System.out.println("✅ Connected to Aiven MySQL!");

            // Coba query sederhana
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");

            while (rs.next()) {
                System.out.println("🗃 Table: " + rs.getString(1));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
