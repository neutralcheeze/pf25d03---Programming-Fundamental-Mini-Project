import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board5 {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell5.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell5.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell5[][] cells;

    /** Constructor to initialize the game board */
    public Board5() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell5[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell5(row, col);
            }
        }
    }


    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State5 stepGame(Seed5 player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Compute and return the new game state
        if (cells[selectedRow][0].content == player  // 3-in-the-row
                && cells[selectedRow][1].content == player
                && cells[selectedRow][2].content == player
                || cells[0][selectedCol].content == player // 3-in-the-column
                && cells[1][selectedCol].content == player
                && cells[2][selectedCol].content == player
                || selectedRow == selectedCol     // 3-in-the-diagonal
                && cells[0][0].content == player
                && cells[1][1].content == player
                && cells[2][2].content == player
                || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
                && cells[0][2].content == player
                && cells[1][1].content == player
                && cells[2][0].content == player) {
            return (player == Seed5.CROSS) ? State5.CROSS_WON : State5.NOUGHT_WON;
        } else {
            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed5.NO_SEED) {
                        return State5.PLAYING; // still have empty cells
                    }
                }
            }
            return State5.DRAW; // no empty cell, it's a draw
        }
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell5.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell5.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}