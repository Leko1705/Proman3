package proman.diagrams.graph.edges;

public interface PathFactory {

    Path createDirectPath(Edge edge);

    Path createCyclicPath(Edge edge);

}
