package proman.editing;

public interface EditorManager<T> {

    Editor<T>[] getEditors();

    Editor<T> getCurrentEditor();

}
