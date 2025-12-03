package com.example.mindmap.ui;

import com.example.mindmap.entities.User;
import com.example.mindmap.services.AuthService;
import com.example.mindmap.services.MindMapService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginForm extends JFrame {

    private final AuthService authService;
    private final MindMapService mindMapService;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm(AuthService authService, MindMapService mindMapService) {
        super("Mind Map - Login");
        this.authService = authService;
        this.mindMapService = mindMapService;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(430, 270);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(245, 247, 252));
        add(background);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));

        background.add(card);

        JLabel titleLabel = new JLabel("Mind Map", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 80));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(6, 6, 6, 6);
        fgbc.anchor = GridBagConstraints.WEST;
        fgbc.fill = GridBagConstraints.HORIZONTAL;

        fgbc.gridx = 0;
        fgbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), fgbc);

        fgbc.gridx = 1;
        usernameField = new JTextField(18);
        formPanel.add(usernameField, fgbc);

        fgbc.gridx = 0;
        fgbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), fgbc);

        fgbc.gridx = 1;
        passwordField = new JPasswordField(18);
        formPanel.add(passwordField, fgbc);

        card.add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton registerBtn = new JButton("Sign up");
        JButton loginBtn = new JButton("Login");

        stylePrimaryButton(loginBtn);
        styleSecondaryButton(registerBtn);

        buttonsPanel.add(registerBtn);
        buttonsPanel.add(loginBtn);
        card.add(buttonsPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> onLogin());
        registerBtn.addActionListener(e -> onRegister());
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            SwingUtilities.invokeLater(() ->
                    new DashboardForm(user, authService, mindMapService).setVisible(true)
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    private void onRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Username and password are required");
            return;
        }

        authService.register(username, password);

        JOptionPane.showMessageDialog(this,
                "Welcome, " + username + "!\nNow you may log in.");
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(new Color(88, 101, 242));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(new Color(235, 238, 246));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
}
