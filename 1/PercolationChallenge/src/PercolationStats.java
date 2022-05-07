import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double[] results;

    private double meanValue;
    private double stdDevValue;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        this.meanValue = -1;
        this.stdDevValue = -1;
        this.results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }

            this.results[i] = ((double) percolation.numberOfOpenSites()) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (Math.abs(this.meanValue - (-1)) <= 0.0001) {
            this.meanValue = StdStats.mean(this.results);
        }

        return this.meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (Math.abs(this.stdDevValue - (-1)) <= 0.0001) {
            this.stdDevValue = StdStats.stddev(this.results);
        }

        return this.stdDevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(this.results.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(this.results.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n, trial;
        try {
            n = Integer.parseInt(args[0]);
            trial = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return;
        }

        PercolationStats percolationStats = new PercolationStats(n, trial);

        System.out.println("mean\t\t\t\t\t= " + percolationStats.mean());
        System.out.println("stddev\t\t\t\t\t= " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
