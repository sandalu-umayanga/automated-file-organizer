package com.yourname.organizer;

import javax.swing.JFrame;
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
        
        // 2. Tell the program to stop running when you click the 'X' to close the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 3. Set the size (Width: 600 pixels, Height: 400 pixels)
        frame.setSize(600, 400);
        
        // 4. Center it on the screen
        frame.setLocationRelativeTo(null);
        
        // 5. Make it visible!
        frame.setVisible(true);
    }
}