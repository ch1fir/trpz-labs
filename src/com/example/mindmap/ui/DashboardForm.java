package com.example.mindmap.ui;

import com.example.mindmap.entities.MindMap;
import com.example.mindmap.entities.User;
import com.example.mindmap.services.AuthService;
import com.example.mindmap.services.MindMapService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardForm extends JFrame {

    private final User user;
    private final AuthService authService;
    private final MindMapService mindMapService;

    private DefaultListModel<MindMap> listModel;
    private JList<MindMap> mapsList;

    public DashboardForm(User user, AuthService authService, MindMapService mindMapService) {
        super("Mind Map - Dashboard");
        this.user = user;
        this.authService = authService;
        this.mindMapService = mindMapService;

        initComponents();
        loadMaps();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(720, 430);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 252));
        add(mainPanel);

        // Верхня панель
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Ліва частина: Hi + Your mind maps
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel hiLabel = new JLabel("Hi, " + user.getUsername());
        hiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hiLabel.setForeground(new Color(120, 120, 145));

        JLabel subtitleLabel = new JLabel("Your mind maps");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        subtitleLabel.setForeground(new Color(40, 40, 70));

        leftPanel.add(hiLabel);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(subtitleLabel);

        topBar.add(leftPanel, BorderLayout.WEST);

        // Права частина: New map + Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JButton newMapBtn = new JButton("New map");
        JButton logoutBtn = new JButton("Logout");

        styleNewMapButton(newMapBtn);
        styleLogoutButton(logoutBtn);

        rightPanel.add(newMapBtn);
        rightPanel.add(logoutBtn);

        topBar.add(rightPanel, BorderLayout.EAST);

        // Центральна частина
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));

        listModel = new DefaultListModel<>();
        mapsList = new JList<>(listModel);
        mapsList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mapsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(mapsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 227, 235)));

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Обробники кнопок
        newMapBtn.addActionListener(e -> onCreateMap());
        logoutBtn.addActionListener(e -> onLogout());

        // Подвійний клік по мапі – відкриває редактор
        mapsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // double click
                    MindMap selected = mapsList.getSelectedValue();
                    if (selected != null) {
                        openEditor(selected);
                    }
                }
            }
        });
    }

    private void styleNewMapButton(JButton btn) {
        btn.setBackground(new Color(76, 175, 80)); // зелений
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 26, 10, 26));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void styleLogoutButton(JButton btn) {
        btn.setBackground(new Color(244, 67, 54)); // червоний
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void loadMaps() {
        listModel.clear();
        List<MindMap> maps = mindMapService.getMapsByUser(user);
        for (MindMap m : maps) {
            listModel.addElement(m);
        }
    }

    private void onCreateMap() {
        String title = JOptionPane.showInputDialog(this, "Enter map title:");
        if (title == null) return;
        if (title.isBlank()) {
            JOptionPane.showMessageDialog(this, "Назва мапи обов'язкова");
            return;
        }

        mindMapService.createMap(title, user);
        loadMaps();
    }

    private void onLogout() {
        dispose();
        SwingUtilities.invokeLater(() ->
                new LoginForm(authService, mindMapService).setVisible(true)
        );
    }

    private void openEditor(MindMap map) {
        SwingUtilities.invokeLater(() ->
                new MindMapEditorForm(map, mindMapService).setVisible(true)
        );
    }
}
