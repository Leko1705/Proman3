package proman.filesys;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface VirtualFile {

    String getName();

    default String getExtension(){
        return getName().substring(getName().lastIndexOf(".") + 1);
    }

    default String getNameWithoutExtension() {
        String nameWithExtension = getName();
        String extension = getExtension();
        int lastPos = nameWithExtension.lastIndexOf(extension);
        return nameWithExtension.substring(0, lastPos);
    }

    default Icon getIcon(){
        return null;
    }

    default String getPath() {
        if (getParent() == null) return "";
        return getParent().getPath() + File.separator + getName();
    }

    default String getAbsolutePath(){
        if (getParent() == null) {
            return FileSystem.getInstallationPath() + File.separator + getName();
        }
        return getParent().getAbsolutePath() + File.separator + getName();
    }

    default String getCanonicalPath(){
        if (getParent() == null) return "";
        return getParent().getPath() + '/' + getName();
    }

    VirtualFile getParent();

    boolean isOpen();

    boolean isDirectory();

    default boolean isReadOnly(){
        return false;
    }

    default Path toNioPath(){
        return Path.of(getAbsolutePath());
    }

    default boolean exists(){
        return Files.exists(Path.of(getAbsolutePath()));
    }

    default boolean create(){
        try {
            return new File(getAbsolutePath()).createNewFile();
        }
        catch (IOException ignored) {
            return false;
        }
    }

}
