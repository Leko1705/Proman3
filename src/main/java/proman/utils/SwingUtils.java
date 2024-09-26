package proman.utils;

import proman.view.style.TextStyle;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class SwingUtils {

    private static final int LINE_CLICK_BOUNDS = 12;


    public static boolean overlap(Component c1, Component c2) {
        return c1.getBounds().intersects(c2.getBounds());
    }

    public static boolean contains(Component c, Point p) {
        Rectangle r = c.getBounds();
        return r.contains(p);
    }

    public static Shape createClickableLine(Point2D p, Point2D q) {

        double angle = angle(p, q);

        int distance = (int) distance(p, q);

        int fromX = (int) Math.min(p.getX(), q.getX());
        int fromY = (int) Math.min(p.getY(), q.getY());

        Rectangle r = new Rectangle(fromX - LINE_CLICK_BOUNDS, fromY - LINE_CLICK_BOUNDS, distance + LINE_CLICK_BOUNDS, LINE_CLICK_BOUNDS * 2);

        AffineTransform rotateTransform = AffineTransform.getRotateInstance((angle*2*Math.PI)/360d);
        // rotate the original shape with no regard to the final bounds
        Shape rotatedShape = rotateTransform.createTransformedShape(r);
        // get the bounds of the rotated shape
        Rectangle2D rotatedRect = rotatedShape.getBounds2D();
        // calculate the x,y offset needed to shift it to top/left bounds of original rectangle
        double xOff = r.getX()-rotatedRect.getX();
        double yOff = r.getY()-rotatedRect.getY();
        AffineTransform translateTransform = AffineTransform.getTranslateInstance(xOff, yOff);
        // shift the new shape to the top left of original rectangle
        return translateTransform.createTransformedShape(rotatedShape);
    }

    public static Point closestPointOnRectTo(Rectangle r1, Rectangle r2){
        Point m1 = getCenter(r1);
        Point m2 = getCenter(r2);
        return lineIntersectionOnRect(r1, new Line2D.Double(m1, m2));
    }

    public static Point lineIntersectionOnRect(Rectangle rectangle, Line2D line) {

        double w = rectangle.getWidth() / 2;
        double h = rectangle.getHeight() / 2;

        double xB = line.getP1().getX();
        double yB = line.getP1().getY();

        double xA = line.getP2().getX();
        double yA = line.getP2().getY();

        double dx = xA - xB;
        double dy = yA - yB;

        //if A=B return B itself
        if (dx == 0 && dy == 0) return new Point((int) xB, (int) yB);

        double tan_phi = h / w;
        double tan_theta = Math.abs(dy / dx);

        //tell me in which quadrant the A point is
        double qx = (int) Math.signum(dx);
        double qy = (int) Math.signum(dy);


        double xI, yI;

        if (tan_theta > tan_phi) {
            xI = xB + (h / tan_theta) * qx;
            yI = yB + h * qy;
        } else {
            xI = xB + w * qx;
            yI = yB + w * tan_theta * qy;
        }

        return new Point((int) xI, (int) yI);
    }

    public static Point getCenter(Rectangle rectangle){
        int x = (int) (rectangle.getX() + rectangle.getWidth()/2);
        int y = (int) (rectangle.getY() + rectangle.getHeight()/2);
        return new Point(x, y);
    }

    public static Point getCenter(Point p, Point q){
        int x = p.x + ((q.x - p.x)/2);
        int y = p.y + ((q.y - p.y)/2);
        return new Point(x, y);
    }

    public static double angle(Point2D p, Point2D q){
        return Math.toDegrees(Math.atan2(q.getY() - p.getY(), q.getX() - p.getX()));
    }

    public static double distance(Point2D p1, Point2D p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        }
        else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    public static void applyTextStyle(StyledDocument doc, TextStyle textStyle){
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        attrs.addAttribute("Underline-Color", textStyle.getUnderlineColor());
        doc.setCharacterAttributes(0, doc.getLength()-1, attrs, true);

        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);

        StyleConstants.setBold(attrs, textStyle.isBold());
        StyleConstants.setItalic(attrs, textStyle.isItalic());
        StyleConstants.setUnderline(attrs, textStyle.isUnderline());
        StyleConstants.setFontSize(attrs, textStyle.getTextSize());
        StyleConstants.setForeground(attrs, textStyle.getTextColor());

        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
    }
}
