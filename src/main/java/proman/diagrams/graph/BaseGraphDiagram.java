package proman.diagrams.graph;

import proman.diagrams.DiagramType;
import proman.diagrams.DiagramView;
import proman.diagrams.Element;
import proman.diagrams.GraphDiagram;
import proman.diagrams.graph.edges.Edge;
import proman.diagrams.graph.edges.EdgeType;
import proman.diagrams.graph.nodes.Node;
import proman.diagrams.graph.nodes.PointNode;
import proman.view.GraphPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class BaseGraphDiagram extends GraphPanel implements GraphDiagram, DiagramView {

    private final MousePointNode MOUSE_POINT_END = new MousePointNode();


    private final int version;
    private final DiagramType type;

    private EdgeType<?> initialEdgeType;

    private Edge processedConnection = null;

    private boolean lineClicked = false;


    public BaseGraphDiagram(int version, DiagramType type, EdgeType<?> initialEdgeType) {
        super(true, true, true);
        this.version = version;
        this.type = Objects.requireNonNull(type);
        this.initialEdgeType = Objects.requireNonNull(initialEdgeType);

        addNode(MOUSE_POINT_END);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (!lineClicked)
                    onDiagramClick(e);

                if (processedConnection != null)
                    removeEdge(processedConnection);
                processedConnection = null;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
                lineClicked = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (processedConnection != null) {
                    MOUSE_POINT_END.getView().setLocation(e.getPoint());
                    repaint();
                }
            }
        });

        addEdgeClickListener((edge, e) -> {
            lineClicked = true;
            onEdgeClicked(edge, e);
        });
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public DiagramType getType() {
        return type;
    }

    @Override
    public Iterable<Element> getElements() {
        return new ArrayList<>(getNodes());
    }

    @Override
    public DiagramView getView() {
        return this;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Set<Node> getNodes() {
        return super.getNodes();
    }

    @Override
    public Set<Edge> getEdges() {
        return super.getEdges();
    }

    public void setInitialEdgeType(EdgeType<?> initialEdgeType) {
        this.initialEdgeType = initialEdgeType;
    }


    public void onDiagramClick(MouseEvent e){
    }

    public void onNodeClicked(Node node, MouseEvent e){
    }

    public void onEdgeClicked(Edge edge, MouseEvent e){
    }

    public Edge createNewConnection(Element from, Element to) {
        if (!initialEdgeType.canConnect(from, to))
            return null;
        return initialEdgeType.createEdge(from, to);
    }

    @Override
    public void addNode(Node node) {
        applyComponentSpecs(node);
        super.addNode(node);
    }

    public void startConnectionProcedure(Node source){
        processedConnection = initialEdgeType.createEdge(source, MOUSE_POINT_END);
    }


    private void applyComponentSpecs(Node node) {
        node.getView().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (processedConnection != null){
                    if (SwingUtilities.isLeftMouseButton(e)){
                        removeEdge(processedConnection);
                        processedConnection = null;
                    }
                    else {
                        removeEdge(processedConnection);
                        Element from = processedConnection.getSource();
                        Edge edge = createNewConnection(from, node);
                        if (edge != null) {
                            addEdge(edge);
                        }
                        processedConnection = null;
                    }
                }

                onNodeClicked(node, e);
            }
        });
    }


    public static class MousePointNode extends PointNode { }
}
