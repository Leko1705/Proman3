package proman.filesys;

import java.io.File;

public class DirectoryNode implements VirtualFile {

    private final String name;
    private final VirtualFile parent;

    public DirectoryNode(String name, VirtualFile parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return "";
    }

    @Override
    public VirtualFile getParent() {
        return parent;
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
