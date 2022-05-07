import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;

public class PointSET {
    private final SET<Point2D> point2Ds;

    // construct an empty set of points
    public PointSET() {
        this.point2Ds = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.point2Ds.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.point2Ds.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p != null) {
            this.point2Ds.add(p);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p != null) {
            return this.point2Ds.contains(p);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : this.point2Ds) {
            point2D.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect != null) {
            Queue<Point2D> rangeQ = new Queue<>();
            for (Point2D point2D : this.point2Ds) {
                if (rect.contains(point2D)) {
                    rangeQ.enqueue(point2D);
                }
            }
            return rangeQ;
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p != null) {
            Point2D nearest = null;
            double minDist = Double.MAX_VALUE;
            double currentDist;
            if (!isEmpty()) {
                for (Point2D point2D : this.point2Ds) {
                    currentDist = point2D.distanceSquaredTo(p);
                    if (currentDist < minDist) {
                        minDist = currentDist;
                        nearest = point2D;
                    }
                }
            }
            return nearest;
        } else {
            throw new IllegalArgumentException();
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();

        for (int i = 0; i < 100; i++) {
            pointSET.insert(new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0)));
        }

        for (Point2D point2D : pointSET.point2Ds) {
            System.out.println(point2D.x() + ", " + point2D.y());
        }

        System.out.println("_____________________");

        for (int i = 0; i < 10; i++) {
            double xMin = StdRandom.uniform(0.0, 1.0);
            double xMax = StdRandom.uniform(xMin, 1.0);
            double yMin = StdRandom.uniform(0.0, 1.0);
            double yMax = StdRandom.uniform(yMin, 1.0);
            Iterable<Point2D> point2DIterable = pointSET.range(new RectHV(xMin, yMin, xMax, yMax));
            System.out.println("In range: X : " + xMax + ", " + xMin + "\tY: " + yMax + ", " + yMin);
            point2DIterable.forEach(point2D -> System.out.println(point2D.x() + ", " + point2D.y()));
        }

        Point2D target = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
        Point2D nearest = pointSET.nearest(target);
        System.out.println("Nearest to " + target.x() + ", " + target.y() +  " is " + nearest.x() + ", " + nearest.y());
    }
}
