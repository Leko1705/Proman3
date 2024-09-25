package proman.view;

public interface ContextMenuBuilder {

    ContextMenuBuilder addAction(AnAction action);

    ContextMenuBuilder addSeparator();

    void show(int x, int y);

}
