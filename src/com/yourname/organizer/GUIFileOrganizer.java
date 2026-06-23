package com.yourname.organizer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GUIFileOrganizer {

    // 1. STATE VARIABLES: These keep track of the user's choices
    private final Map<String, String> fileTypes = new HashMap<>();
    private Path selectedDirectory;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIFileOrganizer().createAndShowGUI());
    }

    public GUIFileOrganizer() {
        // Load some default rules into our map to start
        fileTypes.put("pdf", "Documents");
        fileTypes.put("jpg", "Images");
        fileTypes.put("png", "Images");
        fileTypes.put("zip", "Archives");
        
        // Default the folder to the user's Downloads directory
        selectedDirectory = Paths.get(System.getProperty("user.home"), "Downloads");
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Dynamic File Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL: Configuration Area ---
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Folder Selection Row
        JPanel folderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        folderPanel.add(new JLabel("Target Folder:"));
        JTextField folderPathField = new JTextField(selectedDirectory.toString(), 40);
        folderPathField.setEditable(false);
        JButton browseButton = new JButton("Browse...");
        folderPanel.add(folderPathField);
        folderPanel.add(browseButton);

        // Rule Addition Row
        JPanel rulePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rulePanel.add(new JLabel("File Extension (e.g. txt):"));
        JTextField extField = new JTextField(7);
        rulePanel.add(new JLabel("Move to Folder:"));
        JTextField destField = new JTextField(12);
        JButton addRuleButton = new JButton("Add Rule");
        rulePanel.add(extField);
        rulePanel.add(destField);
        rulePanel.add(addRuleButton);

        configPanel.add(folderPanel);
        configPanel.add(rulePanel);

        // --- CENTER PANEL: Logs ---
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Logs & Active Rules"));
        
        // Print the default rules to the screen on startup
        logArea.append("Active Rules Loaded:\n");
        fileTypes.forEach((ext, dir) -> logArea.append("  ." + ext + " -> [" + dir + "]\n"));
        logArea.append("--------------------------------------------------\n");

        // --- BOTTOM PANEL: Action Button ---
        JButton runButton = new JButton("Organize Selected Folder!");
        runButton.setFont(new Font("Arial", Font.BOLD, 16));
        runButton.setBackground(new Color(46, 204, 113));
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        runButton.setPreferredSize(new Dimension(0, 50));

        // --- EVENT LISTENERS (Making the buttons work) ---
        
        // 1. Browse Button Logic
        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(selectedDirectory.toFile());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int choice = chooser.showOpenDialog(frame);
            if (choice == JFileChooser.APPROVE_OPTION) {
                selectedDirectory = chooser.getSelectedFile().toPath();
                folderPathField.setText(selectedDirectory.toString());
                logArea.append("📁 Target changed to: " + selectedDirectory + "\n");
            }
        });

        // 2. Add Rule Button Logic
        addRuleButton.addActionListener(e -> {
            String ext = extField.getText().trim().toLowerCase();
            if (ext.startsWith(".")) ext = ext.substring(1); // Remove '.' if user typed it
            String dest = destField.getText().trim();
            
            if (!ext.isEmpty() && !dest.isEmpty()) {
                fileTypes.put(ext, dest); // Update our map
                logArea.append("➕ New Rule Added: ." + ext + " -> [" + dest + "]\n");
                extField.setText("");
                destField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both an extension and a destination folder.");
            }
        });

        // 3. THE MAGIC CONNECTION: Run Button Logic
        runButton.addActionListener(e -> {
            logArea.append("\n🚀 Starting cleanup in: " + selectedDirectory.toString() + "\n");
            
            // Hand the GUI data over to the Logic class!
            FileOrganizer.processFiles(logArea, selectedDirectory, fileTypes);
        });

        // Assemble and show the window
        frame.add(configPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(runButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}