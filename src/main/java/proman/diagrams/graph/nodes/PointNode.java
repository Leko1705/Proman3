package proman.diagrams.graph.nodes;

import proman.diagrams.Element;

import javax.swing.*;
import java.awt.*;

public class PointNode implements Node {

    public static final NodeType<?> TYPE = new Type();


    private final PointView fakeView = new PointView();


    @Override
    public NodeType<?> getType() {
        return TYPE;
    }

    @Override
    public Component getView() {
        return fakeView;
    }

    @Override
    public Node copy() {
        return new PointNode();
    }



    private static class Type implements NodeType<PointNode> {

        @Override
        public String getName() {
            return "point";
        }

        @Override
        public PointNode createNode() {
            return new PointNode();
        }
    }



    private static class PointView extends JComponent {

        private static final Dimension SIZE = new Dimension(1, 1);

        @Override
        protected void paintComponent(Graphics g) {
            // points are not visible
        }

        @Override
        public Dimension getSize(Dimension rv) {
            return SIZE;
        }

        @Override
        public Dimension getMaximumSize() {
            return SIZE;
        }

        @Override
        public Dimension getSize() {
            return SIZE;
        }

        @Override
        public Dimension getMinimumSize() {
            return SIZE;
        }

        @Override
        public Dimension getPreferredSize() {
            return SIZE;
        }
    }
}
