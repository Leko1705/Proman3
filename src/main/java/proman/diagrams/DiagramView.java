package proman.diagrams;

import proman.view.ContextMenuBuilder;
import proman.view.LazyContextMenuBuilder;

import java.awt.*;

public interface DiagramView {

    Component getComponent();

    default ContextMenuBuilder getContextMenuBuilder(){
        return new LazyContextMenuBuilder(getComponent());
    }

}
