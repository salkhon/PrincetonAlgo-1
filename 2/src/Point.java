import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point


    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        double slope;
        if (this.x != that.x) {
            if (this.y != that.y) {
                slope = (double) (that.y - this.y) / (that.x - this.x);
            } else {
                slope = +0.0; // positive zero - negative zero issue avoidance for IEEE floating point representation
            }
        } else {
            if (this.y == that.y) {
                slope = Double.NEGATIVE_INFINITY;
            } else {
                slope = Double.POSITIVE_INFINITY;
            }
        }

        return slope;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        int comp;
        comp = Integer.compare(this.y, that.y);
        if (comp == 0) {
            comp = Integer.compare(this.x, that.x);
        }

        return comp;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return Comparator.comparingDouble(this::slopeTo);
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point point1 = new Point(1, 3);
        Point point2 = new Point(5, 7);
        System.out.println("Slope of " + point1 + " and " + point2 + " is " + point1.slopeTo(point2));

        final int TEST = 100;
        Point[] points = new Point[TEST];
        for (int i = 0; i < TEST; i++) {
            points[i] = new Point(StdRandom.uniform(100), StdRandom.uniform(100));
        }

        Point comparingPoint = points[StdRandom.uniform(TEST)];
        Arrays.sort(points, comparingPoint.slopeOrder());

        System.out.println("Points sorted with slope order with " + comparingPoint + " : ");
        for (Point point : points) {
            System.out.print(point + " ");
        }
    }
}