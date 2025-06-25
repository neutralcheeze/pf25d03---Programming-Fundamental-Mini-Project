package Bab5.src;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LeaderboardScreen extends JPanel {

    private static final Color BG_COLOR = new Color(45, 52, 54);
    private static final Color BTN_COLOR = new Color(99, 110, 114);
    private static final Color PRIMARY_TEXT_COLOR = new Color(223, 230, 233);
    private static final Color BORDER_COLOR = new Color(170, 170, 170);

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 40);
    private static final Font BTN_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font LABEL_FONT  = new Font("Arial", Font.PLAIN, 14);
    public LeaderboardScreen() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_COLOR);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(BG_COLOR);

        JButton backBtn = new JButton("↩️   BACK");
        backBtn.setBackground(BG_COLOR);
        backBtn.setForeground(PRIMARY_TEXT_COLOR);
        backBtn.setFont(new Font(null, Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("MENU ");
                    frame.setContentPane(new StartScreen());
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });

            SwingUtilities.getWindowAncestor(this).dispose();
        });

        backPanel.add(backBtn);

        // add backPanel to LEFT
        topPanel.add(backPanel, BorderLayout.NORTH);


        setBackground(BG_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

       JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);
       title.setFont(TITLE_FONT);
       title.setForeground(PRIMARY_TEXT_COLOR);
       topPanel.add(title, BorderLayout.CENTER);
       add(topPanel, BorderLayout.NORTH);
       add(Box.createVerticalStrut(10));

       JTable table = new JTable();

       try {
           LeaderboardService leaderboardService = new LeaderboardService();
           table.setModel(leaderboardService.getLeaderboardTable());
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
       }

       table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);

        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
       add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame("Leaderboard");
        frame.setContentPane(new LeaderboardScreen());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
