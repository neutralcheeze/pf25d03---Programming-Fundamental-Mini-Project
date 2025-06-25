package Bab5.src;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class LeaderboardService {
    private Connection conn;

    public LeaderboardService() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public DefaultTableModel getLeaderboardTable()throws SQLException {
        String[] columnNames = {"Username", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        PreparedStatement statement = conn.prepareStatement("SELECT username, score FROM players ORDER BY score DESC");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String username = rs.getString("username");
            int score = rs.getInt("score");
            model.addRow(new Object[]{username, score});
        }

        return model;
    }
}
