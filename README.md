<<<<<<< HEAD
🧹 Automated File Organizer
=======
# Automated File Organizer
>>>>>>> 1a79f64 (restructured the readme file)

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
