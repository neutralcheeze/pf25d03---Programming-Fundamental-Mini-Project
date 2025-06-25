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
    private static final long serialVersionUID = 1L;

    // Define named constants for the drawing graphics
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);
    // Round untuk game
    private int crossWins   = 0;
    private int noughtWins  = 0;
    private int roundsPlayed = 0;
    private final int ROUNDS_TO_WIN = 2;
    private boolean roundScored = false;
    private int playerWIns = 0;
    private int botWin = 0;

    // Define game objects
    private GameMode mode;
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private Seed playerSeed;
    private Seed botSeed;
    private JLabel statusBar;

    // Variabel untuk menyimpan posisi mouse untuk efek hover
    private int hoveredRow = -1;
    private int hoveredCol = -1;

    /** Constructor utama untuk setup UI dan komponen game */
    public GameMain(GameMode mode) {
        this.mode = mode;
        initGame();

        // Listener untuk event klik mouse
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {

                        if (mode == GameMode.PVB) {
                            if (currentPlayer == playerSeed) {
                                // Memperbarui state game setelah pemain melangkah
                                State result = board.stepGame(playerSeed, row, col);
                                if ((result == State.CROSS_WON && playerSeed == Seed.CROSS) || // ketika x menang dan player adalah x artinya player menang
                                        (result == State.NOUGHT_WON && playerSeed == Seed.NOUGHT)) {
                                    currentState = State.PLAYER_WON;
                                } else if ((result == State.CROSS_WON && playerSeed != Seed.CROSS) || // jika x menang tapi x bukan player artinya bot menang
                                        (result == State.NOUGHT_WON && playerSeed != Seed.NOUGHT)) {
                                    currentState = State.BOTWON;
                                } else {
                                    currentState = result;
                                }
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
                } else { // Jika game sudah selesai, klik di mana saja akan memulai game baru
                    newGame();
                }
                SoundEffect.DIE.play();
                repaint(); // Menggambar ulang papan
                updateScore(); // mengupdate score
            }

            // Event untuk menghilangkan highlight saat kursor keluar dari papan
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                hoveredCol = -1;
                repaint();
            }
        });

        // Listener untuk mendeteksi gerakan mouse untuk efek hover
        super.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = e.getY() / Cell.SIZE;
                int col = e.getX() / Cell.SIZE;

                if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS) {
                    if (hoveredRow != row || hoveredCol != col) {
                        hoveredRow = row;
                        hoveredCol = col;
                        repaint();
                    }
                } else {
                    if (hoveredRow != -1 || hoveredCol != -1) {
                        hoveredRow = -1;
                        hoveredCol = -1;
                        repaint();
                    }
                }
            }
        });

        // Pengaturan status bar di bagian bawah
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Inisialisasi permainan
        initGame();
        newGame();

        // mulai background sound
        BackgroundMusicPlayer.play();
    }

    /** Constructor tambahan untuk menerima nama pemain */
    public GameMain(GameMode mode, String playerXName, String playerOName) {
        this(mode);
        this.playerXName = playerXName;
        this.playerOName = playerOName;
    }

    /** Logika untuk gerakan bot (secara acak) */
    private void botMove() {
        Random random = new Random();
        if (currentState == State.PLAYING) {
            while (true) {
                int botRow = random.nextInt(Board.ROWS);
                int botCol = random.nextInt(Board.COLS);

                if (board.cells[botRow][botCol].content == Seed.NO_SEED) {
                    State result = board.stepGame(botSeed, botRow, botCol);
                    if ((result == State.CROSS_WON && botSeed == Seed.CROSS) || //Jika x menang dan bot adalah x, maka bot menang
                            (result == State.NOUGHT_WON && botSeed == Seed.NOUGHT)) {
                        currentState = State.BOTWON;
                    } else if ((result == State.CROSS_WON && botSeed != Seed.CROSS) || //jika setelah bergerak dan x menang tapi bot bukan x, maka player menang
                            (result == State.NOUGHT_WON && botSeed != Seed.NOUGHT)) {
                        currentState = State.PLAYER_WON;
                    } else {
                        currentState = result;
                    }

                    // Mainkan suara untuk gerakan bot
                    SoundEffect.EAT_FOOD.play();

                    currentState = board.stepGame(botSeed, botRow, botCol);
                    updateScore();
                    repaint();
                    break;
                }
            }
        }
    }

    /** Inisialisasi papan permainan (dijalankan sekali) */
    public void initGame() {
        board = new Board();
    }

    /** Mereset papan dan state untuk ronde baru */
    public void newGame() {
        board.newGame();
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentState = State.PLAYING;
        roundScored = false;

        if (mode == GameMode.PVB) {
            boolean isPlayerCross = new Random().nextBoolean();
            if (isPlayerCross) {
                playerSeed = Seed.CROSS;
                botSeed = Seed.NOUGHT;
            } else {
                playerSeed = Seed.NOUGHT;
                botSeed = Seed.CROSS;
            }
            currentPlayer = Seed.CROSS;
            if (botSeed == Seed.CROSS) {
                botMove();
                currentPlayer = playerSeed;
            } else {
                currentPlayer = playerSeed;
            }
        } else if (mode == GameMode.PVP) {
            currentPlayer = Seed.CROSS;
        }
        repaint();
    }

    /** Logika untuk menggambar semua komponen di panel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g); // Menggambar papan

        // Menggambar efek highlight saat mouse di atas sel kosong
        if (currentState == State.PLAYING && hoveredRow != -1 && hoveredCol != -1) {
            if (board.cells[hoveredRow][hoveredCol].content == Seed.NO_SEED) {
                g.setColor(new Color(173, 216, 230, 128));
                g.fillRect(hoveredCol * Cell.SIZE, hoveredRow * Cell.SIZE, Cell.SIZE, Cell.SIZE);
            }
        }

        // Menampilkan pesan status di status bar
        // Menampilkan pesan status berdasarkan state permainan
        if (mode == GameMode.PVP) {
            if (currentState == State.PLAYING) { //saat bermain statusbarnya akan menampilkan tampilan satis berupa:
                statusBar.setText("Round " + (roundsPlayed + 1) + "  |  " + playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score" + "  |  " + ((currentPlayer == Seed.CROSS) ? playerXName + "'s Turn" : playerOName + "'s Turn"));
            } else if (currentState == State.DRAW) { //saat draw statusbar di bawah akan menampilkan its a draw...
                statusBar.setText("It's a Draw! Click to play again.( " + playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score)");
            } else if (currentState == State.CROSS_WON) { //saat X menang statusbar kaan menampilkan (nama player x) won beserta skornya
                statusBar.setText(playerXName + " Won! Click to play again. ( " + playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score)");
            } else if (currentState == State.NOUGHT_WON) { // Saat O menang statusbar akan menampilkan (nama player o) won beserta skornya
                statusBar.setText(playerOName + " Won! Click to play again. " + playerXName + "' score " + crossWins + " – " + noughtWins + " " + playerOName + "'s Score)");
            }
        } else {
            if (currentState == State.PLAYING) { //saat bermain statusbarnya akan menampilkan tampilan satis berupa:
                statusBar.setText("Round " + (roundsPlayed + 1) + "  |  " + playerXName + "' score " + playerWIns + " – " + botWin + " " + playerOName + "'s Score" + "  |  " + ((currentPlayer == Seed.CROSS) ? playerXName + "'s Turn" : playerOName + "'s Turn"));
            } else if (currentState == State.DRAW) { //saat draw statusbar di bawah akan menampilkan its a draw...
                statusBar.setText("It's a Draw! Click to play again.( " + playerXName + "' score " + playerWIns + " – " + botWin + " " + playerOName + "'s Score)");
            } else if (currentState == State.PLAYER_WON) { //saat X menang statusbar akan menampilkan (nama player x) won beserta skornya
                statusBar.setText(playerXName + " Won! Click to play again. ( " + playerXName + "' score " + playerWIns + " – " + botWin + " " + playerOName + "'s Score)");
            } else if (currentState == State.BOTWON) { // saat O menang statusbar akan menampilkan (nama player o) won beserta skornya
                statusBar.setText(playerOName + " Won! Click to play again. " + playerXName + "' score " + playerWIns + " – " + botWin + " " + playerOName + "'s Score)");
            }
        }
    }

        /** Mereset seluruh match, termasuk skor dan ronde */
        private void resetMatch () {
            // Menghentikan musik latar saat match selesai
            BackgroundMusicPlayer.stop();

            crossWins = 0;
            noughtWins = 0;
            roundsPlayed = 0;
            roundScored = false;
            playerWIns = 0;
            botWin = 0;
            newGame();

        }
    /** Memperbarui skor dan menampilkan pemenang ronde */
    private void updateScore() {
        if (roundScored || currentState == State.PLAYING)  return;   // Mencegah skor dihitung dua kaliAdd commentMore actions

        // Menyiapkan pesan pemenang ronde
        String roundWinnerMessage = ""; //deklarasi diluar agar dapat diisi dan dikeluarkan lagi
        if (mode == GameMode.PVP) {
            roundWinnerMessage = "";
            if (currentState == State.CROSS_WON) {
                roundWinnerMessage = playerXName + " is the winner of this round";
            } else if (currentState == State.NOUGHT_WON) {
                roundWinnerMessage = playerOName + " is the winner of this round";
            } else {
                roundWinnerMessage = "It is a Draw";
            }
        } else { //else artinya PVB karena selain PVP hanya ada PVB
            roundWinnerMessage = "";
            if (currentState == State.PLAYER_WON) {
                roundWinnerMessage = playerXName + " is the winner of this round";
            } else if (currentState == State.BOTWON) {
                roundWinnerMessage = playerOName + " is the winner of this round";
            } else {
                roundWinnerMessage = "It is a Draw";
            }

        }
            // Menambah skor pemenang
        if (mode == GameMode.PVP) {
            if (currentState == State.CROSS_WON) { //menambah crosswins/noughtwins sesuai hasil akhir sebuah ronde
                crossWins++;
            } else if (currentState == State.NOUGHT_WON) {
                noughtWins++;
            }
            roundsPlayed++;
            roundScored = true;
        } else { //untuk PVB karena hanya ada mode PVB selain PVP
            if (currentState == State.PLAYER_WON) { // menambah playerwin/botwin sesuai hasil akhir ronde
                playerWIns++;
            } else if (currentState == State.BOTWON) {
                botWin++;
            }
            roundsPlayed++;
            roundScored = true;
        }
            // Menampilkan pesan pop-up pemenang ronde
            JOptionPane.showMessageDialog(this, roundWinnerMessage, "Winner of round " + roundsPlayed,
                    JOptionPane.INFORMATION_MESSAGE);

        // Cek jika ada pemenang match (Best of 3)
        if (crossWins == ROUNDS_TO_WIN || noughtWins == ROUNDS_TO_WIN) { // Jika skor salah satu antara x atau o sudah 2 maka dapat di runAdd commentMore actions
            String champ = (crossWins == ROUNDS_TO_WIN) ? playerXName : playerOName;
            JOptionPane.showMessageDialog(this, champ + " Won! " +
                            "Final Score: " + playerXName + " " + crossWins + " – " + noughtWins + " " + playerOName, "Match Finished",
                    JOptionPane.INFORMATION_MESSAGE); // Menampilkan skor final dab pemenangnya
            int option = JOptionPane.showOptionDialog(this, "Game over! \n Do you want to play again?", "Play Again?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, //memunculkan joptionpane setelahnya untuk menanyakan apakah ingin bermain lagi atau keluar apliaksi
                    null,
                    new String[]{"Replay", "Quit"},
                    "Replay");

            if (option == JOptionPane.YES_OPTION) {
                resetMatch();
            } else {
                System.exit(0);  // Keluar dari aplikasi
            }
        } else if (playerWIns == ROUNDS_TO_WIN || botWin == ROUNDS_TO_WIN) {
                String champ = (playerWIns == ROUNDS_TO_WIN) ? playerXName : playerOName;
                JOptionPane.showMessageDialog(this, "Final Score: " + playerXName + " " + playerWIns + " – " + botWin + " " + playerOName, "Match Finished",
                        JOptionPane.INFORMATION_MESSAGE);

                int option = JOptionPane.showOptionDialog(this,
                        "Game over! \nDo you want to play again?", "Play Again?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Replay", "Quit"},
                        "Replay");

                if (option == JOptionPane.YES_OPTION) {
                    resetMatch();// Restart match

                } else {
                        System.exit(0);  // Keluar dari aplikasi
                }
        }
    }
    /** Metode utama untuk menjalankan aplikasi */
    public static void main(String[] args) {
        // Inisialisasi semua file sound effect dan musik latar
        SoundEffect.initGame();
        BackgroundMusicPlayer.init("audio/Lagu.wav");

        // Menjalankan GUI di thread yang aman
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
//                frame.setContentPane(new StartScreen());
                frame.setContentPane(new LoginScreen(frame));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}