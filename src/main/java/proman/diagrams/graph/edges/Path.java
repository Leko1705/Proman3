package proman.diagrams.graph.edges;

import proman.view.style.LineStyle;

import java.awt.*;
import java.awt.geom.Point2D;

public interface Path {

    boolean contains(Point2D point);

    void draw(Graphics g, LineStyle style);

    double getSourceAngle();

    double getTargetAngle();

}
