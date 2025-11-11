import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    final int PANEL_WIDTH = 800;
    final int PANEL_HEIGHT = 600;


    private Paddle paddle;
    private Ball ball;
    private BrickMap brickMap;
    private Timer timer;
    private GameState gameState;
    private ScoreManager scoreManager;
    private LevelManager levelManager;
    private GameManager gameManager;
    private GameRenderer gameRenderer;
    private ArrayList<PowerUp> powerUps;
    private Info info;
    private HighScoreManager highScoreManager;

    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());

        highScoreManager = new HighScoreManager();
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        gameManager = new GameManager(gameState);
        gameRenderer = new GameRenderer();
        info = new Info();
        powerUps = new ArrayList<>();

        askPlayerName();


        timer = new Timer(10, this);
        timer.start();
    }

    private void askPlayerName() {
        String name = JOptionPane.showInputDialog(
                this,
                "Nhập tên của bạn:",
                "Chào mừng!",
                JOptionPane.PLAIN_MESSAGE
        );
        if (name == null || name.trim().isEmpty()) {
            name = "Player";
        }
        int currentHighScore = highScoreManager.getHighScore(name);
        info.setPlayerInfo(name, currentHighScore);
    }

    public void startGame() {
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
        powerUps.clear();
        ball.stickToPaddle(paddle);
        brickMap = new BrickMap(levelManager.getLevel());
        gameState.setStatus(GameStatus.READY);
    }

    public void resetAfterLifeLost() {
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        powerUps.clear();
        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
        ball.stickToPaddle(paddle);
        gameState.setStatus(GameStatus.READY);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.render(g, gameManager, PANEL_WIDTH, PANEL_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        gameManager.update(getWidth(), getHeight());
        repaint();
    }

    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            int keyCode = e.getKeyCode();

            // ----- MENU CHÍNH -----
            if (gameState.isMenu()) {
                if (keyCode == KeyEvent.VK_UP) {
                    gameManager.moveMenuSelectionUp();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    gameManager.moveMenuSelectionDown();
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    gameManager.selectMenuOption();
                }
                return;
            }


            // ----- MENU TẠM DỪNG -----
            if (gameState.isPaused()) {
                if (keyCode == KeyEvent.VK_UP) {
                    gameManager.movePauseMenuUp();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    gameManager.movePauseMenuDown();
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    gameManager.selectPauseMenuOption();
                }
                return;
            }

            // ----- MENU GAME OVER -----
            if (gameState.isGameOver()) {
                if (keyCode == KeyEvent.VK_UP) {
                    gameManager.moveGameOverMenuUp();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    gameManager.moveGameOverMenuDown();
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    gameManager.selectGameOverOption();
                }
                return;
            }

            // ----- TRONG GAME -----
            if (gameState.isReady() || gameState.isRunning()) {
                Paddle currentPaddle = gameManager.getPaddle();
                if (currentPaddle != null) {
                    if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                        currentPaddle.keyPressed(e);
                    }
                }

                if (keyCode == KeyEvent.VK_UP && gameState.isReady()) {
                    Ball currentBall = gameManager.getBall();
                    if (currentBall != null) {
                        currentBall.launch();
                        gameState.setStatus(GameStatus.RUNNING);
                    }
                }
            }

            // ----- TẠM DỪNG / TIẾP TỤC -----
            if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_P) {
                if (gameState.isRunning()) {
                    gameState.setStatus(GameStatus.PAUSED);
                } else if (gameState.isPaused()) {
                    gameState.setStatus(GameStatus.RUNNING);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            if (gameState.isRunning() || gameState.isReady()) {
                Paddle paddle = gameManager.getPaddle();
                if (paddle != null) {
                    paddle.keyReleased(e);
                }
            }
        }

        @Override

        public void keyTyped(KeyEvent e) { }
    }
}
