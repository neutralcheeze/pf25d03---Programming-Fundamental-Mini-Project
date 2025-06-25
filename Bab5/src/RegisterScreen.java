package Bab5.src;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterScreen extends JPanel {

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


    public RegisterScreen(JFrame frame) {
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
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
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

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(PRIMARY_TEXT_COLOR);
        confirmPasswordLabel.setFont(LABEL_FONT);

        JPanel confirmPswPanel = new JPanel(new BorderLayout());
        confirmPswPanel.setBackground(BG_COLOR);
        confirmPswPanel.add(confirmPasswordLabel, BorderLayout.NORTH);
        confirmPswPanel.add(confirmPasswordField, BorderLayout.CENTER);
        confirmPswPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(confirmPswPanel);
        add(Box.createVerticalStrut(20));

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(BTN_COLOR);
        registerBtn.setForeground(PRIMARY_TEXT_COLOR);
        registerBtn.setFont(BTN_FONT);
        registerBtn.setFocusPainted(false);
        registerBtn.setAlignmentX(LEFT_ALIGNMENT);
        add(registerBtn);
        add(Box.createVerticalStrut(10));


        registerBtn.addActionListener(e -> {
           String username = usernameField.getText();
           String password = new String(passwordField.getPassword());
           String confirmation_password = new String(confirmPasswordField.getPassword());

           handleRegister(username, password, confirmation_password);
        });

        JLabel footerLabel = new JLabel("Already have an account?");
        footerLabel.setFont(LABEL_FONT);
        footerLabel.setForeground(PRIMARY_TEXT_COLOR);
        add(footerLabel);
        add(Box.createVerticalStrut(10));

        JButton loginButton = new JButton("Login Here");
        loginButton.setBackground(BG_COLOR);
        loginButton.setFont(LABEL_FONT);
        loginButton.setForeground(new Color(138, 189, 248));
        loginButton.setMargin(new Insets(0, 0, 0, 0));
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Login");
                    frame.setContentPane(new LoginScreen(frame));
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
        footerPanel.add(loginButton, BorderLayout.CENTER);
        footerPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(footerPanel);
    }

    void handleRegister(String username, String password, String confirmation_password) {
        if (username.isEmpty() || password.isEmpty() || confirmation_password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please input all fields.");
            return;
        }

        if (!password.equals(confirmation_password)) {
            JOptionPane.showMessageDialog(this, "Password confirmation does not match");
            return;
        }

        try {
            Authentication auth = new Authentication();
            if (auth.isUsernameFound(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken");
                return;
            }

           boolean success = auth.register(username, password, confirmation_password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration Successfull!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame  = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new RegisterScreen(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
