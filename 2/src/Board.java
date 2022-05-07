import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] tiles;
    private final int N;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // defensive copy
        this.N = tiles.length;
        this.tiles = new int[this.N][this.N];
        for (int i = 0; i < this.N; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, this.N);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.N + "\n");
        for (int[] tile : this.tiles) {
            for (int t : tile) {
                stringBuilder.append(t).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return this.N;
    }

    // number of tiles out of place
    public int hamming() {
        int notInPosition = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] != 0 && this.tiles[i][j] != tileBelongingAt(i, j)) {
                    notInPosition++;
                }
            }
        }
        return notInPosition;
    }

    private int tileBelongingAt(int row, int col) {
        return (row == this.N - 1 && col == this.N - 1) ? 0 : row * this.N + col + 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int rowColDistance = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] != 0) {
                    rowColDistance += Math.abs(i - rowOfTile(this.tiles[i][j])) +
                            Math.abs(j - colOfTile(this.tiles[i][j]));
                }
            }
        }
        return rowColDistance;
    }

    private int rowOfTile(int t) {
        return t == 0 ? this.N - 1 : (t - 1) / this.N;
    }

    private int colOfTile(int t) {
        return t == 0 ? this.N - 1 : (t - 1) % this.N;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y instanceof Board) {
            if (this == y) {
                return true;
            }
            Board yBoard = (Board) y;
            if (this.N != yBoard.N) {
                return false;
            }
            for (int i = 0; i < this.N; i++) {
                for (int j = 0; j < this.N; j++) {
                    if (this.tiles[i][j] != yBoard.tiles[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private int[] indexOf(int t) {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] == t) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    private void exchangeTiles(int row1, int col1, int row2, int col2) {
        int temp = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] indexOfZero = indexOf(0);
        // 0 blank up, 1 blank down, 2 blank left, 3 blank right
        List<Board> boards = new ArrayList<>();
        if (indexOfZero[0] > 0) {
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0] - 1, indexOfZero[1]);
            boards.add(new Board(this.tiles));
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0] - 1, indexOfZero[1]);
        }
        if (indexOfZero[0] < this.N - 1) {
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0] + 1, indexOfZero[1]);
            boards.add(new Board(this.tiles));
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0] + 1, indexOfZero[1]);
        }
        if (indexOfZero[1] > 0) {
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0], indexOfZero[1] - 1);
            boards.add(new Board(this.tiles));
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0], indexOfZero[1] - 1);
        }
        if (indexOfZero[1] < this.N - 1) {
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0], indexOfZero[1] + 1);
            boards.add(new Board(this.tiles));
            exchangeTiles(indexOfZero[0], indexOfZero[1], indexOfZero[0], indexOfZero[1] + 1);
        }

        Board[] neighboringBoards = new Board[boards.size()];
        boards.toArray(neighboringBoards);
        return () -> new NeighboringBoards(neighboringBoards);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int t = 1;
        while (this.tiles[rowOfTile(t)][colOfTile(t)] == 0) {
            t++;
        }
        int r1 = rowOfTile(t), c1 = colOfTile(t);
        t++;
        while (this.tiles[rowOfTile(t)][colOfTile(t)] == 0) {
            t++;
        }
        int r2 = rowOfTile(t), c2 = colOfTile(t);

        exchangeTiles(r1, c1, r2, c2);
        Board twin = new Board(this.tiles);
        exchangeTiles(r1, c1, r2, c2);
        return twin;
    }

    private static class NeighboringBoards implements Iterator<Board> {
        private final Board[] neighboringBoards;
        int next;
        public NeighboringBoards(Board[] neighboringBoards) {
            this.neighboringBoards = neighboringBoards;
            this.next = 0;
        }

        @Override
        public boolean hasNext() {
            return this.next < this.neighboringBoards.length;
        }

        @Override
        public Board next() {
            if (hasNext()) {
                return this.neighboringBoards[next++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        final int N = 4;
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = i * N + j;
            }
        }
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println("Hamming Distance: " + board.hamming());
        System.out.println("Manhattan Distance: " + board.manhattan());

        int[][] tilesCopy = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(tiles, 0, tilesCopy, 0, N);
        }
        Board copyBoard = new Board(tilesCopy);
        System.out.println("Copy equals main? : " + board.equals(copyBoard));


        Iterable<Board> neighborBoardIterator = board.neighbors();
        System.out.println("Original Board: ");
        System.out.println(board);

        System.out.println("Neighboring boards: ");
        for (Board neighbor : neighborBoardIterator) {
            System.out.println(neighbor);
        }

        board.exchangeTiles(0, 0, 2, 2);
        System.out.println("Original board: ");
        System.out.println(board);
        System.out.println("Neighbors: ");
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
        }

        System.out.println("Twin: ");
        System.out.println(board.twin());
    }

}
