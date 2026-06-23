package com.yourname.organizer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOrganizer {

    public static void main(String[] args) {
        // 1. Point the program at your folder (UPDATE THIS PATH!)
        // Path sourceDirectory = Paths.get("/home/snake/Downloads");
        Path sourceDirectory = Paths.get(
        	    System.getProperty("user.home"),
        	    "Downloads"
        	);
        
        // 2. Grab the folder and get a list of everything inside it
        File folder = sourceDirectory.toFile();
        File[] listOfFiles = folder.listFiles();

        // 3. Safety check: Did we find the folder?
        if (listOfFiles == null) {
            System.out.println("Could not find the directory. Please check your path!");
            return; // Stops the program
        }

        // 4. The Loop: Go through the list and print the names
        System.out.println("Scanning folder... found the following files:");
        for (File file : listOfFiles) {
            // We only want to print files, not sub-folders
            if (file.isFile()) {
                System.out.println("- " + file.getName());
            }
        }
    }
}