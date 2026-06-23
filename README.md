# 🗂️ Automated File Organizer

<div align="center">

![Java](https://img.shields.io/badge/Java-14%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![GUI](https://img.shields.io/badge/UI-Java%20Swing-orange?style=for-the-badge)

**A cross-platform Java desktop application that automatically organizes your files into categorized folders based on file extension rules you define.**

[📥 Download Installer](#-installation) · [🛠️ Build from Source](#️-building-from-source) · [📖 How It Works](#️-how-it-works) · [🤝 Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Screenshots / Preview](#-screenshots--preview)
- [Installation](#-installation)
  - [Linux (.deb)](#-linux-deb-package)
  - [Windows (.exe)](#-windows-exe-installer)
- [Usage](#-usage)
- [How It Works](#️-how-it-works)
  - [Default File Categories](#default-file-categories)
  - [Architecture](#architecture)
- [Building from Source](#️-building-from-source)
  - [Prerequisites](#prerequisites)
  - [Step 1 – Compile the Source](#step-1--compile-the-source)
  - [Step 2 – Package into a JAR](#step-2--package-into-a-jar)
  - [Step 3 – Create Native Installers](#step-3--create-native-installers-optional)
- [Project Structure](#-project-structure)
- [Configuration & Customization](#-configuration--customization)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

---

## 🔍 Overview

**Automated File Organizer** is a lightweight, zero-dependency Java desktop utility with a clean Swing GUI. Point it at any folder (defaults to your `~/Downloads` directory), define custom extension-to-folder rules, and click a button — it will automatically sort every file into its correct subfolder.

The application follows a clean **MVC-inspired separation of concerns**:
- `FileOrganizer.java` — pure file-moving business logic
- `GUIFileOrganizer.java` — all Swing UI and event handling

---

## 🚀 Features

| Feature | Description |
|---|---|
| 🖥️ **Graphical Interface** | Intuitive Java Swing GUI — no terminal required |
| 📁 **Custom Folder Targeting** | Browse and select any folder on your system, not just `Downloads` |
| ➕ **Dynamic Rule Management** | Add custom extension → folder rules at runtime without restarting |
| 🧠 **Intelligent Categorization** | Sensible built-in defaults for Documents, Images, Archives, Code, and more |
| 🔒 **Safe File Operations** | Uses `java.nio.file.Files.move()` with `REPLACE_EXISTING` for reliable moves |
| 📜 **Live Activity Log** | In-app scrollable log shows every move, skip, and error in real time |
| 🌍 **Cross-Platform** | Runs on Windows, Linux, and macOS — resolves `user.home` dynamically |
| 📦 **Zero Dependencies** | Built entirely on the Java standard library (`java.nio`, `javax.swing`) |
| 🏗️ **Native Installers** | Pre-built `.deb` (Ubuntu/Debian) and `.exe` (Windows) packages available |

---

## 🖼️ Screenshots / Preview

> The application opens a 750×500 window with three main areas:
>
> - **Top Panel** — Folder path selector (Browse button) and rule addition fields
> - **Center Panel** — Scrollable live log showing active rules and file operation results
> - **Bottom Panel** — Large "Organize Selected Folder!" action button

```
┌──────────────────────────────────────────────────┐
│  Target Folder: [/home/user/Downloads      ] [Browse]  │
│  Extension: [txt] Move to Folder: [TextFiles] [Add Rule]│
├──────────────────────────────────────────────────┤
│ Logs & Active Rules                               │
│  Active Rules Loaded:                             │
│    .pdf -> [Documents]                            │
│    .jpg -> [Images]                               │
│    .png -> [Images]                               │
│    .zip -> [Archives]                             │
│  --------------------------------------------------│
│  🚀 Starting cleanup in: /home/user/Downloads     │
│  ✅ Moved: resume.pdf -> Documents                │
│  ✅ Moved: photo.jpg -> Images                    │
│  ⚠️  Ignored: unknown.xyz (No rule for xyz)       │
│  Done! Check your folder.                         │
├──────────────────────────────────────────────────┤
│         [ Organize Selected Folder! ]              │
└──────────────────────────────────────────────────┘
```

---

## 📥 Installation

### 🐧 Linux (.deb Package)

Download the pre-built Debian package and install with `dpkg`:

```bash
# Download from the Releases section of this repository
sudo dpkg -i fileorganizer_1.0_amd64.deb
```

After installation, launch it from your app menu by searching **"FileOrganizer"** or **"Dynamic File Organizer"**, or run it from the terminal:

```bash
/opt/dynamicfileorganizer/bin/DynamicFileOrganizer
```

> **Tip:** If no app menu entry appears, the installer was built without the `--linux-shortcut` flag. See [Troubleshooting](#-troubleshooting) for how to create a shortcut manually.

---

### 🪟 Windows (.exe Installer)

Download `FileOrganizer-1.0.exe` from the Releases section and run it as a standard Windows installer. It will install the application and set up a Start Menu entry automatically.

---

### ☕ Running from JAR (Cross-Platform)

If you have Java 14+ installed, you can run the prebuilt JAR directly without installing anything:

```bash
java -jar FileOrganizer.jar
```

---

## 📖 Usage

1. **Launch** the application using one of the methods above.
2. **Select a Target Folder** — click **Browse…** to choose any folder on your system. It defaults to `~/Downloads`.
3. **Review Active Rules** — the log panel shows pre-loaded rules on startup.
4. **Add Custom Rules** (optional) — enter a file extension (e.g., `txt`) and a destination folder name (e.g., `TextFiles`), then click **Add Rule**.
5. **Organize!** — click **"Organize Selected Folder!"** and watch the live log as files are moved to their categorized subfolders.

> **Note:** Only files in the *root* of the selected folder are processed. Files inside existing subdirectories are left untouched.

---

## ⚙️ How It Works

### Default File Categories

When the application starts, the following built-in rules are active:

| Extension | Destination Folder |
|---|---|
| `.pdf`, `.docx`, `.xlsx`, `.md` | `Documents` |
| `.jpg`, `.jpeg`, `.png` | `Images` |
| `.zip` | `Archives` |
| `.exe` | `Installers` |
| `.sql`, `.tsx` | `Code` |

> **These rules can be extended at runtime** via the "Add Rule" panel without restarting. Rules are stored in a `HashMap<String, String>` and applied immediately to the next run.

### How Files Are Moved

For each file in the target folder:

1. Extract the file extension (lowercased).
2. Look up the extension in the active rules map.
3. If a rule matches:
   - Create the destination subdirectory inside the target folder (if it doesn't already exist).
   - Move the file using `Files.move(..., StandardCopyOption.REPLACE_EXISTING)`.
   - Log a ✅ success message.
4. If no rule matches, log a ⚠️ ignored message and leave the file in place.
5. If an error occurs (e.g., permission denied), log a ❌ error message and continue.

---

### Architecture

The application uses a **two-class architecture** with clean separation of responsibilities:

```
src/
└── com/yourname/organizer/
    ├── FileOrganizer.java       ← Business Logic (file moving engine)
    └── GUIFileOrganizer.java    ← Presentation Layer (Swing UI + event handling)
```

```
┌─────────────────────────────────────────────────────────┐
│                  GUIFileOrganizer.java                   │
│                                                          │
│  State: selectedDirectory, fileTypes (HashMap)           │
│  Layout: JFrame → configPanel, scrollPane, runButton     │
│  Events: browseButton, addRuleButton, runButton          │
│                           │                             │
│           on button click │                             │
│                           ▼                             │
│         FileOrganizer.processFiles(                     │
│             logArea, selectedDirectory, fileTypes)       │
└─────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                   FileOrganizer.java                     │
│                                                          │
│  Input:  JTextArea (for logging), Path, Map<ext, folder> │
│  Action: Iterates files, matches extensions, moves files │
│  Output: Appends status lines to the provided JTextArea  │
└─────────────────────────────────────────────────────────┘
```

**Why this design?**
- `FileOrganizer` has zero knowledge of buttons, windows, or UI components.
- `GUIFileOrganizer` has zero file I/O logic — it only manages state and hands it off.
- This makes the logic layer independently testable and reusable.

---

## 🛠️ Building from Source

### Prerequisites

| Requirement | Version |
|---|---|
| Java Development Kit (JDK) | **14 or higher** |
| Eclipse IDE *(optional)* | Any modern version |
| WiX Toolset *(Windows installers only)* | v3.14 |

---

### Step 1 – Compile the Source

From the **project root directory**, compile all Java source files into the `out/` directory:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

**On Windows (PowerShell):**
```powershell
New-Item -ItemType Directory -Force -Path out
Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName } | javac -d out @args
```

---

### Step 2 – Package into a JAR

**Create or update the manifest** to declare the entry point:

*Linux/macOS:*
```bash
echo "Main-Class: com.yourname.organizer.GUIFileOrganizer" > manifest.txt
```

*Windows (PowerShell):*
```powershell
@"
Manifest-Version: 1.0
Main-Class: com.yourname.organizer.GUIFileOrganizer

"@ | Set-Content manifest.txt
```

**Bundle into a runnable JAR:**
```bash
jar cfm FileOrganizer.jar manifest.txt -C out .
```

**Run the JAR to verify:**
```bash
java -jar FileOrganizer.jar
```

---

### Step 3 – Create Native Installers (Optional)

`jpackage` (bundled with JDK 14+) creates platform-native installers that include the Java runtime — no Java installation required on the target machine.

> ⚠️ **Important:** You must compile the installer **on the target OS**. Build on Linux to get a `.deb`, build on Windows to get an `.exe`.

**Setup the AppBuilder directory:**
```
AppBuilder/
└── input/
    └── FileOrganizer.jar    ← Copy your JAR here
```

```bash
mkdir -p AppBuilder/input
cp FileOrganizer.jar AppBuilder/input/
cd AppBuilder
```

---

#### 🐧 Linux — Generate a `.deb` Package

```bash
jpackage \
  --name DynamicFileOrganizer \
  --input input \
  --main-jar FileOrganizer.jar \
  --main-class com.yourname.organizer.GUIFileOrganizer \
  --linux-shortcut \
  --linux-app-category Utility
```

> The `--linux-shortcut` flag creates an app menu entry so the application appears in your Ubuntu/GNOME applications grid.

Install the generated package:
```bash
sudo dpkg -i dynamicfileorganizer_1.0_amd64.deb
```

---

#### 🪟 Windows — Generate a `.exe` / `.msi` Installer

**Prerequisite:** Install [WiX Toolset v3.14](https://github.com/wixtoolset/wix3/releases) and add it to your PATH:

```powershell
# Temporarily add WiX to PATH
$env:Path += ";C:\Program Files (x86)\WiX Toolset v3.14\bin"
```

Then run:
```powershell
jpackage `
  --name FileOrganizer `
  --input input `
  --main-jar FileOrganizer.jar `
  --main-class com.yourname.organizer.GUIFileOrganizer
```

**Alternative: Portable App-Image (No WiX Required)**

If you don't want to install WiX, generate a portable folder instead:
```bash
jpackage \
  --type app-image \
  --name FileOrganizer \
  --input input \
  --main-jar FileOrganizer.jar \
  --main-class com.yourname.organizer.GUIFileOrganizer
```
This produces a `FileOrganizer/` folder containing the `.exe` and Java runtime. Zip it and share directly — no installation needed!

---

## 📁 Project Structure

```
automated-file-organizer/
│
├── src/                                    # Java source code
│   ├── module-info.java                    # Java module declaration (requires java.desktop)
│   └── com/yourname/organizer/
│       ├── FileOrganizer.java              # Business logic: file-moving engine
│       └── GUIFileOrganizer.java           # Swing GUI: window, panels, event listeners
│
├── AppBuilder/                             # jpackage workspace (git-ignored)
│   └── input/
│       └── FileOrganizer.jar              # JAR input for jpackage
│
├── out/                                   # Compiled .class files (git-ignored)
├── bin/                                   # Eclipse build output (git-ignored)
│
├── FileOrganizer.jar                      # Pre-built runnable JAR
├── FileOrganizer-1.0.exe                  # Windows installer
├── fileorganizer_1.0_amd64.deb           # Debian/Ubuntu installer
│
├── manifest.txt                           # JAR manifest (git-ignored)
├── .gitignore
└── README.md
```

---

## ⚙️ Configuration & Customization

### Adding Rules at Runtime

In the running application:
1. Type the file extension (without the dot) in the **"File Extension"** field, e.g. `mp4`
2. Type the destination folder name in the **"Move to Folder"** field, e.g. `Videos`
3. Click **Add Rule**

The new rule is active immediately for the next run.

### Modifying Built-in Default Rules

To change the default rules that load at startup, edit the `static` block in [`FileOrganizer.java`](src/com/yourname/organizer/FileOrganizer.java) and the constructor of [`GUIFileOrganizer.java`](src/com/yourname/organizer/GUIFileOrganizer.java):

```java
// In FileOrganizer.java static block:
FILE_TYPES.put("mp4", "Videos");
FILE_TYPES.put("mp3", "Music");
FILE_TYPES.put("txt", "TextFiles");

// In GUIFileOrganizer constructor (for GUI display on startup):
fileTypes.put("mp4", "Videos");
fileTypes.put("mp3", "Music");
```

### module-info.java

The project uses the Java Platform Module System (JPMS). The module declaration grants access to the `java.desktop` module (required by Swing):

```java
module AutomatedFileOrganizer {
    requires java.desktop;
}
```

---

## 🩺 Troubleshooting

### App not appearing in Ubuntu app menu after `.deb` install

The `.deb` was built without the `--linux-shortcut` flag. Create a desktop entry manually:

```bash
sudo nano /usr/share/applications/fileorganizer.desktop
```

Paste:
```ini
[Desktop Entry]
Name=Dynamic File Organizer
Exec=/opt/dynamicfileorganizer/bin/DynamicFileOrganizer
Icon=fileorganizer
Type=Application
Categories=Utility;
```

Then refresh:
```bash
update-desktop-database ~/.local/share/applications/
```

---

### "No such file or directory" when running the installed app

Linux is case-sensitive. The executable name matches the `--name` flag used during packaging exactly. Use tab-completion to find the exact path:

```bash
ls /opt/                          # Find the install folder name
ls /opt/<foldername>/bin/         # Find the exact executable name
/opt/<foldername>/bin/<ExeName>   # Run it
```

---

### `jpackage` fails on Windows

Ensure WiX Toolset v3.14 is installed and its `bin/` directory is on your PATH. Alternatively, use `--type app-image` to skip the installer and produce a portable folder instead.

---

### `java.awt.HeadlessException` on Linux servers

The application requires a graphical display environment. It is not designed to run in headless/SSH-only environments. If you need headless operation, use the original `FileOrganizer` command-line logic (refactored separately from the GUI class).

---

## 🤝 Contributing

Contributions are welcome! Here are some ideas for improvements:

- [ ] Persist custom rules to a JSON/properties config file
- [ ] Add a "Preview Mode" that shows what would be moved without actually moving files
- [ ] Support recursive organization of subdirectories
- [ ] Add an undo feature to reverse the last run
- [ ] Add system tray icon and minimize-to-tray support
- [ ] Internationalization (i18n) for multiple languages

**To contribute:**
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📄 License

This project is open source. See the repository for license details.

---

<div align="center">

Built with ☕ Java and ❤️ — a practical tool for keeping your Downloads folder tidy.

</div>
