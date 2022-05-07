import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] points1 = Arrays.copyOf(points, points.length);
        checkArgumentValidity(points1);

        List<LineSegment> lineSegmentList = new ArrayList<>();

        for (int i = 0; i < points1.length; i++) {
            for (int j = i + 1; j < points1.length; j++) {
                for (int k = j + 1; k < points1.length; k++) {
                    for (int m = k + 1; m < points1.length; m++) {

                        double slopeIJ = points1[i].slopeTo(points1[j]);
                        double slopeJK = points1[j].slopeTo(points1[k]);
                        double slopeKL = points1[k].slopeTo(points1[m]);

                        if (slopeIJ == slopeJK && slopeJK == slopeKL) {
                            Point[] temp = new Point[4];
                            temp[0] = points1[i]; temp[1] = points1[j]; temp[2] = points1[k]; temp[3] = points1[m];
                            Arrays.sort(temp);
                            lineSegmentList.add(new LineSegment(temp[0], temp[3]));
                        }
                    }
                }
            }
        }

        this.lineSegments = new LineSegment[lineSegmentList.size()];
        for (int i = 0; i < lineSegmentList.size(); i++) {
            this.lineSegments[i] = lineSegmentList.get(i);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, this.lineSegments.length);
    }

    private void checkArgumentValidity(Point[] points) {
        try {
            Arrays.sort(points); // so can check for adjacent duplicates
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null || (i > 0 && points[i].compareTo(points[i - 1]) == 0)) {
                throw new IllegalArgumentException();
            }
        }
    }

    // test client
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

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        LineSegment[] lineSegments = bruteCollinearPoints.segments();

        for (LineSegment lineSegment : lineSegments) {
            System.out.println(lineSegment);
        }
    }
}
