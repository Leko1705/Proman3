package proman.diagrams;

import javax.swing.*;

public interface DiagramType {

    String getName();

    default Icon getIcon(){
        return null;
    }

    default String getDescription(){
        return null;
    }

    DiagramFactory getDiagramFactory();

    DiagramLoader getDiagramLoader();

    String toString();

}
