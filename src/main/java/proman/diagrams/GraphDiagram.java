package proman.diagrams;

import proman.diagrams.graph.edges.Edge;
import proman.diagrams.graph.nodes.Node;

import java.util.ArrayList;

public interface GraphDiagram extends Diagram {

    int getVersion();


    DiagramType getType();

    Iterable<Element> getElements();

    DiagramView getView();


    void addNode(Node node);

    void removeNode(Node node);

    void addEdge(Edge edge);

    void removeEdge(Edge edge);

    Iterable<Node> getNodes();

    Iterable<Edge> getEdges();

}
