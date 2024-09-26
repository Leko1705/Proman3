package proman.view.listeners;

import proman.diagrams.graph.edges.Edge;

import java.awt.event.MouseEvent;

public interface EdgeClickListener {

    void edgeClicked(Edge edge, MouseEvent e);

}
