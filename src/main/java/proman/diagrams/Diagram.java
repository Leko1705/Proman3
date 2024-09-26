package proman.diagrams;

public interface Diagram {

    int getVersion();

    DiagramType getType();

    Iterable<Element> getElements();

    DiagramView getView();

}
