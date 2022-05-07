import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private final MinPQ<SearchNode> searchNodes;
    private final MinPQ<SearchNode> altSearchNodes;
    private final SearchNode lastNode;
    private final boolean isSolvableBoard;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        this.searchNodes = new MinPQ<>();
        this.altSearchNodes = new MinPQ<>();

        this.searchNodes.insert(new SearchNode(initial, 0, null));
        this.altSearchNodes.insert(new SearchNode(initial.twin(), 0, null));

        this.lastNode = solve();
        this.isSolvableBoard = lastNode != null;
    }

    private SearchNode solve() {
        SearchNode searchNode = this.searchNodes.delMin(), previousNode;
        SearchNode altSearchNode = this.altSearchNodes.delMin(), altPreviousNode;
        // pre computed cached manhattan distance not equals zero
        while ((searchNode.getPriority() - searchNode.getMoves() != 0) &&
                (altSearchNode.getPriority() - altSearchNode.getMoves() != 0)) {
            previousNode = searchNode.getPreviousNode();
            for (Board neighborBoard : searchNode.getBoard().neighbors()) {
                if (previousNode != null && previousNode.getBoard().equals(neighborBoard)) {
                    continue;
                }
                this.searchNodes.insert(new SearchNode(neighborBoard, searchNode.getMoves() + 1, searchNode));
            }
            searchNode = this.searchNodes.delMin();

            altPreviousNode = altSearchNode.getPreviousNode();
            for (Board altNeighborBoard : altSearchNode.getBoard().neighbors()) {
                if (altPreviousNode != null && altPreviousNode.getBoard().equals(altNeighborBoard)) {
                    continue;
                }
                this.altSearchNodes.insert(new SearchNode(altNeighborBoard, altSearchNode.getMoves() + 1, altSearchNode));
            }
            altSearchNode = this.altSearchNodes.delMin();
        }
        if (searchNode.getPriority() - searchNode.getMoves() == 0) {
            return searchNode;
        } else {
            return null;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvableBoard;
    }

    // finding number of inversions in NlogN time
//    private int numberOfInversions(Board board) {
//        int[] tiles = new int[board.dimension() * board.dimension()];
//        for (int i = 0; i < board.dimension() * board.dimension(); i++) {
//            tiles[i] = board.tiles[linearIndexRow(i, board.dimension())][linearIndexCol(i, board.dimension())];
//        }
//        int[] aux = new int[tiles.length];
//        System.arraycopy(tiles, 0, aux, 0 , tiles.length);
//        return numberOfInversions(tiles, 0, tiles.length - 1, aux);
//    }
//
//    private int numberOfInversions(int[] tiles, int lo, int hi, int[] aux) {
//        if (lo >= hi) {
//            return 0;
//        }
//        int mid = (lo + hi) / 2;
//        return numberOfInversions(aux, lo, mid - 1, tiles) + numberOfInversions(aux, mid + 1, hi, tiles) + merge(tiles, lo, mid, hi, aux);
//    }
//
//    private int merge(int[] tiles, int lo, int mid, int hi, int[] aux) {
//        int ptrL = lo, ptrR = mid + 1, i = lo;
//        int numberOfInversions = 0;
//        while (i <= hi) {
//            if (ptrL > mid) {
//                tiles[i++] = aux[ptrR++];
//            } else if (ptrR > hi) {
//                tiles[i++] = aux[ptrL++];
//            } else if (aux[ptrR] < aux[ptrL]) {
//                numberOfInversions += mid - ptrL + 1;
//                tiles[i++] = aux[ptrR++];
//            } else {
//                tiles[i++] = aux[ptrL++];
//            }
//        }
//
//        return numberOfInversions;
//    }
//
//    private int linearIndexRow(int i, int size) {
//        return i / size;
//    }
//
//    private int linearIndexCol(int i, int size) {
//        return i % size;
//    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int moves = -1;
        if (this.lastNode != null) {
            moves = this.lastNode.getMoves();
        }

        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Iterable<Board> boards = null;
        if (isSolvable()) {
            Board[] boardSeq = new Board[moves() + 1];
            SearchNode searchNode = this.lastNode;
            for (int i = searchNode.getMoves(); i >= 0; i--) {
                boardSeq[i] = searchNode.getBoard();
                searchNode = searchNode.getPreviousNode();
            }

            boards = () -> new SolutionIterator(boardSeq);
        }

        return boards;
    }

    private static class SolutionIterator implements Iterator<Board> {
        final Board[] boardSeq;
        int next;

        public SolutionIterator(Board[] boardSeq) {
            this.boardSeq = boardSeq;
            this.next = 0;
        }

        @Override
        public boolean hasNext() {
            return this.next < this.boardSeq.length;
        }

        @Override
        public Board next() {
            if (hasNext()) {
                return this.boardSeq[this.next++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previousNode;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            this.priority = this.board.manhattan() + this.moves;
        }

        @Override
        public int compareTo(Solver.SearchNode that) {
            if (that == null) {
                return 1;
            }
            return Integer.compare(this.priority, that.priority);
        }

        public int getPriority() {
            return priority;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPreviousNode() {
            return previousNode;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("puzzle3x3-31.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
