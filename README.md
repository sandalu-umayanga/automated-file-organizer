"Phase 1"

Important: Look at line 8. You must change "C:/Users/YourName/Downloads" to a real path on your computer. If you are on a Mac, it will look like "/Users/YourName/Downloads".

If everything worked, you should see a printed list of all the files sitting inside your Downloads folder!

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOrganizer {

    public static void main(String[] args) {
        // 1. Point the program at your folder (UPDATE THIS PATH!)
        Path sourceDirectory = Paths.get("C:/Users/YourName/Downloads");
        
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


Cross-platform version 

For same code to work on both Windows and Linux without modification:

Path sourceDirectory = Paths.get(
    System.getProperty("user.home"),
    "Downloads"
);

This resolves to:

Windows → C:\Users\<username>\Downloads
Linux → /home/<username>/Downloads




============================================================================



Phase 2: The Slicer and The HashMap.


In this phase, still not moving any files. Just going to teach the program to figure out what type of file it is looking at, check the rules, and print out where that file should go.


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

==============================================================================================


Phase 3: Actually Moving the Files.


expanded the hashmap to add more file extensions

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
        
        
        System.out.println("Cleaning up folder...\n");
        
        

        // 3. THE MATCHMAKER: Loop through files and check the rules
        for (File file : listOfFiles) {
            if (file.isFile()) {
                // Slice off the extension
                String extension = getFileExtension(file);
                
                // Ask the HashMap: "Do you know what to do with this?"
                if (FILE_TYPES.containsKey(extension)) {
                    String targetFolder = FILE_TYPES.get(extension);
                    
                // Define where the folder should be
                    Path targetDir = sourceDirectory.resolve(targetFolder);
                    
                    try {
                        // Create the "Documents" or "Images" folder if it doesn't exist
                        if (!Files.exists(targetDir)) {
                            Files.createDirectories(targetDir);
                        }
                        
                        // Define the final destination for the file
                        Path targetPath = targetDir.resolve(file.getName());
                        
                        // MOVE THE FILE!
                        Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("✅ Moved: " + file.getName() + " -> " + targetFolder);
                        
                    } catch (IOException e) {
                        System.out.println("❌ Failed to move: " + file.getName());
                    }
                    
                } else {
                    System.out.println("❌ Found '" + file.getName() + "'. I don't have a rule for this extension (" + extension + "). Ignoring.");
                }
            }
        }
    }
}

=============================================================================================================
convert this in to executable file in windows or linux machine without java installed or installed


create an executable JAR

in project root
Create an output directory:

mkdir -p out


Compile all .java files  (run from project root):

javac -d out $(find src -name "*.java")

Create the manifest:
echo "Main-Class: com.yourname.organizer.FileOrganizer" > manifest.txt         ===> on linux

on windows
@"
Manifest-Version: 1.0
Main-Class: com.yourname.organizer.FileOrganizer

"@ | Set-Content manifest.txt


A manifest file tells Java information about the JAR file.

Without a manifest, Java sees the JAR as just a collection of .class files and doesn't know which class to


Build the JAR:
jar cfm FileOrganizer.jar manifest.txt -C out .      ==> linux
--------------------------------------------------------------------
Important Concept: You Must Compile on the Target OS

Step 1: Verify your Java Version
jpackage is included automatically in Java 14 and newer. Let's make sure your computer is ready.
Open your terminal (Ubuntu) or Command Prompt (Windows) and type:

jpackage --version


Step 2: Set up your workspace (Crucial)
jpackage will bundle everything it finds in your input folder into the final installer. To prevent it from accidentally bundling your whole computer, we need a clean workspace.

create a new folder called AppBuilder.

Inside AppBuilder, create another folder called input.

Copy your exported FileOrganizer.jar file and paste it inside the input folder.

Navigate to your AppBuilder folder using terminal


Run this exact command (already  filled in  specific package and class names based on the code if you have any changes change them):
jpackage --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer    ===>  on linux

on Windows
Configure WiX

WiX Toolset v3.14 is already installed.

The issue is that the WiX bin directory is not on your PATH, so jpackage cannot find:

candle.exe
light.exe
Step 1: Locate WiX executables

Run:

where candle
where light

If they are not found, check:

dir "C:\Program Files (x86)\WiX Toolset v3.14\bin"

You should see:

candle.exe
light.exe
heat.exe
Step 2: Add WiX to PATH

Temporarily for the current PowerShell session:

$env:Path += ";C:\Program Files (x86)\WiX Toolset v3.14\bin"

Verify:

candle -?
light -?

Both commands should display help information.

Step 3: Permanently add WiX to PATH

Add:

C:\Program Files (x86)\WiX Toolset v3.14\bin

to the Windows System PATH via:

System Properties
→ Advanced
→ Environment Variables
→ Path
→ Edit
→ New

Then open a new PowerShell window.

Verify:

where candle
where light

Expected output:

C:\Program Files (x86)\WiX Toolset v3.14\bin\candle.exe
C:\Program Files (x86)\WiX Toolset v3.14\bin\light.exe
Step 4: Build the installer

Navigate to your AppBuilder directory and run:

jpackage --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer

If WiX is correctly configured, jpackage should generate a Windows installer (.exe or .msi depending on the JDK version and defaults).

Option 2: Create an application image only

If you just want a runnable Windows application and not an installer:

jpackage `
  --type app-image `
  --name FileOrganizer `
  --input input `
  --main-jar FileOrganizer.jar `
  --main-class com.yourname.organizer.FileOrganizer

This does not require WiX.

It will create a folder containing:

FileOrganizer/
├── FileOrganizer.exe
├── app/
└── runtime/

You can run FileOrganizer.exe directly.

Check available package types

Run:

jpackage --help

Look for:

Valid package types:
app-image
exe
msi

If app-image works, then your Java setup and JAR are fine; only WiX is missing.

For a student project, app-image is often enough. If you specifically need a Windows installer (.exe or .msi), install WiX and rerun jpackage.




Step 4: Test your new Native App!
Once the command finishes, look inside your AppBuilder folder.

If you ran this on Windows: You will see a FileOrganizer-1.0.exe or FileOrganizer-1.0.msi installer.

If you ran this on Ubuntu: You will see a fileorganizer_1.0-1_amd64.deb installer.


you can see the .deb installer in the repo also you can just download it and use it
