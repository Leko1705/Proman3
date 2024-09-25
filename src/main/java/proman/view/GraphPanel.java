package proman.view;

import proman.diagrams.graph.edges.Edge;
import proman.diagrams.graph.edges.HeadDefiner;
import proman.diagrams.graph.edges.Path;
import proman.diagrams.graph.edges.PathFactory;
import proman.diagrams.graph.nodes.Node;
import proman.view.listeners.EdgeClickListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GraphPanel extends NavigableMapPanel {

    private final Set<Node> nodes;
    private final Set<Edge> edges;

    private final Set<EdgeClickListener> edgeClickListeners;

    public GraphPanel(boolean canCameraMove, boolean canZoom, boolean canDrag) {
        super(canCameraMove, canZoom, canDrag);
        nodes = new HashSet<>();
        edges = new HashSet<>();
        edgeClickListeners = new HashSet<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Edge edge : edges)
                    handlePotentialEdgeClick(e, edge);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Edge edge: edges)
            paintEdge(g, edge);
    }

    private void paintEdge(Graphics g, Edge edge) {
        Path path = getEdgePath(edge);
        path.draw(g, edge.getType().getStyle());

        HeadDefiner headDefiner = edge.getSourceHeadPainter();
        if (headDefiner != null) {
            Graphics2D g2d = (Graphics2D) g;
            Shape head = headDefiner.paint(edge, path);
            g2d.draw(head);
        }

        headDefiner = edge.getDestinationHeadPainter();
        if (headDefiner != null) {
            Graphics2D g2d = (Graphics2D) g;
            Shape head = headDefiner.paint(edge, path);
            g2d.draw(head);
        }
    }

    private Path getEdgePath(Edge edge) {
        PathFactory factory = edge.getPathFactory();

        if (edge.getSource() == edge.getDestination()){
            return factory.createCyclicPath(edge);
        }
        else {
            return factory.createDirectPath(edge);
        }
    }

    private void handlePotentialEdgeClick(MouseEvent e, Edge edge) {
        Path path = getEdgePath(edge);
        if (path.contains(e.getPoint())){
            for (EdgeClickListener listener : edgeClickListeners)
                listener.edgeClicked(edge, e);
        }
    }

    public void addNode(Node node) {
        Objects.requireNonNull(node);
        nodes.add(node);
        add(node.getView());
        repaint();
    }

    public void removeNode(Node node) {
        Objects.requireNonNull(node);
        nodes.remove(node);
        edges.removeIf(edge -> edge.getSource() == node || edge.getDestination() == node);
        repaint();
    }

    public void addEdge(Edge edge) {
        Objects.requireNonNull(edge);
        edges.add(edge);
        repaint();
    }

    public void removeEdge(Edge edge) {
        Objects.requireNonNull(edge);
        edges.remove(edge);
        repaint();
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void addEdgeClickListener(EdgeClickListener edgeClickListener) {
        Objects.requireNonNull(edgeClickListener);
        edgeClickListeners.add(edgeClickListener);
    }

    public void removeEdgeClickListener(EdgeClickListener edgeClickListener) {
        Objects.requireNonNull(edgeClickListener);
        edgeClickListeners.remove(edgeClickListener);
    }
}
