package proman.filesys;

public class SimpleFileNode implements VirtualFile {

    private final String name;
    private final VirtualFile parent;

    public SimpleFileNode(String name, VirtualFile parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
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
        return false;
    }
}
