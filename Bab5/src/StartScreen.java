package Bab5.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {
    public StartScreen() {
        setTitle("Choose Game Mode");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Button
        JButton pvpButton = new JButton("Player 👤 vs 👤 Player");
        JButton pvbButton = new JButton("Player 👤 vs 🤖 Bot");
        JButton leaderboardButton = new JButton("Leaderboard");

        // set button size
        pvpButton.setPreferredSize(new Dimension(200, 40));
        pvbButton.setPreferredSize(new Dimension(200, 40));

        pvpButton.addActionListener(e -> {
            System.out.println("PVP MODE");
            launchGame(GameMode.PVP);
        });

        pvbButton.addActionListener(e -> {
            System.out.println("PVB MODE");
            launchGame(GameMode.PVB);
        });

        leaderboardButton.addActionListener(e -> {
            System.out.println("Leaderboard");
        });

        // Layout
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
        panel.add(leaderboardButton);
        panel.add(pvpButton);
        panel.add(pvbButton);

        add(panel);
        setVisible(true);


    }

    private void launchGame(GameMode mode) {
        JFrame gameFrame = new JFrame("Tic-Tac-Toe");
        gameFrame.setContentPane(new GameMain(mode));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        this.dispose();
    }

}
