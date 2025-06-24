package Bab5.src;

import java.sql.*;

public class DatabaseConnector {
        private static final String URL = "jdbc:mysql://mysql-ac852dc-achmadzulfikar-fp.f.aivencloud.com:10410/tictactoe";
        private static final String USER = "avnadmin";
        private static final String PASSWORD = "AVNS_AV26kMKYitWxS0e67mI";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
}
