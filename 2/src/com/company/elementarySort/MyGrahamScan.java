package com.company.elementarySort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class MyGrahamScan {

    public static Point2D[] findConvexHull(Point2D[] points) {
        Point2D lowestPointTemp = points[0];
        for (Point2D point : points) {
            if (point.getY() < lowestPointTemp.getY()) {
                lowestPointTemp = point;
            }
        }

        final Point2D lowestPoint = lowestPointTemp;
        Arrays.sort(points, (point1, point2) -> {
            int slopePoint1 = lowestPoint.slope(point1);
            int slopePoint2 = lowestPoint.slope(point2);
            int comparison;
            if (slopePoint1 == slopePoint2) {
                comparison = 0;
            } else if ((slopePoint1 >= 0 && slopePoint2 >= 0) || (slopePoint1 < 0 && slopePoint2 < 0)) {
                comparison = slopePoint1 < slopePoint2 ? -1 : 1;
            } else {
                // different signs
                comparison = slopePoint1 >= 0 ? -1 : 1;
            }
            return comparison;
        });

        // as we're adding probable points to the convex hull we'll need to compare the points immediately before the current point - ideal case for stack
        Stack<Point2D> pointStack = new Stack<>();
        pointStack.push(points[0]);
        pointStack.push(points[1]);
        Point2D prev;
        for (int i = 2; i < points.length; i++) {
            prev = pointStack.pop();
            while (!Point2D.isCounterClockwise(pointStack.peek(), prev, points[i])) {
                // non counter clockwise rotation means that this point has to cover the previously inserted point inside the hull area
                // take out previous point, and check with the previous trio without the taken out point
                prev = pointStack.pop(); // the peek is now popped, and previous point is disposed as long as the trio doesn't form counter clockwise
            }
            // at this point peek(), prev, and point[i] form a ccw trio
            pointStack.push(prev);
            pointStack.push(points[i]);
        }

        Point2D[] convexHull = new Point2D[pointStack.size()];
        int i = 0;
        for (Point2D point : pointStack) {
            convexHull[i++] = point;
        }
        return convexHull;
    }

    private static class Point2D {
        private int x;
        private int y;

        public Comparator<Point2D> POLAR_ORDER;

        public Point2D(int x, int y) {
            this.x = x;
            this.y = y;

            this.POLAR_ORDER = (point1, point2) -> {
                int comp;
                if (point1.y >= this.y && point2.y < this.y) {
                    comp = 1;
                } else if (point1.y < this.y && point2.y >= this.y) {
                    comp = -1;
                } else if (point1.y == this.y && point2.y == this.y) {
                    if (point1.x >= this.x && point2.x < this.x) {
                        comp = -1;
                    } else if (point1.x < this.x && point2.x >= this.x) {
                        comp = 1;
                    } else {
                        comp = 0;
                    }
                } else {
                    comp = -areaDeterminant(this, point1, point2);
                }
                return comp;
            };
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int slope(Point2D that) {
            int slope;
            if (this.x != that.x) {
                slope = (that.y - this.y) / (that.x - this.x);
            } else {
                slope = Integer.MAX_VALUE;
            }
            return slope;
        }

        public static boolean isCounterClockwise(Point2D p1, Point2D p2, Point2D p3) {
            return areaDeterminant(p1, p2, p3) > 0; // area determinant sign positive for counter clockwise points
        }

        private static int areaDeterminant(Point2D p1, Point2D p2, Point2D p3) {
            return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        }

    }
}
