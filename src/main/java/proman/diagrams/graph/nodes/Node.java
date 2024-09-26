package proman.diagrams.graph.nodes;

import proman.diagrams.Element;

import java.awt.*;

public interface Node extends Element {

    NodeType<?> getType();

    Component getView();

    Node copy();

}
