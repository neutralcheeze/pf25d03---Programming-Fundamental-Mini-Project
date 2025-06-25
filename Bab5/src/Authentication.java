package Bab5.src;

import java.sql.*;

public class Authentication {
    private final Connection conn;

    public Authentication() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public boolean register(String username, String password, String confirmation_password) throws SQLException {
       PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO players (username, password, score) VALUES (?, ?, ?)");
       insertStatement.setString(1, username);
       insertStatement.setString(2, password);
       insertStatement.setInt(3, 0);

       insertStatement.executeUpdate();

       return true;
    }

    public boolean login(String username, String password) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM players WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();
        return result.next();
    }

    public boolean isUsernameFound(String username) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT username FROM players WHERE username = ?");
        statement.setString(1, username);

        ResultSet result = statement.executeQuery();
        return result.next();
    }
}
