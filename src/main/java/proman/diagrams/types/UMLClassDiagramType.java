package proman.diagrams.types;

import proman.diagrams.DiagramFactory;
import proman.diagrams.DiagramLoader;
import proman.diagrams.DiagramType;

public class UMLClassDiagramType implements DiagramType {

    @Override
    public String getName() {
        return "UML Class Diagram";
    }

    @Override
    public DiagramFactory getDiagramFactory() {
        return null;
    }

    @Override
    public DiagramLoader getDiagramLoader() {
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
