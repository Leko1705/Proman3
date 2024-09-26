package proman.filesys;

import java.io.File;

public class RootProjectNode implements VirtualFile, Project {

    @Override
    public VirtualFile getProjectRoot() {
        return this;
    }

    @Override
    public void reloadProject() {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getExtension() {
        return "";
    }

    @Override
    public VirtualFile getParent() {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean create() {
        return new File(getAbsolutePath()).mkdirs();
    }
}
