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
    private GameMode mode;
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private Seed playerSeed;
    private Seed botSeed;
    private JLabel statusBar;    // for displaying status message

    // Variabel untuk menyimpan posisi mouse untuk efek hover
    private int hoveredRow = -1;
    private int hoveredCol = -1;

    /** Constructor to setup the UI and game components */
    public GameMain(GameMode mode) {
        this.mode = mode;
        initGame();

        // Listener untuk event klik mouse
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Mendapatkan baris dan kolom yang diklik
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {

                        if (mode == GameMode.PVB) {
                            if (currentPlayer == playerSeed) {
                                // Memperbarui state game setelah pemain melangkah
                                currentState = board.stepGame(playerSeed, row, col);

                                if (currentState == State.PLAYING) {
                                    botMove();
                                    currentPlayer = playerSeed;
                                }
                            }
                        } else if (mode == GameMode.PVP) {
                            // Mengganti giliran pemain
                            currentState = board.stepGame(currentPlayer, row, col);
                            if (currentState == State.PLAYING) {
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                            }
                        }
                    }
                } else {        // Jika game sudah selesai
                    newGame();  // Memulai game baru
                }
                // Menggambar ulang papan permainan
                repaint();
                updateScore(); // Memperbarui skor
            }

            // Event untuk mereset highlight saat kursor mouse keluar dari area papan
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                hoveredCol = -1;
                repaint();
            }
        });

        // Listener untuk mendeteksi gerakan mouse di atas papan untuk efek hover
        super.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = e.getY() / Cell.SIZE;
                int col = e.getX() / Cell.SIZE;

                if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS) {
                    if (hoveredRow != row || hoveredCol != col) { // Hanya update jika posisi berubah
                        hoveredRow = row;
                        hoveredCol = col;
                        repaint();
                    }
                } else {
                    if (hoveredRow != -1 || hoveredCol != -1) { // Hanya update jika sebelumnya ada highlight
                        hoveredRow = -1;
                        hoveredCol = -1;
                        repaint();
                    }
                }
            }
        });

        // Pengaturan status bar untuk menampilkan pesan
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
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Inisialisasi permainan
        initGame();
        newGame();
    }

    // Constructor tambahan untuk menerima nama pemain (jika dikembangkan lebih lanjut)
    public GameMain(GameMode mode, String playerXName, String playerOName) {
        // Logika untuk menyimpan nama pemain bisa ditambahkan di sini
        this(mode); // Memanggil constructor utama
    }

    // Logika untuk gerakan bot (secara acak)
    private void botMove() {
        Random random = new Random();
        if (currentState == State.PLAYING) {
            while (true) {
                int botRow = random.nextInt(Board.ROWS);
                int botCol = random.nextInt(Board.COLS);

                // Keluar dari loop jika sel yang dipilih acak masih kosong
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
        board = new Board();  // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        board.newGame();
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // Mengosongkan semua sel
            }
        }
        currentState = State.PLAYING;  // Siap untuk bermain
        roundScored = false; // Mereset status skor ronde

        // Pengaturan untuk mode Player vs Bot
        if (mode == GameMode.PVB) {
            boolean isPlayerCross = new Random().nextBoolean();
            if (isPlayerCross) {
                playerSeed = Seed.CROSS;
                botSeed = Seed.NOUGHT;
            } else {
                playerSeed = Seed.NOUGHT;
                botSeed = Seed.CROSS;
            }
            currentPlayer = Seed.CROSS; // X selalu jalan pertama
            if (botSeed == Seed.CROSS) {
                botMove(); // Jika bot mendapat giliran pertama
                currentPlayer = playerSeed;
            } else {
                currentPlayer = playerSeed;
            }
        } else if (mode == GameMode.PVP) { // Pengaturan untuk mode Player vs Player
            currentPlayer = Seed.CROSS;    // X jalan pertama
        }
        repaint();
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // Mengatur warna background

        board.paint(g);  // Meminta papan untuk menggambar dirinya sendiri

        // Menggambar kotak highlight di sel yang sedang disorot oleh mouse
        if (currentState == State.PLAYING && hoveredRow != -1 && hoveredCol != -1) {
            if (board.cells[hoveredRow][hoveredCol].content == Seed.NO_SEED) {
                // Atur warna highlight
                g.setColor(new Color(173, 216, 230, 128)); // Biru muda semi-transparan
                // Gambar persegi panjang di atas sel
                g.fillRect(hoveredCol * Cell.SIZE, hoveredRow * Cell.SIZE, Cell.SIZE, Cell.SIZE);
            }
        }

        // Menampilkan pesan status berdasarkan state permainan
        if (currentState == State.PLAYING) { //saat bermain
            statusBar.setForeground(Color.BLACK);
            statusBar.setText("Round " + (roundsPlayed + 1) + "  |  X's Score " + crossWins + " – " + noughtWins + " O's Score" + "  |  " + ((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn"));
        } else if (currentState == State.DRAW) { //saat draw
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.(X's Score  " + crossWins + " – " + noughtWins + " O's Score)");
        } else if (currentState == State.CROSS_WON) { //saat X menang
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again. (X's Score " + crossWins + " – " + noughtWins + " O's Score)");
        } else if (currentState == State.NOUGHT_WON) { // saat O menang
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again. X's Score  " + crossWins + " – " + noughtWins + " O's Score)");
        }
    }

    // Mereset seluruh match (skor dan ronde)
    private void resetMatch() {
        crossWins= 0;
        noughtWins = 0;
        roundsPlayed =0;
        roundScored = false;
        newGame();          // Memulai ronde pertama dari match baru
    }

    // Memperbarui skor dan menampilkan pemenang ronde melalui pop-up
    private void updateScore() {
        if (roundScored || currentState == State.PLAYING) return;   // Mencegah skor dihitung dua kali

        // Menyiapkan pesan pemenang ronde
        String roundWinnerMessage = "";
        if(currentState == State.CROSS_WON){
            roundWinnerMessage = "X is the winner of this round";
        }else if(currentState == State.NOUGHT_WON){
            roundWinnerMessage  = "O is the winner of this round";
        }
        else{
            roundWinnerMessage = "It is a Draw";
        }

        // Menambah skor pemenang
        if (currentState == State.CROSS_WON) {
            crossWins++;
        } else if (currentState == State.NOUGHT_WON) {
            noughtWins++;
        }
        roundsPlayed++;
        roundScored = true;

        // Menampilkan pesan pop-up untuk pemenang ronde
        JOptionPane.showMessageDialog(this, roundWinnerMessage, "Winner of round " + roundsPlayed,
                JOptionPane.INFORMATION_MESSAGE);

        // Cek apakah sudah ada pemenang match (Best of 3)
        if (crossWins == ROUNDS_TO_WIN || noughtWins == ROUNDS_TO_WIN) {
            String champ = (crossWins == ROUNDS_TO_WIN) ? "X" : "O";
            JOptionPane.showMessageDialog(this, champ + " Won! " +
                            "Final Score: X " + crossWins + " – " + noughtWins + " O", "Match Finished",
                    JOptionPane.INFORMATION_MESSAGE
            );
            resetMatch();       // Memulai match baru
        }
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Menjalankan kode pembuatan GUI di Event-Dispatching thread untuk keamanan thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new StartScreen()); // Memulai dari StartScreen

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // Memposisikan window di tengah layar
                frame.setVisible(true);            // Menampilkan window
            }
        });
    }
}