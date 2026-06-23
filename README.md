
# Automated File Organizer


A lightweight, cross-platform Java utility that automatically scans your `Downloads` folder and neatly organizes your files into subdirectories (Documents, Images, Archives, etc.) based on their file extensions.

## 🚀 Features

- **Cross-Platform Compatibility**: Works seamlessly on Windows, macOS, and Linux by dynamically resolving the user's home directory.
- **Zero Dependencies**: Built entirely using vanilla Java (`java.nio.file`) without any external library dependencies.
- **Pre-Built Installers**: Available as ready-to-use `.deb` (Linux) and `.exe` (Windows) installers.
- **Intelligent Categorization**: Automatically groups files by their extensions into sensible folders like `Images`, `Documents`, `Installers`, `Archives`, and `Code`.
- **Safe Operations**: Utilizes robust file moving operations to relocate files safely.

## 📥 Installation

You can download the pre-built `.deb` (Linux) or `.exe` (Windows) installers directly from the releases in this repository!

## ⚙️ How It Works

The tool categorizes files based on predefined mappings:

- **Documents**: `.pdf`, `.docx`, `.xlsx`, `.md`, `.txt`
- **Images**: `.jpg`, `.jpeg`, `.png`
- **Archives**: `.zip`
- **Installers**: `.exe`
- **Code**: `.sql`, `.tsx`

When executed, it scans the `Downloads` directory, matches file extensions against the rules, creates any missing destination directories automatically, and physically moves the files to their respective target folders.

## 🛠️ Building from Source

If you want to build this project from source, you can compile it into a Runnable JAR file. Requires **Java 14+**.

### 1. Create an Executable JAR

Run these commands from the root directory of the project:

**Create output directory & compile:**
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

**Create the Manifest:**
A manifest file tells Java which class contains the main method.

On Linux/macOS:
```bash
echo "Main-Class: com.yourname.organizer.FileOrganizer" > manifest.txt
```
On Windows (PowerShell):
```powershell
@"
Manifest-Version: 1.0
Main-Class: com.yourname.organizer.FileOrganizer

"@ | Set-Content manifest.txt
```

**Build the JAR:**
```bash
jar cfm FileOrganizer.jar manifest.txt -C out .
```

### 2. Creating Native Installers (jpackage)

**Note:** You must compile on the target OS (Compile on Windows for an `.exe`, compile on Linux for a `.deb`).

**Step 1: Workspace Setup**
Create a clean directory structure so `jpackage` doesn't bundle your entire computer:
```text
AppBuilder/
└── input/
    └── FileOrganizer.jar
```

**Step 2: Build the Installer**

**🐧 On Linux (Generates a .deb package)**
Navigate to your AppBuilder directory and run:
```bash
jpackage --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer
```

**🪟 On Windows (Generates an .exe or .msi)**
*Important Prerequisite:* Windows requires the WiX Toolset v3.14 to create installers.
If WiX is installed but `jpackage` fails, add it to your PATH:
```powershell
# Temporarily add WiX to PATH in PowerShell:
$env:Path += ";C:\Program Files (x86)\WiX Toolset v3.14\bin"
```
Once WiX is configured, run the packaging command:
```bash
jpackage --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer
```

**⚡ Alternative: Windows App-Image (No WiX Required)**
If you don't want to install WiX, you can generate a portable "App Image" instead of an installer. This creates a folder containing your `.exe` and the necessary runtime files.
```bash
jpackage --type app-image --name FileOrganizer --input input --main-jar FileOrganizer.jar --main-class com.yourname.organizer.FileOrganizer
```
You can then zip the resulting `FileOrganizer` folder and share it directly!




---------------------------------------------------------------------------------

Building the GUI Executable
Step 1: The Blank Window (JFrame)
In Java, a basic desktop window is called a JFrame. Before we add any buttons or logic, let's just tell Java to open an empty 600x400 window on your screen.

In Eclipse, right-click your package (com.yourname.organizer), create a New > Class, and name it GUIFileOrganizer.

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

By default, Java tries to keep projects as tiny as possible. The tools to draw windows (like JFrame and Swing) are stored in a specific module called java.desktop. Because your project doesn't explicitly ask for permission to use that module, Java blocks you from accessing it.

Ask for Permission
If you want to keep the module system active, you have to tell Java you need the desktop tools.

Open the module-info.java file.

Add this exact line inside the curly braces: requires java.desktop;

It should look something like this:
module AutomatedFileOrganizer {
    requires java.desktop;
}


Step 2: The Button and Text Area


Step 3 & 4: Connecting the Logic
We need to add our organizeFiles method to the class and tell the button to call it when clicked.

DRY (Don't Repeat Yourself) and Separation of Concerns. Instead of rewriting the logic, it is much better to have your GUI class call your existing FileOrganizer class.

However, there is one small problem to solve: Your original FileOrganizer uses System.out.println(), which sends text to the hidden console. Your GUI text box has no way to "catch" that text.

To connect them perfectly, you just need to make a tiny tweak to your original FileOrganizer class so it can push text to the GUI.

Here is how you connect them:

1. Update FileOrganizer.java (The Brain)
Change your main method into a public helper method that accepts a JTextArea. Now, instead of printing to the console, it appends text directly to whatever GUI asks for it!

Java
package com.yourname.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea; // Import this!

public class FileOrganizer {
    
    // ... (Keep your FILE_TYPES HashMap and getFileExtension method exactly the same) ...

    // Change "public static void main(String[] args)" to this:
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
2. Update GUIFileOrganizer.java (The Visuals)
Now, your GUI class doesn't need to know anything about HashMaps or Files.move. It just builds the window, and when the button is clicked, it hands the logArea over to FileOrganizer.

Java
package com.yourname.organizer;

import javax.swing.*;
import java.awt.*;

public class GUIFileOrganizer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Magic File Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);

        JButton runButton = new JButton("Clean My Downloads Folder!");
        runButton.setFont(new Font("Arial", Font.BOLD, 16));
        runButton.setPreferredSize(new Dimension(600, 50));

        // THE MAGIC CONNECTION:
        runButton.addActionListener(e -> {
            logArea.setText(""); // Clear the screen
            // Call the other class and pass the logArea to it!
            FileOrganizer.processFiles(logArea); 
        });

        frame.add(runButton, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
This is the exact architecture professional Java developers use. You have one class entirely dedicated to Logic (FileOrganizer), and one class entirely dedicated to User Interface (GUIFileOrganizer). They work together beautifully!