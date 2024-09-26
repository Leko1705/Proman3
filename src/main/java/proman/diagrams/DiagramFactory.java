package proman.diagrams;

public interface DiagramFactory {

    Diagram createTemplateDiagram();

    default Diagram createDiagram(int version) {
        return createTemplateDiagram();
    }

}
