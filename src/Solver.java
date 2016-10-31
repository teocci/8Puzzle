import edu.princeton.cs.algs4.*;

/**
 *  The {@code Solver} class provides a solution to an initial board by using the A* algorithm.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Jorge Frisancho
 */
public class Solver {
    private boolean solvable;
    private int moves;
    private MinPQ<SearchNode> pq = new MinPQ<>();
    private Stack<Board> boardStack = new Stack<>();

    /**
     * This method finds a solution to the initial board by using the A* algorithm.
     *
     * @param initial the Board object
     */
    public Solver(Board initial) {
        checkPointNull(initial);

        initBoard();

        Board board;
        SearchNode node = null;
        pq.insert(new SearchNode(initial, true));
        pq.insert(new SearchNode(initial.twin(), false));

        while (!pq.isEmpty()) {
            node = pq.delMin();
            board = node.getBoard();
            if (board.isGoal()) {
                break;
            }

            for (Board neigh: board.neighbors()) {
                if (node.prev == null || !node.prev.getBoard().equals(neigh)) {
                    pq.insert(new SearchNode(neigh, node));
                }
            }
        }

        if (node.notTwin) {
            solvable = true;
            moves = node.move;

            while (node != null) {
                boardStack.push(node.board);
                node = node.prev;
            }
        } else {
            solvable = false;
            moves = -1;
        }
    }

    private void initBoard() {
        solvable = false;
        moves = 0;
    }

    /**
     * Returns the boolean represents the initial board is solvable.
     *
     * @return boolean represents the initial board is solvable
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Returns the minimum number of moves to solve initial board; -1 if no solution.
     *
     * @return minimum number of moves to solve initial board; -1 if no solution
     */
    public int moves() {
        return moves;
    }

    /**
     * Returns an independent iterator over solution of Boards.
     *
     * @return independent iterator over solution of Boards
     */
    public Iterable<Board> solution() {
        if (!solvable)
            return null;
        return boardStack;
    }

    /**
     *  The {@code SearchNode} inner class implements a Comparable interface to use the Manhattan priority
     *  as heuristic value.
     *  <p>
     *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a>
     *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
     *
     *  @author Jorge Frisancho
     */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int move;
        private int priority;
        private boolean notTwin;

        public SearchNode(Board board, boolean notTwin) {
            this.board = board;
            prev = null;
            move = 0;
            priority = board.manhattan();
            this.notTwin = notTwin;
        }

        public SearchNode(Board board, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.move = (short) (prev.move + 1);
            this.notTwin = prev.notTwin;
            priority = (short) (board.manhattan() + this.move);
        }

        public Board getBoard() {
            return board;
        }

        public int compareTo(SearchNode that) {
            if (priority < that.priority)
                return -1;
            else if (priority > that.priority)
                return 1;
            else {
                if (board.hamming() >= that.board.hamming())
                    return 1;
                else
                    return -1;
            }
        }
    }

    /**
     * Throws a java.lang.NullPointerException if a point is null
     *
     * @param initial an array of points
     */
    private void checkPointNull(Board initial) {
        if (initial == null)
            throw new NullPointerException("A point in the array is null");
    }

    /**
     *  test client to read a puzzle from a file (specified as a command-line argument)
     *  and print the solution to standard output.
     *
     *  @param args main function standard arguments
     */
    public static void main(String[] args) { // solve a slider puzzle (given below)  
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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