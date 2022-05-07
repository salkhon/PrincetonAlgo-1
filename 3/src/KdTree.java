import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    private static final boolean VERTICAL = true;

    private Node<Point2D> root;

    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.root == null;
    }

    // number of points in the set
    public int size() {
        return subtreePopulationOf(this.root);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p != null) {
            this.root = insert(p, this.root, 0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Node<Point2D> insert(Point2D point2D, Node<Point2D> current, int depth) {
        if (current == null) {
            return new Node<>(point2D, depth);
        }

        if (point2D.equals(current.getKey())) {
            return current;
        }

        int cmp = comparePointOrientationToSplit(point2D, current);

        if (cmp <= 0) {
            current.setLeft(insert(point2D, current.getLeft(), depth + 1));
        } else {
            current.setRight(insert(point2D, current.getRight(), depth + 1));
        }

        current.setSubtreePopulation(subtreePopulationOf(current.getLeft()) + subtreePopulationOf(current.getRight()) + 1);

        return current;
    }

    private int subtreePopulationOf(Node<Point2D> point2DNode) {
        int population = 0;
        if (point2DNode != null) {
            population = point2DNode.getSubtreePopulation();
        }
        return population;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p != null) {
            boolean doesContain = false;
            Node<Point2D> current = this.root;

            int cmp;
            while (current != null) {
                cmp = comparePointOrientationToSplit(p, current);

                if (p.equals(current.getKey())) {
                    doesContain = true;
                    break;
                } else if (cmp <= 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
            }
            return doesContain;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int comparePointOrientationToSplit(Point2D p, Node<Point2D> splitPoint) {
        int cmp;
        if (splitPoint.splitType() == VERTICAL) {
            cmp = Double.compare(p.x(), splitPoint.getKey().x());
        } else {
            cmp = Double.compare(p.y(), splitPoint.getKey().y());
        }
        return cmp;
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect != null) {
            Queue<Point2D> point2Ds = new Queue<>();
            range(rect, this.root, point2Ds);
            return point2Ds;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void range(RectHV queryRange, Node<Point2D> current, Queue<Point2D> point2Ds) {
        if (current == null) {
            return;
        }

        if (isQueryRectangleInLeftSubtree(queryRange, current)) {
            range(queryRange, current.getLeft(), point2Ds);
        }

        if (isQueryRectangleInRightSubtree(queryRange, current)) {
            range(queryRange, current.getRight(), point2Ds);
        }

        if (queryRange.contains(current.getKey())) {
            point2Ds.enqueue(current.getKey());
        }
    }

    // if there is query section to the left, search left
    // if there is query section to the right, search right
    private boolean isQueryRectangleInLeftSubtree(RectHV queryRange, Node<Point2D> current) {
        boolean value;
        if (current.splitType() == VERTICAL) {
            value = queryRange.xmin() <= current.getKey().x();
        } else {
            value = queryRange.ymin() <= current.getKey().y();
        }
        return value;
    }

    private boolean isQueryRectangleInRightSubtree(RectHV queryRange, Node<Point2D> current) {
        boolean value;
        if (current.splitType() == VERTICAL) {
            value = queryRange.xmax() >= current.getKey().x();
        } else {
            value = queryRange.ymax() >= current.getKey().y();
        }
        return value;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p != null) {
            Point2D nearest = null;
            if (!isEmpty()) {
                nearest = searchNearest(p, this.root.getKey(), this.root, null);
            }
            return nearest;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Point2D searchNearest(Point2D queryPoint, Point2D currentNearest, Node<Point2D> current, Node<Point2D> parent) {
        if (current == null) {
            return currentNearest;
        }

        if (queryPoint.distanceSquaredTo(currentNearest) > queryPoint.distanceSquaredTo(current.getKey())) {
            currentNearest = current.getKey();
        }

        int cmp = comparePointOrientationToSplit(queryPoint, current);
        if (cmp <= 0) {
            currentNearest = searchNearest(queryPoint, currentNearest, current.getLeft(), current);
        }
        if (cmp >= 0) {
            currentNearest = searchNearest(queryPoint, currentNearest, current.getRight(), current);
        }

        // prune
        if (!isPrune(queryPoint, current, parent, queryPoint.distanceSquaredTo(currentNearest))) {
            // or search the other subtree
            if (cmp < 0) {
                currentNearest = searchNearest(queryPoint, currentNearest, current.getRight(), current);
            } else if (cmp > 0) {
                currentNearest = searchNearest(queryPoint, currentNearest, current.getLeft(), current);
            }
        }

        return currentNearest;
    }

    private boolean isPrune(Point2D queryPoint, Node<Point2D> current, Node<Point2D> parent, double currentNearestDist) {
        double minDistanceOnTheOtherSplitPlane;
        if (parent != null && pointOnOppositeSideOfParent(queryPoint, current, parent)) {
            Point2D intersection;
            if (current.splitType() == VERTICAL) {
                intersection = new Point2D(current.getKey().x(), parent.getKey().y());
            } else {
                intersection = new Point2D(parent.getKey().x(), current.getKey().y());
            }
            minDistanceOnTheOtherSplitPlane = queryPoint.distanceSquaredTo(intersection);
        } else {
            minDistanceOnTheOtherSplitPlane = perpendicularDistSquaredFromPointToSplitNode(queryPoint, current);
        }

        return currentNearestDist <= minDistanceOnTheOtherSplitPlane;
    }
    private boolean pointOnOppositeSideOfParent(Point2D point, Node<Point2D> current, Node<Point2D> parent) {
        int cmpPointParent = comparePointOrientationToSplit(point, parent);
        int cmpCurrentParent = comparePointOrientationToSplit(current.getKey(), parent);
        return cmpPointParent > 0 && cmpCurrentParent <= 0 ||
                cmpPointParent <= 0 && cmpCurrentParent > 0;
    }

    private double perpendicularDistSquaredFromPointToSplitNode(Point2D point, Node<Point2D> splitLine) {
        double distance;
        if (splitLine.splitType() == VERTICAL) {
            distance = Math.abs(point.x() - splitLine.getKey().x());
        } else {
            distance = Math.abs(point.y() - splitLine.getKey().y());
        }
        return distance * distance;
    }

    private static class Node<K extends Comparable<K>> implements Comparable<Node<K>> {
        private final K key;
        private Node<K> left, right;
        private int depth;
        private int subtreePopulation;

        public Node(K key, int depth) {
            this.key = key;
            this.depth = depth;
            this.subtreePopulation = 1;
        }

        public Node<K> getLeft() {
            return left;
        }

        public void setLeft(Node<K> left) {
            this.left = left;
        }

        public Node<K> getRight() {
            return right;
        }

        public void setRight(Node<K> right) {
            this.right = right;
        }

        public boolean splitType() {
            return this.depth % 2 == 0;
        }

        public K getKey() {
            return key;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public int getSubtreePopulation() {
            return subtreePopulation;
        }

        public void setSubtreePopulation(int subtreePopulation) {
            this.subtreePopulation = subtreePopulation;
        }

        @Override
        public int compareTo(Node<K> o) {
            return this.key.compareTo(o.key);
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree pointsKdTree = new KdTree();

        for (int i = 0; i < 100; i++) {
            pointsKdTree.insert(new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0)));
        }

        System.out.println("_____________________");

        for (int i = 0; i < 10; i++) {
            double xMin = StdRandom.uniform(0.0, 1.0);
            double xMax = StdRandom.uniform(xMin, 1.0);
            double yMin = StdRandom.uniform(0.0, 1.0);
            double yMax = StdRandom.uniform(yMin, 1.0);
            Iterable<Point2D> point2DIterable = pointsKdTree.range(new RectHV(xMin, yMin, xMax, yMax));
            System.out.println("In range: X : " + xMax + ", " + xMin + "\tY: " + yMax + ", " + yMin);
            point2DIterable.forEach(point2D -> System.out.println(point2D.x() + ", " + point2D.y()));
        }

        Point2D target = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
        Point2D nearest = pointsKdTree.nearest(target);
        System.out.println("Nearest to " + target.x() + ", " + target.y() +  " is " + nearest.x() + ", " + nearest.y());
    }
}