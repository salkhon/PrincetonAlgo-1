import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int gridSize;
    private final boolean[][] grid;
    private int numOpenSites;
    private final WeightedQuickUnionUF weightedQuickUnionUF, weightedQuickUnionUFNoBackwash;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.gridSize = n;
        this.grid = new boolean[n][n];
        this.numOpenSites = 0;

        this.weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2); // extra dimension for storing virtual node
        this.weightedQuickUnionUFNoBackwash = new WeightedQuickUnionUF(n * n + 1); // only top virtual node to avoid backwash

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                this.grid[i - 1][j - 1] = false;
                if (i == 1) {
                    this.weightedQuickUnionUF.union(linearIndex(i, j), n * n); // extra node 0 represents top virtual node
                    this.weightedQuickUnionUFNoBackwash.union(linearIndex(i, j), n * n);
                } else if (i == n) {
                    this.weightedQuickUnionUF.union(linearIndex(i, j), n * n + 1); // extra node 1 represents bottom virtual node
                }
            }
        }
    }

    private int linearIndex(int row, int col) {
        return (row - 1) * this.gridSize + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkValidity(row, col);

        if (!isOpen(row, col)) {
            this.grid[row - 1][col - 1] = true;
            this.numOpenSites++;

            int givenIndex = linearIndex(row, col);

            if (row > 1 && isOpen(row - 1, col)) {
                this.weightedQuickUnionUF.union(linearIndex(row - 1, col), givenIndex);
                this.weightedQuickUnionUFNoBackwash.union(linearIndex(row - 1, col), givenIndex);
            }

            if (row < this.gridSize && isOpen(row + 1, col)) {
                this.weightedQuickUnionUF.union(linearIndex(row + 1, col), givenIndex);
                this.weightedQuickUnionUFNoBackwash.union(linearIndex(row + 1, col), givenIndex);

            }

            if (col > 1 && isOpen(row, col - 1)) {
                this.weightedQuickUnionUF.union(linearIndex(row, col - 1), givenIndex);
                this.weightedQuickUnionUFNoBackwash.union(linearIndex(row, col - 1), givenIndex);

            }

            if (col < this.gridSize && isOpen(row, col + 1)) {
                this.weightedQuickUnionUF.union(linearIndex(row, col + 1), givenIndex);
                this.weightedQuickUnionUFNoBackwash.union(linearIndex(row, col + 1), givenIndex);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkValidity(row, col);

        return this.grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && this.weightedQuickUnionUFNoBackwash.find(linearIndex(row, col)) == this.weightedQuickUnionUFNoBackwash.find(this.gridSize * this.gridSize);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (this.gridSize > 1) {
            return this.weightedQuickUnionUF.find(this.gridSize * this.gridSize) == this.weightedQuickUnionUF.find(this.gridSize * this.gridSize + 1);
        }
        return this.grid[0][0];
    }

    private void checkValidity(int row, int col) {
        if (row > this.gridSize || row < 1 || col > this.gridSize || col < 1) {
            throw new IllegalArgumentException();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 5;
        int[] openSites = { 0, 1, 3, 8, 11, 12, 13, 14, 15, 17, 20, 21, 23, 24};
        Percolation percolation = new Percolation(n);

        for (int i : openSites) {
            percolation.open((i / n) + 1, (i % n) + 1);
        }

        System.out.println(percolation.percolates() + " opensites: " + percolation.numberOfOpenSites() + " " + openSites.length);
    }
}
