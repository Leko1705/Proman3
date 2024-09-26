package proman.diagrams.coordsys;

import java.awt.geom.Point2D;

public class Matrix2D {

    private final double[][] base;


    public Matrix2D(double[][] base) {
        if (base.length != 2 || base[0].length != 2)
            throw new IllegalArgumentException("base must have a dimension of 2");

        this.base = base;
    }

    public Matrix2D(double x0, double y0, double x1, double y1) {
        base = new double[][] {
                {x0, x1},
                {y0, y1}
        };
    }

    public double[][] getBase() {
        return base;
    }

    public Point2D transform(double x, double y) {
        return transform(new Point2D.Double(x, y));
    }

    public Point2D transform(Point2D p){
        return new Point2D.Double(
                base[0][0] * p.getX() + base[1][0] * p.getY(),
                base[0][1] * p.getX() + base[1][1] * p.getY()
        );
    }

    public double getX0(){
        return base[0][0];
    }



}
