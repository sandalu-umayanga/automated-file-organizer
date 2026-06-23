🧹 Automated File Organizer

A lightweight, cross-platform Java utility that automatically scans your Downloads folder and neatly organizes your files into subdirectories (Documents, Images, Archives, etc.) based on their file extensions.

You can download the pre-built .deb (Linux) or .exe (Windows) installers directly from the releases in this repository!

🚀 How It Works

This project was built in three logical phases. It uses vanilla Java (java.nio.file) and requires zero external dependencies.

Phase 1: Reading the Files (Cross-Platform)

Instead of hardcoding a Windows or Mac path (like C:/Users/...), the program uses System.getProperty("user.home") to dynamically locate the Downloads folder on any operating system.

// Cross-platform directory resolution
Path sourceDirectory = Paths.get(System.getProperty("user.home"), "Downloads");

// Grab the folder and get a list of everything inside it
File folder = sourceDirectory.toFile();
File[] listOfFiles = folder.listFiles();

if (listOfFiles == null) {
    System.out.println("Could not find the directory. Please check your path!");
    return;
}

// Print files
for (File file : listOfFiles) {
    if (file.isFile()) {
        System.out.println("- " + file.getName());
    }
}


Phase 2: The Brain & The Slicer

Before moving files, the program needs to know where they go. We use a HashMap (The Brain) to link file extensions to target folders, and a helper method (The Slicer) to extract the extension from the filename.

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
        FILE_TYPES.put("pdf", "Documents");
        FILE_TYPES.put("jpg", "Images");
        FILE_TYPES.put("png", "Images");
        FILE_TYPES.put("exe", "Installers");
        FILE_TYPES.put("txt", "Documents");
    }

    // 2. THE SLICER: Helper method to grab the extension
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == 0) return ""; 
        return name.substring(lastIndexOf + 1).toLowerCase();
    }

    public static void main(String[] args) {
        Path sourceDirectory = Paths.get(System.getProperty("user.home"), "Downloads");
        File folder = sourceDirectory.toFile();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) return;
        
        System.out.println("Scanning and categorizing files...\n");

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String extension = getFileExtension(file);
                if (FILE_TYPES.containsKey(extension)) {
                    String targetFolder = FILE_TYPES.get(extension);
                    System.out.println("✅ Found '" + file.getName() + "'. Belongs in [" + targetFolder + "].");
                } else {
                    System.out.println("❌ Found '" + file.getName() + "'. No rule for (" + extension + "). Ignoring.");
                }
            }
        }
    }
}


Phase 3: The Final Working Code

This phase brings it all together. It creates the target directories if they don't exist and uses Files.move() to physically relocate the files.

package com.yourname.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FileOrganizer {
    
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

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == 0) return ""; 
        return name.substring(lastIndexOf + 1).toLowerCase();
    }

    public static void main(String[] args) {
        Path sourceDirectory = Paths.get(System.getProperty("user.home"), "Downloads");
        File folder = sourceDirectory.toFile();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            System.out.println("Could not find the directory. Please check your path!");
            return;
        }
        
        System.out.println("Cleaning up folder...\n");

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
                        System.out.println("✅ Moved: " + file.getName() + " -> " + targetFolder);
                        
                    } catch (IOException e) {
                        System.out.println("❌ Failed to move: " + file.getName());
                    }
                } else {
                    System.out.println("❌ Found '" + file.getName() + "'. No rule for (" + extension + "). Ignoring.");
                }
            }
        }
        System.out.println("\nDone! Check your folder.");
    }
}


🛠️ Building the Project

If you want to build this project from source, you can compile it into a Runnable JAR file.

1. Create a Executable JAR (Requires Java installed)

Run these commands from the root directory of the project:

Create output directory & compile:

mkdir -p out
javac -d out $(find src -name "*.java")


Create the Manifest:
A manifest file tells Java which class contains the main method.

On Linux/macOS:

echo "Main-Class: com.yourname.organizer.FileOrganizer" > manifest.txt


On Windows (PowerShell):

@"
Manifest-Version: 1.0
Main-Class: com.yourname.organizer.FileOrganizer

"@ | Set-Content manifest.txt


Build the JAR:

jar cfm FileOrganizer.jar manifest.txt -C out .


📦 Creating Native Installers (jpackage)

Note: You must compile on the target OS. (Compile on Windows for an .exe, compile on Linux for a .deb). Requires Java 14+.

To distribute this app to users who do not have Java installed, we use the jpackage tool to bundle a mini Java Runtime Environment (JRE) with our application.

Step 1: Workspace Setup

Create a clean directory structure so jpackage doesn't bundle your entire computer:

AppBuilder/
└── input/
    └── FileOrganizer.jar


Step 2: Build the Installer

🐧 On Linux (Generates a .deb package)

Navigate to your AppBuilder directory and run:

jpackage --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer


🪟 On Windows (Generates an .exe or .msi)

Important Prerequisite: Windows requires the WiX Toolset v3.14 to create installers.
If WiX is installed but jpackage fails, add it to your PATH:

# Temporarily add WiX to PATH in PowerShell:
$env:Path += ";C:\Program Files (x86)\WiX Toolset v3.14\bin"

# Verify it works:
where candle
where light


Once WiX is configured, run the packaging command:

jpackage --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer


⚡ Alternative: Windows App-Image (No WiX Required)

If you don't want to install WiX, you can generate a portable "App Image" instead of an installer. This creates a folder containing your .exe and the necessary runtime files.

jpackage --type app-image --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer


You can then zip the resulting FileOrganizer folder and share it directly!
