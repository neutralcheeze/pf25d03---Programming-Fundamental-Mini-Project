package Bab5.src;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginScreen extends JPanel {
    private static final Color BG_COLOR = new Color(45, 52, 54);
    private static final Color BTN_COLOR = new Color(99, 110, 114);
    private static final Color PRIMARY_TEXT_COLOR = new Color(223, 230, 233);
    private static final Color BORDER_COLOR = new Color(170, 170, 170);

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 40);
    private static final Font BTN_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font LABEL_FONT  = new Font("Arial", Font.PLAIN, 14);

    JPanel jPanel = new JPanel();

    JLabel title = new JLabel("Register");


    JTextField usernameField = new JTextField(15);
    JPasswordField passwordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField();


    public LoginScreen(JFrame frame) {
        setBackground(BG_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField.setPreferredSize(new Dimension(200, 30));
        usernameField.setMaximumSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(30));


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(PRIMARY_TEXT_COLOR);
        usernameLabel.setFont(LABEL_FONT);

        JPanel usnPanel = new JPanel(new BorderLayout());
        usnPanel.setBackground(BG_COLOR);
        usnPanel.add(usernameLabel, BorderLayout.NORTH);
        usnPanel.add(usernameField, BorderLayout.CENTER);
        usnPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(usnPanel);
        add(Box.createVerticalStrut(20));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(PRIMARY_TEXT_COLOR);
        passwordLabel.setFont(LABEL_FONT);

        JPanel pswPanel = new JPanel(new BorderLayout());
        pswPanel.setBackground(BG_COLOR);
        pswPanel.add(passwordLabel, BorderLayout.NORTH);
        pswPanel.add(passwordField, BorderLayout.CENTER);
        pswPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(pswPanel);
        add(Box.createVerticalStrut(20));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(BTN_COLOR);
        loginBtn.setForeground(PRIMARY_TEXT_COLOR);
        loginBtn.setFont(BTN_FONT);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        add(loginBtn);
        add(Box.createVerticalStrut(10));

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            handleLogin(username, password);
        });


        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

        });

        JLabel footerLabel = new JLabel("Don't have an account?");
        footerLabel.setFont(LABEL_FONT);
        footerLabel.setForeground(PRIMARY_TEXT_COLOR);
        add(footerLabel);
        add(Box.createVerticalStrut(10));

        JButton registerButton = new JButton("Register Here");
        registerButton.setBackground(BG_COLOR);
        registerButton.setFont(LABEL_FONT);
        registerButton.setForeground(new Color(138, 189, 248));
        registerButton.setMargin(new Insets(0, 0, 0, 0));
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.addActionListener(e -> {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Register");
                    frame.setContentPane(new RegisterScreen(frame));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });

            SwingUtilities.getWindowAncestor(this).dispose();

        });

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BG_COLOR);
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        footerPanel.add(registerButton, BorderLayout.CENTER);
        footerPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(footerPanel);
    }


    void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please input all fields.");
            return;
        }

        try {
            Authentication auth = new Authentication();
            boolean success = auth.login(username, password);

            if (success) {
                JOptionPane.showMessageDialog(null, "Login Successfull!");

                Player.setUsername(username);

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFrame frame = new JFrame("MENU");
                        frame.setContentPane(new StartScreen());
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                });

                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Username or password is invalid!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame  = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new LoginScreen(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
