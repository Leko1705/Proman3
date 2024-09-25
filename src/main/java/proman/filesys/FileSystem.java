package proman.filesys;

import proman.application.ApplicationManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class FileSystem {

    private static final boolean DEBUG = true;

    private static String installationPath = null;

    private FileSystem() {
    }


    public static String getInstallationPath(){

        if (DEBUG)
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources";

        if (installationPath == null) {
            installationPath = getInstallationPathImpl();
        }
        createIfNotExists(installationPath);
        return installationPath;
    }

    public static String getProjectAreaPath(){
        // TODO return path where all projects are stored.
        throw new AbstractMethodError("not implemented yet");
    }


    public VirtualFile getOrCreateChildFile(VirtualFile file, String childName){
        if (!file.isDirectory()){
            throw new IllegalArgumentException("The given file is not a directory");
        }
        return new SimpleFileNode(childName, file);
    }

    public VirtualFile getOrCreateChildDirectory(VirtualFile file, String childName){
        if (!file.isDirectory()){
            throw new IllegalArgumentException("The given file is not a directory");
        }
        return new DirectoryNode(childName, file);
    }



    private static String getInstallationPathImpl(){
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        Path appDataDirectory;

        String appName = ApplicationManager.getApplicationName();

        if (os.contains("win")) {
            // Windows: Use AppData (either Roaming or Local)
            String appData = System.getenv("APPDATA"); // Roaming
            if (appData == null) {
                appData = System.getenv("LOCALAPPDATA"); // Fallback to Local if Roaming isn't available
            }
            appDataDirectory = Paths.get(appData, appName);
        }
        else if (os.contains("mac")) {
            // macOS: Use ~/Library/Application Support
            appDataDirectory = Paths.get(System.getProperty("user.home"), "Library", "Application Support", appName);
        }
        else if (os.contains("nix") || os.contains("nux")) {
            // Linux: Use ~/.config or ~/.local/share
            appDataDirectory = Paths.get(System.getProperty("user.home"), ".config", appName);
            if (!Files.exists(appDataDirectory)) {
                appDataDirectory = Paths.get(System.getProperty("user.home"), ".local", "share", appName);
            }
        }
        else {
            throw new UnsupportedOperationException("Unknown operating system: " + os);
        }

        // Create the directory if it doesn't exist
        return appDataDirectory.toString();
    }

    private static void createIfNotExists(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
