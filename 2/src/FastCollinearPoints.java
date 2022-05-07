import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private final Point[] points;
    private final LineSegment[] lineSegments;
    private final List<Point[]> segmentPoints;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        this.points = Arrays.copyOf(points, points.length);
        checkArgumentValidity(this.points);

        List<LineSegment> lineSegmentList = new ArrayList<>();

        this.segmentPoints = new ArrayList<>();

        for (int i = 0; i < this.points.length - 3; i++) {
            Arrays.sort(this.points, i + 1, this.points.length, this.points[i].slopeOrder());
            int streak = 0;
            double lastSlope = Double.NEGATIVE_INFINITY;
            double currentSlope;
            for (int j = i + 1; j < this.points.length; j++) {

                currentSlope = this.points[i].slopeTo(this.points[j]);
                if (currentSlope == Double.NEGATIVE_INFINITY) {
                    continue;
                }

                if (currentSlope == lastSlope) {
                    streak++;
                } else {
                    if (streak >= 2) {
                        addLineSegment(lineSegmentList, i, j - 1, streak); // adding when streak ends to take maximal streak
                    }
                    streak = 0;
                }

                lastSlope = currentSlope;
            }

            // the last item might be >2 streak
            if (streak >= 2) {
                addLineSegment(lineSegmentList, i, this.points.length - 1, streak);
            }
        }

        filterSubSegments(lineSegmentList);

        this.lineSegments = new LineSegment[lineSegmentList.size()];
        lineSegmentList.toArray(this.lineSegments);
    }

    private void addLineSegment(List<LineSegment> lineSegmentList, int i, int j, int streak) {
        List<Point> temp = new ArrayList<>();
        temp.add(this.points[i]); // this.points[i] is the unique entry for each sub-segment
        for (int k = 0; k <= streak; k++) {
            temp.add(this.points[j - k]);
        }

        // for filtering out sub segments
        Point[] tempPoints = new Point[2];
        tempPoints[0] = temp.get(0); tempPoints[1] = temp.get(temp.size() - 1); // temp.get(0) stores this.point[i] - which resolves bug for infinite slopes
        this.segmentPoints.add(tempPoints);

//        System.out.println("Sub-segments: " + temp);
        Collections.sort(temp);
        lineSegmentList.add(new LineSegment(temp.get(0), temp.get(temp.size() - 1)));
    }

    // hideous bug for infinite slope cases - if || is in slope compare
    private void filterSubSegments(List<LineSegment> lineSegmentList) {
        for (int i = 0; i < this.segmentPoints.size(); i++) {
            double segmentSlope = this.segmentPoints.get(i)[0].slopeTo(this.segmentPoints.get(i)[1]);
            for (int j = 0; j < i; j++) {
                if (segmentSlope == this.segmentPoints.get(i)[0].slopeTo(this.segmentPoints.get(j)[0])) {
                    this.segmentPoints.remove(i); // linear! so n2 * n = n3, might use array to avoid remove() or assign null to indicate removal
//                    System.out.println("Removing : " + lineSegmentList.remove(i));
                    lineSegmentList.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.lineSegments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, this.lineSegments.length);
    }

    private void checkArgumentValidity(Point[] points) {
        try {
            Arrays.sort(points);
        } catch (NullPointerException e) {
            throw  new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null || (i > 0 && points[i].compareTo(points[i - 1]) == 0)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {
        final int TEST = 100;
        Point[] points = new Point[TEST];
        for (int i = 0; i < TEST; i++) {
            points[i] = new Point(StdRandom.uniform(100), StdRandom.uniform(100));
        }

        for (Point point : points) {
            System.out.print(point + " ");
        }
        System.out.println();

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        LineSegment[] lineSegments = fastCollinearPoints.segments();

        for (LineSegment lineSegment : lineSegments) {
            System.out.println(lineSegment);
        }
//        Point[] points = new Point[6];
//        points[0] = new Point(19000, 10000);
//        points[1] = new Point(18000, 10000);
//        points[2] = new Point(32000, 10000);
//        points[3] = new Point(21000, 10000);
//        points[4] = new Point(1234, 5678);
//        points[5] = new Point(14000, 10000);
//
//        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
//        for (LineSegment lineSegment : fastCollinearPoints.segments()) {
//            System.out.println(lineSegment);
//        }
//        Point[] points = new Point[8];
//        points[0] = new Point(10000, 0);
//        points[1] = new Point(0, 10000);
//        points[2] = new Point(3000, 7000);
////        points[2] = null;
//        points[3] = new Point(7000, 3000);
//        points[4] = new Point(20000, 21000);
//        points[5] = new Point(3000, 4000);
//        points[6] = new Point(14000, 15000);
//        points[7] = new Point(6000, 7000);
//
//        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
//        for (LineSegment lineSegment : fastCollinearPoints.segments()) {
//            System.out.println(lineSegment);
//        }
    }
}
