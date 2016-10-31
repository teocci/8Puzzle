import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code Board} class constructs a board from an n-by-n array of blocks
 *  (where blocks[i][j] = block in row i, column j)
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Jorge Frisancho
 */
public class Board {
    private byte count;
    private int[] board;
    private int hamming, manhattan;
    private int blank;

    /**
     * Initializes a count-by-count board, where blocks[i][j] = block in row i, column j.
     *
     * @param blocks 2 dimension array of board
     */
    public Board(int[][] blocks) {
        count = (byte) blocks.length;
        board = new int[count * count];

        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                board[i*count+j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blank = i*count+j;
                }
            }
        }

        getManPoint();
        getHamPoint();
    }

    /**
     * Returns the number of board dimension count.
     *
     * @return number of board dimension count
     */
    public int dimension() {
        return count;
    }

    /**
     * Returns the number of blocks out of place.
     *
     * @return number of blocks out of place
     */
    public int hamming() {
        return hamming;
    }

    /**
     * Returns the sum of Manhattan distances between blocks and goal.
     *
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        return manhattan;
    }

    /**
     * Returns the boolean represent this board is the goal board.
     *
     * @return boolean represent this board is the goal board
     */
    public boolean isGoal() {
        for (int i = 0; i < count * count - 1; i++) {
            if (board[i] != i+1)
                return false;
        }

        return true;
    }

    /**
     * Returns a board obtained by exchanging two adjacent blocks in the same row.
     *
     * @return board obtained by exchanging two adjacent blocks in the same row
     */
    public Board twin() {
        int[][] twinBoard = new int[count][count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                twinBoard[i][j] = board[i*count+j];
            }
        }
        if (blank / count == 1) {
            swap(twinBoard, 0, 1);
        } else {
            swap(twinBoard, count, count+1);
        }

        return new Board(twinBoard);
    }

    /**
     * Returns the boolean represent this board equal y.
     *
     * @param y the other board
     * @return boolean represent this board equal y
     */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;

        if (this.count != that.count) {
            return false;
        }
        
        if (this.hamming == that.hamming && this.manhattan == that.manhattan) {
            for (int i = 0; i < count * count; i++) {
                if (this.board[i] != that.board[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns all neighboring boards as an independent iterator
     *
     * @return an Iterable Board
     */
    public Iterable<Board> neighbors() {
        Stack<Board> neighbor = new Stack<>();

        int up = blank - count;
        int down = blank + count;
        int left = blank - 1;
        int right = blank + 1;

        int[][] neigh = new int[count][count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                neigh[i][j] = board[i*count+j];
            }
        }

        if (up >= 0) {
            swap(neigh, up, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, up, blank);
        }
        if (down <= count*count-1) {
            swap(neigh, down, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, down, blank);
        }
        if (left >= 0 && blank % count != 0) {
            swap(neigh, left, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, left, blank);
        }
        if (right <= count*count-1 && blank % count != count-1) {
            swap(neigh, right, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, right, blank);
        }

        return neighbor;
    }

    /**
     * Returns a string representation of the board, count rows with count numbers.
     *
     * @return string representation of this board
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(count + "\n");
        for (int i = 0; i < count * count; i++) {
            s.append(String.format("%2d ", board[i]));
            if (i % count == (count - 1)) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    private void getManPoint() {
        int loc1;
        int loc2;
        for (int i = 0; i < count*count; i++) {
            if (board[i] != 0) {
                loc1 = i / count - (board[i]-1) / count;
                loc2 = i % count - (board[i]-1) % count;
                if (loc1 < 0) loc1 = -loc1;
                if (loc2 < 0) loc2 = -loc2;
                manhattan += (loc1 + loc2);
            }
        }
    }

    private void getHamPoint() {
        for (int i = 0; i < count * count; i++) {
            if (board[i] != 0 && board[i] != i + 1)
                hamming++;
        }
    }

    private void swap(int[][] block, int i, int j) {
        int temp;
        temp = block[i / count][i % count];
        block[i / count][i % count] = block[j / count][j % count];
        block[j / count][j % count] = temp;
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int count = in.readInt();
        int[][] blocks = new int[count][count];

        for (int i = 0; i < count; i++)
            for (int j = 0; j < count; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        int[][] blocks2 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board other = new Board(blocks2);
        Board twin = initial.twin();

        StdOut.println(initial);
        StdOut.println("----------------");
        StdOut.println(initial.dimension());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());
        StdOut.println(initial.equals(other));
        StdOut.println(twin);
        StdOut.println("----------------");
        for (Board board: initial.neighbors())
            StdOut.println(board);
    }
}