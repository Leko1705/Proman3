package proman.diagrams.graph.edges;

import proman.diagrams.Element;
import proman.editing.EditorManager;
import proman.view.style.LineStyle;
import proman.view.style.Styles;

import javax.swing.*;

public interface EdgeType<T extends Edge> {

    String getName();

    default Icon getIcon(){
        return null;
    }

    default String getDescription(){
        return null;
    }

    default EditorManager<T> getEditorManager(){
        return null;
    }

    default LineStyle getStyle(){
        return Styles.DEFAULT.getLineStyle();
    }

    default boolean canConnect(Element source, Element target){
        return true;
    }

    T createEdge(Element source, Element destination);

}
