package proman.diagrams.coordsys;

import proman.diagrams.Diagram;
import proman.diagrams.DiagramType;
import proman.diagrams.DiagramView;
import proman.diagrams.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class CoordinateSystem2D extends JPanel implements Diagram, DiagramView {

    private static final double SENSITIVITY = 3;

    private final int version;
    private final DiagramType type;

    private boolean canZoom = true;
    private boolean canMove = true;

    private Matrix2D base;
    private GutterPainter gutterPainter = null;
    private Supplier<Point2D> originSupplier = () -> new Point2D.Double(getWidth()/2f, getHeight()/2f) ;

    private final double[] bias = {0, 0};
    private double zoomX = 100, zoomY = 100;
    private Point cursorPosition = new Point(getWidth()/2, getHeight()/2);

    public CoordinateSystem2D(int version, DiagramType type) {
        this(version, type, new Matrix2D(1, 0, 0, 1));
    }

    public CoordinateSystem2D(int version, DiagramType type, Matrix2D base){
        this.version = version;
        this.type = Objects.requireNonNull(type);
        this.base = Objects.requireNonNull(base);

        addMouseWheelListener(new ZoomListener());
        addMouseMotionListener(new MouseHandler());
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
    public abstract Iterable<Element> getElements();

    @Override
    public DiagramView getView() {
        return this;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    public void setBase(Matrix2D base) {
        this.base = base;
        repaint();
    }

    public double getZoomX() {
        return zoomX;
    }

    public double getZoomY() {
        return zoomY;
    }

    public void setCanZoom(boolean canZoom) {
        this.canZoom = canZoom;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setGutterPainter(GutterPainter gutterPainter) {
        this.gutterPainter = gutterPainter;
        repaint();
    }

    public void setOriginSupplier(Supplier<Point2D> originSupplier) {
        Objects.requireNonNull(originSupplier);
        this.originSupplier = originSupplier;
        repaint();
    }

    public void setOrigin(Point2D origin) {
        Objects.requireNonNull(origin);
        this.originSupplier = () -> origin;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());

        if (gutterPainter != null) {
            gutterPainter.paint(this);
        }

        Point2D origin = originSupplier.get();

        // apply the zoom
        origin = new Point2D.Double(origin.getX()+bias[0]*zoomX, origin.getY()+bias[1]*zoomY);

        int thickness = 3; // TODO modularize this constant

        g2.fillRect((int) origin.getX(), 0, thickness, getHeight());
        g2.fillRect(0, (int) origin.getY(), getWidth(), thickness);
    }


    private class ZoomListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (!canZoom) return;

            zoomX += (double) e.getUnitsToScroll()/2*-10;
            zoomY += (double) e.getUnitsToScroll()/2*-10;

            if (!(zoomX <= 0) && !(zoomY <= 0) && !(zoomX >= 1000) && !(zoomY >= 1000)) {
                getGraphics().clearRect(0, 0, getWidth(), getHeight());
                repaint();
            }
            else{
                zoomX += (double) e.getUnitsToScroll()/2*10;
                zoomY += (double) e.getUnitsToScroll()/2*10;
            }
        }
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!canMove) return;

            double max = Math.max(getWidth(), getHeight());
            double min = Math.min(getWidth(), getHeight());
            double unitScreenWidth = min + ((max - min) / 2);

            if (e.getX()-cursorPosition.getX() > 0) {
                double dx = unitScreenWidth / zoomX / 1000;
                bias[0] += dx*SENSITIVITY;
            }
            else if (e.getX()-cursorPosition.getX() < 0) {
                double dx = unitScreenWidth / zoomX / 1000;
                bias[0] -= dx*SENSITIVITY;
            }

            if (e.getY()-cursorPosition.getY() > 0) {
                double dy = unitScreenWidth / zoomY / 1000;
                bias[1] += dy*SENSITIVITY;
            }
            else if (e.getY()-cursorPosition.getY() < 0) {
                double dy = unitScreenWidth / zoomY / 1000;
                bias[1] -= dy*SENSITIVITY;
            }

            cursorPosition = e.getPoint();

            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            cursorPosition = e.getPoint();
        }
    }


}
