package com.yourname.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

public class FileOrganizer {
	
	// 1. THE BRAIN: Our dictionary of rules
    private static final Map<String, String> FILE_TYPES = new HashMap<>();

    static {
        FILE_TYPES.put("pdf", "Documents");
        FILE_TYPES.put("docx", "Documents");
        FILE_TYPES.put("xlsx", "Documents");
        FILE_TYPES.put("md", "Documents");
        
        FILE_TYPES.put("jpg", "Images");
        FILE_TYPES.put("jpeg", "Images");
        FILE_TYPES.put("png", "Images");
        
        FILE_TYPES.put("zip", "Archives");
        FILE_TYPES.put("exe", "Installers");
        
        FILE_TYPES.put("sql", "Code");
        FILE_TYPES.put("tsx", "Code");
    }

    // 2. THE SLICER: A helper method to grab just the "jpg" or "pdf" part
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == 0) return ""; 
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
    
    
    

    public static void processFiles(JTextArea logArea) {
        Path sourceDirectory = Paths.get(System.getProperty("user.home"), "Downloads");
        File folder = sourceDirectory.toFile();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            logArea.append("❌ Could not find the Downloads directory!\n");
            return;
        }
        
        logArea.append("Cleaning up folder...\n");

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String extension = getFileExtension(file);
                
                if (FILE_TYPES.containsKey(extension)) {
                    String targetFolder = FILE_TYPES.get(extension);
                    Path targetDir = sourceDirectory.resolve(targetFolder);
                    
                    try {
                        if (!Files.exists(targetDir)) {
                            Files.createDirectories(targetDir);
                        }
                        
                        Path targetPath = targetDir.resolve(file.getName());
                        Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                        
                        // Replaced System.out.println with logArea.append
                        logArea.append("✅ Moved: " + file.getName() + " -> " + targetFolder + "\n");
                        
                    } catch (IOException e) {
                        logArea.append("❌ Failed to move: " + file.getName() + "\n");
                    }
                } else {
                    logArea.append("⚠️ Ignored: " + file.getName() + " (No rule for " + extension + ")\n");
                }
            }
        }
        logArea.append("\nDone! Check your folder.");
    }
}