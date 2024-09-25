package proman.view;

import proman.threading.BGT;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LazyContextMenuBuilder implements ContextMenuBuilder {

    private final Component component;

    private static final ConstructBuilder SEPARATOR_CONSTRUCT = JPopupMenu::addSeparator;;

    private final List<ConstructBuilder> structure = new ArrayList<>();

    public LazyContextMenuBuilder(Component component) {
        Objects.requireNonNull(component);
        this.component = component;
    }

    @Override
    public ContextMenuBuilder addAction(AnAction action) {
        Objects.requireNonNull(action);
        structure.add(new ActionConstruct(action));
        return this;
    }

    @Override
    public ContextMenuBuilder addSeparator() {
        structure.add(SEPARATOR_CONSTRUCT);
        return this;
    }

    @Override
    public void show(int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        for (ConstructBuilder structure : structure) {
            structure.build(popupMenu);
        }
        popupMenu.show(component, x, y);
    }


    interface ConstructBuilder {
        void build(JPopupMenu menu);
    }


    private record ActionConstruct(AnAction action) implements ConstructBuilder {

        @Override
        public void build(JPopupMenu menu) {
            JMenuItem item = new JMenuItem(action.getText());
            if (action.getIcon() != null)
                item.setIcon(action.getIcon());
            item.addActionListener(e -> BGT.invokeLater(action::onAction));
            menu.add(item);
        }
    }

}

