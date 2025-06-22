package Bab5.src;

import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    public int winRow1 = -1; // inisiasi koordinat yang menang
    public int winCol1 = -1;
    public int winRow2 = -1;
    public int winCol2 = -1;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
            }
        }
    }


    /** Reset the game board, ready for new game */
    public void newGame() {
        winRow1 = -1;
        winCol1 = -1;
        winRow2 = -1;
        winCol2 = -1;
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;

        //pergantian untuk cek siapa yang menang dan menyimpan dimana menagnnya
        //Untuk cek horizontal dan menyimpan garis kemangan
        if (cells[selectedRow][0].content == player &&
                cells[selectedRow][1].content == player &&
                cells[selectedRow][2].content == player) {
                winRow1 = selectedRow;
                winRow2 = selectedRow;
                winCol1 = 0;
                winCol2 = 2;
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Untuk cek vertikal dan menyimpan garis kemenangan
        if (cells[0][selectedCol].content == player &&
                cells[1][selectedCol].content == player &&
                cells[2][selectedCol].content == player) {
                winCol1 = selectedCol;
                winCol2 = selectedCol;
                winRow1 = 0;
                winRow2 = 2;
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Untuk cek diagonal dan menyimpan garis kemanangan
        if (selectedRow == selectedCol &&
                cells[0][0].content == player &&
                cells[1][1].content == player &&
                cells[2][2].content == player) {
                winRow1 = 0;
                winCol1 = 0;
                winRow2 = 2;
                winCol2 = 2;
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        // Untuk cek anti-diagonal dan menyimpan garis kemeangan
        if (selectedRow + selectedCol == 2 &&
                cells[0][2].content == player &&
                cells[1][1].content == player &&
                cells[2][0].content == player) {
                winRow1 = 0;
                winCol1 = 2;
                winRow2 = 2;
                winCol2 = 0;
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        // Check for Draw
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }
        return State.DRAW;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
        if (winRow1 != -1 && winCol1 != -1 && winRow2 != -1 && winCol2 != -1) {
            g.setColor(Color.GREEN.darker());

            int x1 = winCol1 * Cell.SIZE + Cell.SIZE / 2;
            int y1 = winRow1 * Cell.SIZE + Cell.SIZE / 2;
            int x2 = winCol2 * Cell.SIZE + Cell.SIZE / 2;
            int y2 = winRow2 * Cell.SIZE + Cell.SIZE / 2;

            // gambar garis tebal dengan menggambar beberapa garis paralel
            for (int i = -2; i <= 2; i++) {
                g.drawLine(x1 + i, y1, x2 + i, y2); // Ketebalan horizontal
                g.drawLine(x1, y1 + i, x2, y2 + i); // Ketebalan vertikal
            }
        }
    }
}