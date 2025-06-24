package Bab5.src;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);
    //Round untuk game
    private int crossWins   = 0;   // jumlah kemenangan X
    private int noughtWins  = 0;   // jumlah kemenangan O
    private int roundsPlayed = 0;  // berapa ronde telah selesai
    private final int ROUNDS_TO_WIN = 2;   //butuh 2 kemenangan
    private boolean roundScored = false;   // agar skor 1 ronde tidak terhitung dua kali
    // Define game objects

    // GameMode
    private GameMode mode;
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player

    private Seed playerSeed;
    private Seed botSeed;
    private JLabel statusBar;    // for displaying status message

    /** Constructor to setup the UI and game components */
    public GameMain(GameMode mode) {

        this.mode = mode;
        initGame();

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {

                        if (mode == GameMode.PVB) {
                            if (currentPlayer == playerSeed) {
                                // Update cells[][] and return the new game state after the move
                                currentState = board.stepGame(playerSeed, row, col);

                                if (currentState == State.PLAYING) {
                                    botMove();
                                    currentPlayer = playerSeed;
                                }
                            }
                        } else if (mode == GameMode.PVP) {
                            // Switch player
                            currentState = board.stepGame(currentPlayer, row, col);
                            if (currentState == State.PLAYING) {
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                            }
                        }

                    }

                } else {        // game over
                    newGame();  // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
                updateScore(); //update skor


            }
        });


        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();
    }
    public GameMain(GameMode mode, String playerXName, String playerOName) {
        this(mode); // memanggil constructor utama
        this.playerXName = playerXName;
        this.playerOName = playerOName;
    }
    private void botMove() {
        // Check is current state still playing after player's turn?
        Random random = new Random();
        if (currentState == State.PLAYING) {
            // Loop until bot's row and col in empty seed
            while (true) {

                // set bot's row and col with random number (0 - 2)
                int botRow = random.nextInt(Board.ROWS);
                int botCol = random.nextInt(Board.COLS);

                // Exit loop if bot's row and col in empty seed
                if (board.cells[botRow][botCol].content == Seed.NO_SEED) {
                    currentState = board.stepGame(botSeed, botRow, botCol);
                    updateScore(); //mengupdate skor permainan setelah bot jalan
                    repaint();
                    break;
                }
            }
        }
    }

    /** Initialize the game (run once) */
    public void initGame() {
//        JFrame frame = new JFrame("Tic-Tac-Toe");
//        frame.setContentPane(this);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
        board = new Board();  // allocate the game-board

    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        board.newGame();
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // all cells empty
            }
        }

        currentState = State.PLAYING;  // ready to play
        roundScored = false; //untuk reset ronde berikutnya

        // PVB Game Mode
        if (mode == GameMode.PVB) {
            // Random seed CROSS or NOUGHT
            boolean isPlayerCross = new Random().nextBoolean();

            // Assign playerSeed and botSeed to random seed
            if (isPlayerCross) {
                playerSeed = Seed.CROSS;
                botSeed = Seed.NOUGHT;
            } else {
                playerSeed = Seed.NOUGHT;
                botSeed = Seed.CROSS;
            }

            // First init for current player, first always CROSS
            currentPlayer = Seed.CROSS;

            // Check if botseed is cross, bot will move first and set currentPlayer to playerSeed(NOUGHT)
            if (botSeed == Seed.CROSS) {
                botMove();
                currentPlayer = playerSeed;
            } else {
                currentPlayer = playerSeed;
            }
        } else if (mode == GameMode.PVP) {
            currentPlayer = Seed.CROSS;    // cross plays first
        }


        System.out.println(botSeed);

        // update
        repaint();
    }


    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g);  // ask the game board to paint itself
        String currentName = (currentPlayer == Seed.CROSS) ? playerXName : playerOName;
        // Print status-bar message BO3
        if (currentState == State.PLAYING) { //saat bermain
            statusBar.setForeground(Color.BLACK);
            statusBar.setText("Round " + (roundsPlayed + 1) + "  |  " + playerXName + "' score " + crossWins + " – " + noughtWins +  " " + playerOName + "'s Score" + "  |  " + ((currentPlayer == Seed.CROSS) ? playerXName+"'s Turn" : playerOName+"'s Turn"));
        } else if (currentState == State.DRAW) { //saat draw
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.( " + playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score)");
        } else if (currentState == State.CROSS_WON) { //saat X menang
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerXName + " Won! Click to play again. ( "+playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score)");
        } else if (currentState == State.NOUGHT_WON) { // saat O menang
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerOName + " Won! Click to play again. " + playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score)");
        }
    }

    private void resetMatch() {
        crossWins= 0;
        noughtWins = 0;
        roundsPlayed =0;
        roundScored = false;
        newGame();          // mulai ronde pertama lagi
    }
    private void updateScore() {
        if (roundScored || currentState == State.PLAYING) return;   // sudah dihitung
        String roundWinnerMessage = "";
        if(currentState == State.CROSS_WON){
             roundWinnerMessage = playerXName+" is the winner of this round";
        }else if(currentState == State.NOUGHT_WON){
             roundWinnerMessage  = playerOName + " is the winner of this round";
        }
        else{
             roundWinnerMessage = "It is a Draw";
        }
        if (currentState == State.CROSS_WON) {
            crossWins++;
        } else if (currentState == State.NOUGHT_WON) {
            noughtWins++;
        }
        roundsPlayed++;
        roundScored = true;

        JOptionPane.showMessageDialog(this, roundWinnerMessage, "Winner of round " + roundsPlayed,
                JOptionPane.INFORMATION_MESSAGE);

        // Cek apakah sudah ada juara Bo3
        if (crossWins == ROUNDS_TO_WIN || noughtWins == ROUNDS_TO_WIN) {
            String champ = (crossWins == ROUNDS_TO_WIN) ? playerXName : playerOName ;
            JOptionPane.showMessageDialog(this, champ + " Won! " +
                            "Final Score: " + playerXName +" "+ crossWins + " – " + noughtWins + " " + playerOName, "Match Finished",
                    JOptionPane.INFORMATION_MESSAGE
            );
            resetMatch();       // mulai BO3 baru
        }
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                // Set the content-pane of the JFrame to an instance of main JPanel
//                frame.setContentPane(new GameMain());

                frame.setContentPane(new StartScreen());

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true);            // show it
            }
        });
    }
}