package proman.filesys;

public interface Project {

    String getName();

    VirtualFile getProjectRoot();

    void reloadProject();

}
