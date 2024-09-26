package proman.diagrams.graph.edges;

import proman.diagrams.Element;
import proman.view.style.LineStyle;
import proman.utils.SwingUtils;

import java.awt.*;
import java.awt.geom.Point2D;

public class ShortestStraightPathFactory implements PathFactory {

    @Override
    public Path createDirectPath(Edge edge) {
        return new StraightPath(edge);
    }

    @Override
    public Path createCyclicPath(Edge edge) {
        return new CyclicPath(edge);
    }


    private static class StraightPath implements Path {

        private final Point p, q;

        private StraightPath(Edge edge) {
            Element from = edge.getSource();
            Element to = edge.getSource();

            Rectangle fromBounds = from.getView().getBounds();
            Rectangle toBounds = to.getView().getBounds();

            this.p = SwingUtils.closestPointOnRectTo(fromBounds, toBounds);
            this.q = SwingUtils.closestPointOnRectTo(toBounds, fromBounds);

        }

        @Override
        public boolean contains(Point2D point) {
            return SwingUtils.createClickableLine(p, q).contains(point);
        }

        @Override
        public void draw(Graphics g, LineStyle style) {
            g.setColor(style.getColor());
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setStroke(new BasicStroke(style.getThickness()));
            g2d.drawLine(p.x, p.y, q.x, q.y);
        }

        @Override
        public double getSourceAngle() {
            return SwingUtils.angle(p, q);
        }

        @Override
        public double getTargetAngle() {
            return SwingUtils.angle(p, q);
        }

    }

    private static class CyclicPath implements Path {

        Shape[] outlines = new Rectangle[4];
        Point2D[] points = new Point2D[5];

        public CyclicPath(Edge edge) {
            Element from = edge.getSource();

            Rectangle fromBounds = from.getView().getBounds();

            Point point = SwingUtils.getCenter(fromBounds);

            point.translate(0, fromBounds.height/2);
            move(-1, point.x, point.y);

            point.translate(0, fromBounds.height/2);
            move(0, point.x, point.y);

            point.translate(fromBounds.width, 0);
            move(1, point.x, point.y);

            point.translate(0, -fromBounds.height);
            move(2, point.x, point.y);

            point.translate(-fromBounds.width/2, 0);
            move(3, point.x, point.y);
        }

        private void move(int pos, int x, int y) {
            points[pos] = new Point2D.Double(x, y);
            if (pos == 0) return;

            Point2D last = points[pos - 1];
            Point2D current = new Point2D.Double(x, y);

            outlines[pos - 1] = SwingUtils.createClickableLine(last, current);
        }

        @Override
        public boolean contains(Point2D point) {
            for (Shape outline : outlines) {
                if (outline.contains(point)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void draw(Graphics g, LineStyle style) {
            g.setColor(style.getColor());
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setStroke(new BasicStroke(style.getThickness()));

            for (int i = 1; i < points.length; i++) {
                Point2D last = points[i - 1];
                Point2D current = points[i];
                g2d.drawLine(
                        (int) last.getX(),
                        (int) last.getY(),
                        (int) current.getX(),
                        (int) current.getY());
            }
        }

        @Override
        public double getSourceAngle() {
            return 270;
        }

        @Override
        public double getTargetAngle() {
            return 0;
        }
    }


}
