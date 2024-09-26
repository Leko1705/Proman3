package proman.diagrams.graph.nodes;

import proman.editing.EditorManager;
import proman.view.style.*;

import javax.swing.*;

public interface NodeType<T extends Node> {

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

    default Styles getStyle(){
        return Styles.DEFAULT;
    }

    T createNode();

}
