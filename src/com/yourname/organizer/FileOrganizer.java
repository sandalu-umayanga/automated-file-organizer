package com.yourname.organizer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileOrganizer {
	
	// 1. THE BRAIN: Our dictionary of rules
    private static final Map<String, String> FILE_TYPES = new HashMap<>();

    static {
        // Add a few rules here based on what is actually in your folder!
        FILE_TYPES.put("pdf", "Documents");
        FILE_TYPES.put("jpg", "Images");
        FILE_TYPES.put("png", "Images");
        FILE_TYPES.put("exe", "Installers");
        FILE_TYPES.put("txt", "Documents");
    }

    // 2. THE SLICER: A helper method to grab just the "jpg" or "pdf" part
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == 0) return ""; 
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
    
    
    

    public static void main(String[] args) {
        // Point the program at your folder (UPDATE THIS PATH!)
        // Path sourceDirectory = Paths.get("/home/snake/Downloads");
    	
    	// Cross-platform version
        Path sourceDirectory = Paths.get(
        	    System.getProperty("user.home"),
        	    "Downloads"
        	);
        
        // Grab the folder and get a list of everything inside it
        File folder = sourceDirectory.toFile();
        File[] listOfFiles = folder.listFiles();

        // Safety check: Did we find the folder?
        if (listOfFiles == null) {
            System.out.println("Could not find the directory. Please check your path!");
            return; // Stops the program
        }
        
        
        System.out.println("Scanning and categorizing files...\n");
        
        

        // 3. THE MATCHMAKER: Loop through files and check the rules
        for (File file : listOfFiles) {
            if (file.isFile()) {
                // Slice off the extension
                String extension = getFileExtension(file);
                
                // Ask the HashMap: "Do you know what to do with this?"
                if (FILE_TYPES.containsKey(extension)) {
                    String targetFolder = FILE_TYPES.get(extension);
                    System.out.println("✅ Found '" + file.getName() + "'. It belongs in the [" + targetFolder + "] folder.");
                } else {
                    System.out.println("❌ Found '" + file.getName() + "'. I don't have a rule for this extension (" + extension + "). Ignoring.");
                }
            }
        }
    }
}