package com.example.mindmap.ui;

import com.example.mindmap.entities.MindMap;
import com.example.mindmap.entities.MapElement;
import com.example.mindmap.export.ExportService;
import com.example.mindmap.export.JpgExportStrategy;
import com.example.mindmap.export.PdfExportStrategy;
import com.example.mindmap.export.PngExportStrategy;
import com.example.mindmap.services.MindMapService;
import com.example.mindmap.services.commands.CommandManager;
import com.example.mindmap.services.commands.AddNodeCommand;
import com.example.mindmap.services.commands.MoveNodeCommand;
import com.example.mindmap.services.commands.DeleteNodeCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MindMapEditorForm extends JFrame {

    private final MindMap mindMap;
    private final MindMapService mindMapService;
    private final CanvasPanel canvasPanel;
    private final ExportService exportService;
    private final CommandManager commandManager;

    public MindMapEditorForm(MindMap mindMap, MindMapService mindMapService) {
        super("Editing: " + mindMap.getTitle());
        this.mindMap = mindMap;
        this.mindMapService = mindMapService;
        this.exportService = new ExportService();
        this.commandManager = new CommandManager();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Верхня панель з назвою та кнопками
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        topBar.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(mindMap.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonsPanel.setOpaque(false);

        JButton addNodeBtn = new JButton("Add node");
        addNodeBtn.setBackground(new Color(76, 175, 80));
        addNodeBtn.setForeground(Color.BLACK);
        addNodeBtn.setFocusPainted(false);
        addNodeBtn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        addNodeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton undoBtn = new JButton("Undo");
        undoBtn.setBackground(new Color(224, 224, 224));
        undoBtn.setForeground(Color.BLACK);
        undoBtn.setFocusPainted(false);
        undoBtn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        undoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton redoBtn = new JButton("Redo");
        redoBtn.setBackground(new Color(224, 224, 224));
        redoBtn.setForeground(Color.BLACK);
        redoBtn.setFocusPainted(false);
        redoBtn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        redoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton exportBtn = new JButton("Export");
        exportBtn.setBackground(new Color(88, 101, 242));
        exportBtn.setForeground(Color.BLACK);
        exportBtn.setFocusPainted(false);
        exportBtn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        exportBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        buttonsPanel.add(addNodeBtn);
        buttonsPanel.add(undoBtn);
        buttonsPanel.add(redoBtn);
        buttonsPanel.add(exportBtn);

        topBar.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Полотно
        canvasPanel = new CanvasPanel(mindMap, mindMapService, commandManager);
        mainPanel.add(canvasPanel, BorderLayout.CENTER);

        // Обробники
        addNodeBtn.addActionListener(e -> canvasPanel.addNewNode());

        undoBtn.addActionListener(e -> {
            commandManager.undo();
            canvasPanel.repaint();
        });

        redoBtn.addActionListener(e -> {
            commandManager.redo();
            canvasPanel.repaint();
        });

        exportBtn.addActionListener(e -> onExport());
    }

    private void onExport() {
        String[] options = {"PNG", "JPG", "PDF", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose export format:",
                "Export map",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1 || "Cancel".equals(options[choice])) {
            return;
        }

        String selectedFormat = options[choice];

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save exported file");

        String extension = selectedFormat.toLowerCase();
        chooser.setSelectedFile(new File(mindMap.getTitle().replaceAll("\\s+", "_") + "." + extension));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File targetFile = chooser.getSelectedFile();

        switch (selectedFormat) {
            case "PNG" -> exportService.setStrategy(new PngExportStrategy());
            case "JPG" -> exportService.setStrategy(new JpgExportStrategy());
            case "PDF" -> exportService.setStrategy(new PdfExportStrategy());
            default -> {
                JOptionPane.showMessageDialog(this, "Unsupported format");
                return;
            }
        }

        try {
            exportService.export(mindMap, canvasPanel, targetFile);
            JOptionPane.showMessageDialog(this, "Export successful:\n" + targetFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }

    /**
     * Внутрішній клас для полотна, де малюються вузли
     */
    private static class CanvasPanel extends JPanel {

        private final MindMap mindMap;
        private final MindMapService mindMapService;
        private final CommandManager commandManager;
        private final List<MapElement> elements = new ArrayList<>();

        private MapElement draggedElement = null;
        private int dragOffsetX;
        private int dragOffsetY;

        private float startDragX;
        private float startDragY;

        private MapElement selectedElement = null;

        public CanvasPanel(MindMap mindMap,
                           MindMapService mindMapService,
                           CommandManager commandManager) {
            this.mindMap = mindMap;
            this.mindMapService = mindMapService;
            this.commandManager = commandManager;

            setBackground(new Color(250, 250, 253));
            setFocusable(true); // щоб ловити Delete

            elements.addAll(mindMapService.getElementsForMap(mindMap));

            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    MapElement hit = findElementAt(e.getX(), e.getY());
                    if (hit != null) {
                        draggedElement = hit;
                        dragOffsetX = (int) (e.getX() - hit.getX());
                        dragOffsetY = (int) (e.getY() - hit.getY());
                        startDragX = hit.getX();
                        startDragY = hit.getY();

                        selectedElement = hit;
                        repaint();
                        requestFocusInWindow();
                    } else {
                        selectedElement = null;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (draggedElement != null) {
                        float finalX = draggedElement.getX();
                        float finalY = draggedElement.getY();

                        if (finalX != startDragX || finalY != startDragY) {
                            MoveNodeCommand cmd = new MoveNodeCommand(
                                    draggedElement,
                                    startDragX,
                                    startDragY,
                                    finalX,
                                    finalY,
                                    mindMapService
                            );
                            commandManager.executeCommand(cmd);
                        }

                        draggedElement = null;
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (draggedElement != null) {
                        draggedElement.setX(e.getX() - dragOffsetX);
                        draggedElement.setY(e.getY() - dragOffsetY);
                        repaint();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    MapElement hit = findElementAt(e.getX(), e.getY());

                    if (hit != null) {
                        selectedElement = hit;
                        repaint();
                        requestFocusInWindow();
                    } else {
                        selectedElement = null;
                        repaint();
                    }

                    if (e.getClickCount() == 2 && hit != null) {
                        String newText = JOptionPane.showInputDialog(
                                CanvasPanel.this,
                                "Edit text:",
                                hit.getTextForDisplay()
                        );
                        if (newText != null && !newText.isBlank()) {
                            hit.setTextForDisplay(newText.trim());
                            mindMapService.updateElement(hit);
                            repaint();
                            // тут можна зробити EditTextCommand
                        }
                    }
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);

            setupKeyBindings();
        }

        private void setupKeyBindings() {
            InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke("DELETE"), "delete-selected");
            im.put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete-selected");

            am.put("delete-selected", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    deleteSelectedElement();
                }
            });
        }

        private void deleteSelectedElement() {
            if (selectedElement == null) {
                return;
            }

            DeleteNodeCommand cmd = new DeleteNodeCommand(
                    mindMap,
                    mindMapService,
                    elements,
                    selectedElement
            );
            commandManager.executeCommand(cmd);
            selectedElement = null;
            repaint();
        }

        public void addNewNode() {
            int x = getWidth() / 2 - 60;
            int y = getHeight() / 2 - 20;

            AddNodeCommand cmd = new AddNodeCommand(
                    mindMap,
                    mindMapService,
                    elements,
                    x,
                    y,
                    "New idea"
            );

            commandManager.executeCommand(cmd);
            repaint();
        }

        private MapElement findElementAt(int x, int y) {
            for (int i = elements.size() - 1; i >= 0; i--) {
                MapElement el = elements.get(i);
                Rectangle bounds = getElementBounds(el);
                if (bounds.contains(x, y)) {
                    return el;
                }
            }
            return null;
        }

        private Rectangle getElementBounds(MapElement el) {
            int width = 140;
            int height = 45;
            return new Rectangle((int) el.getX(), (int) el.getY(), width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (MapElement el : elements) {
                Rectangle r = getElementBounds(el);

                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);

                if (el == selectedElement) {
                    g2.setStroke(new BasicStroke(2f));
                    g2.setColor(new Color(255, 140, 0));
                } else {
                    g2.setStroke(new BasicStroke(1f));
                    g2.setColor(new Color(180, 180, 200));
                }
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 12, 12);

                String text = el.getTextForDisplay();
                if (text != null) {
                    g2.setColor(new Color(40, 40, 60));
                    FontMetrics fm = g2.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textX = r.x + (r.width - textWidth) / 2;
                    int textY = r.y + (r.height + fm.getAscent()) / 2 - 4;
                    g2.drawString(text, textX, textY);
                }
            }

            g2.dispose();
        }
    }
}
