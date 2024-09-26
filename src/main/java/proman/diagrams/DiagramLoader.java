package proman.diagrams;

public interface DiagramLoader {

    Diagram loadDiagram(int version, Object context);

}
