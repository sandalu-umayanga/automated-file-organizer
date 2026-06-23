package com.yourname.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import javax.swing.JTextArea;

public class FileOrganizer {

    // 1. THE SLICER: Helper method to grab the extension
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == 0) return ""; 
        return name.substring(lastIndexOf + 1).toLowerCase();
    }

    // 2. THE LOGIC: Notice it accepts `dynamicRules` now! 
    // There is no hardcoded static map in this file anymore.
    public static void processFiles(JTextArea logArea, Path selectedDirectory, Map<String, String> dynamicRules) {
        File folder = selectedDirectory.toFile();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            logArea.append("❌ Could not access the selected directory!\n");
            return;
        }

        int filesMoved = 0;
        
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String extension = getFileExtension(file);
                
                // Ask the dynamic map from the GUI where this goes!
                if (dynamicRules.containsKey(extension)) {
                    String targetFolder = dynamicRules.get(extension);
                    Path targetDir = selectedDirectory.resolve(targetFolder);
                    
                    try {
                        if (!Files.exists(targetDir)) {
                            Files.createDirectories(targetDir);
                        }
                        
                        Path targetPath = targetDir.resolve(file.getName());
                        Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                        
                        logArea.append("✅ Moved: " + file.getName() + " -> " + targetFolder + "\n");
                        filesMoved++;
                        
                    } catch (IOException e) {
                        logArea.append("❌ Failed to move: " + file.getName() + "\n");
                    }
                } else {
                    logArea.append("⚠️ Ignored: " + file.getName() + " (No rule for " + extension + ")\n");
                }
            }
        }
        logArea.append("\n🎉 Cleanup Complete! Successfully moved " + filesMoved + " files.\n");
    }
}