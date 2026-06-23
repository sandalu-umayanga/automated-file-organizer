package com.yourname.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GUIFileOrganizer {

    public static void main(String[] args) {
        // Rule #1 of Java GUIs: Always start them on the "Event Dispatch Thread"
        // This prevents the window from freezing or crashing.
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // 1. Create the window frame
        JFrame frame = new JFrame("Magic File Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        // NEW: Tell the window how to arrange things. 
        // BorderLayout lets us stick things to the top (NORTH), bottom (SOUTH), or middle (CENTER).
        frame.setLayout(new BorderLayout());

        // 2. Create the text area to show logs
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false); // Make it read-only so the user can't type in it
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Wrap the text area in a ScrollPane so we can scroll if there are too many files
        JScrollPane scrollPane = new JScrollPane(logArea);
        
        
        // 3. Create the action button
        JButton runButton = new JButton("Clean My Downloads Folder!");
        // Make the button tall enough to easily click
        runButton.setPreferredSize(new Dimension(600, 50));
        
        // THE MAGIC CONNECTION:
        runButton.addActionListener(e -> {
            logArea.setText(""); // Clear the screen
            // Call the other class and pass the logArea to it!
            FileOrganizer.processFiles(logArea); 
        });
        
        // 4. Add the components to the window frame
        frame.add(runButton, BorderLayout.NORTH); // Stick button to the top
        frame.add(scrollPane, BorderLayout.CENTER); // Let the text area fill the rest of the space
        
        // 5. Show it!
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}