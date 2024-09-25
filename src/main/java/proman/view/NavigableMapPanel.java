package proman.view;

import proman.view.listeners.ZoomChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NavigableMapPanel extends JPanel {

    private Point mousePos;

    private final ComponentDragListener dragListener = new ComponentDragListener();

    private boolean cameraCanMove;
    private boolean canZoom;
    private boolean canDrag;

    private double maxZoom = 2;
    private double minZoom = 0.1;

    private double zoomFactor = 1;
    private int zoomPointX;
    private int zoomPointY;
    private final AffineTransform zoomer = new AffineTransform();

    private final Set<ZoomChangeListener> zoomChangeListeners = new HashSet<>();

    public NavigableMapPanel(boolean cameraCanMove, boolean canZoom, boolean canDrag) {
        this.cameraCanMove = cameraCanMove;
        this.canZoom = canZoom;
        this.canDrag = canDrag;
        CameraMoveListener listener = new CameraMoveListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        ZoomListener zoomListener = new ZoomListener();
        addMouseWheelListener(zoomListener);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control R"), "zoom_reset");
        getActionMap().put("zoom_reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomFactor = 1;
                repaint();
            }
        });
    }

    public NavigableMapPanel(){
        this(true, false, false);
    }

    public void setCameraCanMove(boolean cameraCanMove) {
        this.cameraCanMove = cameraCanMove;
    }

    public boolean cameraCanMove() {
        return cameraCanMove;
    }

    public void setCanZoom(boolean canZoom) {
        this.canZoom = canZoom;
    }

    public boolean canZoom() {
        return canZoom;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public boolean canDrag() {
        return canDrag;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public double getMaxZoom() {
        return maxZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public double getMinZoom() {
        return minZoom;
    }

    @Override
    public Component add(Component comp) {
        setListeners(comp);
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        setListeners(comp);
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        setListeners(comp);
        return super.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        setListeners(comp);
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        setListeners(comp);
        super.add(comp, constraints, index);
    }

    public void add(Component comp, boolean draggable){
        if (draggable)
            setListeners(comp);
        super.add(comp);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (canZoom) {
            applyZoom(g);
        }
        super.paintComponent(g);
    }

    private void applyZoom(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        zoomer.setToIdentity();
        zoomer.translate(zoomPointX, zoomPointY);
        zoomer.scale(zoomFactor, zoomFactor);
        zoomer.translate(-zoomPointX, -zoomPointY);
        g2.transform(zoomer);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(null);
    }

    private void setListeners(Component component){
        transformInverse(component.getLocation());
        component.addMouseListener(dragListener);
        component.addMouseMotionListener(dragListener);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        transformInverse(e.getLocationOnScreen());
        super.processMouseEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        transformInverse(e.getLocationOnScreen());
        super.processMouseMotionEvent(e);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        transformInverse(e.getLocationOnScreen());
        super.processMouseWheelEvent(e);
    }

    public void addZoomChangeListener(ZoomChangeListener listener) {
        Objects.requireNonNull(listener);
        zoomChangeListeners.add(listener);
    }

    public void removeZoomChangeListener(ZoomChangeListener listener){
        zoomChangeListeners.remove(listener);
    }

    public double getZoom(){
        return zoomFactor;
    }



    private class ComponentDragListener extends MouseAdapter {

        private Point initialComponentLocation;

        @Override
        public void mousePressed(MouseEvent e) {
            if (!canDrag) return;
            mousePos = e.getLocationOnScreen();
            transformInverse(mousePos);
            initialComponentLocation = e.getComponent().getLocation();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!canDrag) return;
            Point finalMouseLocation = e.getLocationOnScreen();
            transformInverse(finalMouseLocation);
            int deltaX = finalMouseLocation.x - mousePos.x;
            int deltaY = finalMouseLocation.y - mousePos.y;
            Point newLocation = new Point(initialComponentLocation.x + deltaX, initialComponentLocation.y + deltaY);
            e.getComponent().setLocation(newLocation);
            repaint();
        }
    }





    private class CameraMoveListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            mousePos = e.getLocationOnScreen();
            transformInverse(mousePos);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePos = e.getLocationOnScreen();
            transformInverse(mousePos);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mousePos = e.getLocationOnScreen();
            transformInverse(mousePos);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (cameraCanMove) {

                Point finalMouseLocation = e.getLocationOnScreen();
                transformInverse(finalMouseLocation);

                for (Component c : getComponents()) {
                    int deltaX = finalMouseLocation.x - mousePos.x;
                    int deltaY = finalMouseLocation.y - mousePos.y;
                    Point newLocation = new Point(c.getX() + deltaX, c.getY() + deltaY);
                    c.setLocation(newLocation);
                }

                zoomPointX = e.getX();
                zoomPointY = e.getY();
                mousePos = e.getLocationOnScreen();
                transformInverse(mousePos);
                repaint();
            }
        }
    }




    private class ZoomListener implements MouseWheelListener {

        private static final double ZOOM_INCREMENT = 0.05;

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            zoomPointX = e.getX();
            zoomPointY = e.getY();
            mousePos = e.getLocationOnScreen();
            transformInverse(mousePos);

            //Zoom in
            if (e.getWheelRotation() < 0) {
                zoomFactor += ZOOM_INCREMENT;
                if (zoomFactor >= maxZoom)
                    zoomFactor = maxZoom;
                notifyZoomChanged();
                repaint();
            }

            //Zoom out
            if (e.getWheelRotation() > 0) {
                zoomFactor -= ZOOM_INCREMENT;
                if (zoomFactor <= minZoom)
                    zoomFactor = minZoom;
                notifyZoomChanged();
                repaint();
            }

        }
    }




    private void transformInverse(Point point) {
        if (zoomer == null) return;
        try {
            zoomer.inverseTransform(point, point);
        }
        catch (Exception ignored) {}
    }

    private void notifyZoomChanged(){
        for (ZoomChangeListener l : zoomChangeListeners)
            l.onZoomChanged(zoomFactor);
    }


}
