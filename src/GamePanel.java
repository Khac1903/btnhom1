import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {


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
        this.setPreferredSize(new Dimension(GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT));
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



        timer = new Timer(GameConstants.TIMER_DELAY, this);
        timer.start();
    }

    public void startGame() {
        paddle = new Paddle(GameConstants.PADDLE_INITIAL_X, GameConstants.PADDLE_INITIAL_Y, 
                           GameConstants.PADDLE_ORIGINAL_WIDTH, GameConstants.PADDLE_HEIGHT, 
                           GameConstants.PADDLE_COLOR);
        ball = new Ball(GameConstants.BALL_INITIAL_X, GameConstants.BALL_INITIAL_Y, 
                       GameConstants.BALL_SIZE, GameConstants.BALL_INITIAL_DX, 
                       GameConstants.BALL_INITIAL_DY, GameConstants.BALL_COLOR);
        powerUps.clear();
        ball.stickToPaddle(paddle);
        brickMap = new BrickMap(levelManager.getLevel());
        gameState.setStatus(GameStatus.READY);
    }

    public void resetAfterLifeLost() {
        paddle = new Paddle(GameConstants.PADDLE_INITIAL_X, GameConstants.PADDLE_INITIAL_Y, 
                           GameConstants.PADDLE_ORIGINAL_WIDTH, GameConstants.PADDLE_HEIGHT, 
                           GameConstants.PADDLE_COLOR);
        powerUps.clear();
        ball = new Ball(GameConstants.BALL_INITIAL_X, GameConstants.BALL_INITIAL_Y, 
                       GameConstants.BALL_SIZE, GameConstants.BALL_INITIAL_DX, 
                       GameConstants.BALL_INITIAL_DY, GameConstants.BALL_COLOR);
        ball.stickToPaddle(paddle);
        gameState.setStatus(GameStatus.READY);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.render(g, gameManager, GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT);
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
                    ArrayList<Ball> ballsToLaunch = gameManager.getBalls();
                    if (!ballsToLaunch.isEmpty()) {
                        for(Ball b : ballsToLaunch) {
                            b.launch();
                        }
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