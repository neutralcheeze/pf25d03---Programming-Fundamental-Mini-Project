package Bab5.src;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {

    // Kumpulan konstanta untuk warna dan font yang digunakan dalam UI
    private static final Color WARNA_BACKGROUND = new Color(45, 52, 54);
    private static final Color WARNA_BUTTON = new Color(99, 110, 114);
    private static final Color WARNA_TEKS_UTAMA = new Color(223, 230, 233);
    private static final Color WARNA_BORDER = new Color(170, 170, 170);
    private static final Font FONT_JUDUL = new Font("Arial", Font.BOLD, 40);
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    private static final Font FONT_CREDITS = new Font("Arial", Font.ITALIC, 10);

    public StartScreen() {
        // Pengaturan dasar untuk window JFrame
        setTitle("Choose Game Mode");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama yang menggunakan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(WARNA_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Label untuk judul game "Tic Tac Toe"
        JLabel titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(FONT_JUDUL);
        titleLabel.setForeground(WARNA_TEKS_UTAMA);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Inisialisasi komponen tombol
        JButton pvpButton = new JButton("Player 👤 vs 👤 Player");
        JButton pvbButton = new JButton("Player 👤 vs 🤖 Bot");
        JButton leaderboardButton = new JButton("Leaderboard");

        // Menerapkan style visual ke semua tombol
        styleButton(pvpButton);
        styleButton(pvbButton);
        styleButton(leaderboardButton);

        // Mengatur ukuran preferensi untuk setiap tombol
        pvpButton.setPreferredSize(new Dimension(220, 50));
        pvbButton.setPreferredSize(new Dimension(220, 50));
        leaderboardButton.setPreferredSize(new Dimension(220, 50));

        // Menambahkan fungsi saat tombol Player vs Player diklik
        pvpButton.addActionListener(e -> {
            // Meminta input nama untuk Player X melalui dialog pop-up
            String nameX = JOptionPane.showInputDialog(this, "Enter name for Player X:", "Player X");
            if (nameX == null || nameX.trim().isEmpty()) nameX = "Player X"; // Nama default jika input kosong

            // Meminta input nama untuk Player O melalui dialog pop-up
            String nameO = JOptionPane.showInputDialog(this, "Enter name for Player O:", "Player O");
            if (nameO == null || nameO.trim().isEmpty()) nameO = "Player O"; // Nama default jika input kosong

            // Meluncurkan game dengan mode PvP beserta nama pemain
            launchGame(GameMode.PVP, nameX, nameO);
        });

        // Menambahkan fungsi saat tombol Player vs Bot diklik
        pvbButton.addActionListener(e -> {
            // Meminta input nama untuk pemain
            String nameX = JOptionPane.showInputDialog(this, "Enter name for Player X:", "Your Name");
            if (nameX == null || nameX.trim().isEmpty()) nameX = "Player"; // Nama default jika input kosong

            // Menetapkan "Bot" sebagai nama pemain kedua
            String nameO = "Bot";

            // Meluncurkan game dengan mode PvB beserta nama pemain
            launchGame(GameMode.PVB, nameX, nameO);
        });

        // Menambahkan fungsi saat tombol Leaderboard diklik
        leaderboardButton.addActionListener(e -> {
            // Menampilkan pesan bahwa fitur belum tersedia
            JOptionPane.showMessageDialog(this, "Fitur Leaderboard akan segera hadir!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        });

        // Panel untuk menampung dan menata tombol-tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 15)); // Layout grid: 3 baris, 1 kolom
        buttonPanel.setBackground(WARNA_BACKGROUND);
        buttonPanel.add(leaderboardButton);
        buttonPanel.add(pvpButton);
        buttonPanel.add(pvbButton);

        // Menambahkan panel tombol ke area tengah panel utama
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Panel untuk menampung teks credits di bagian bawah
        JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Layout rata kanan
        creditsPanel.setBackground(WARNA_BACKGROUND);
        JLabel creditsLabel = new JLabel("developed by Zul, Fabio, Nabil");
        creditsLabel.setFont(FONT_CREDITS);
        creditsLabel.setForeground(Color.GRAY);
        creditsPanel.add(creditsLabel);

        // Menambahkan panel credits ke area bawah panel utama
        mainPanel.add(creditsPanel, BorderLayout.SOUTH);

        // Menambahkan panel utama ke frame dan menampilkannya
        add(mainPanel);
        setVisible(true);
    }

    // Metode untuk menerapkan style visual pada tombol
    private void styleButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(WARNA_BUTTON);
        button.setForeground(WARNA_TEKS_UTAMA);
        button.setFocusPainted(false);

        // Membuat border garis yang terlihat
        Border lineBorder = BorderFactory.createLineBorder(WARNA_BORDER, 1);
        // Membuat border kosong untuk spasi/padding di dalam
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        // Menggabungkan kedua border tersebut
        button.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
    }

    // Metode untuk menutup menu dan meluncurkan window permainan
    private void launchGame(GameMode mode, String playerXName, String playerOName) {
        JFrame gameFrame = new JFrame("Tic-Tac-Toe");
        // Memanggil constructor GameMain yang menerima nama pemain
        gameFrame.setContentPane(new GameMain(mode, playerXName, playerOName));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        // Menutup window StartScreen
        this.dispose();
    }
}