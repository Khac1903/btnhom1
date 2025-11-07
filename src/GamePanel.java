import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    final int PANEL_WIDTH = 800;
    final int PANEL_HEIGHT = 600;


    Paddle paddle;
    Ball ball;
    Timer timer;
    GameState gameState;
    BrickMap brickMap;
    ScoreManager scoreManager;
    LevelManager levelManager;
    ArrayList<PowerUp> powerUps;
    Info info;
    HighScoreManager highScoreManager;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new GameKeyAdapter());


        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());

        highScoreManager = new HighScoreManager();
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        info = new Info();

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
        gameState.isReady();
    }

    public void resetAfterLifeLost() {
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        powerUps.clear();
        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
        ball.stickToPaddle(paddle);
        gameState.isReady();
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (PANEL_WIDTH - metrics.stringWidth("Game Over")) / 2, PANEL_HEIGHT / 2);
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String startMessage = "Nhấn Enter để bắt đầu";
        g.drawString(startMessage, (PANEL_WIDTH - metrics.stringWidth(startMessage)) / 2, PANEL_HEIGHT / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (gameState.getStatus()) {
            case MENU:
                drawStartScreen(g);
                break;
            case READY:
            case RUNNING:
                paddle.draw(g);
                ball.draw(g);
                brickMap.draw(g);
                break;
            case GAME_OVER:
                drawGameOver(g);
                break;
        }
        scoreManager.draw(g);
        levelManager.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameState.isRunning() || gameState.isReady()) {
            //int brickDefault = brickMap.getTotalBricks();
            paddle.move(this.getWidth(), this.getHeight());
            if (gameState.isReady()) {
                ball.stickToPaddle(paddle);
            } else if (gameState.isRunning()) {
                ball.updatePosition();
                ball.handleWallCollision(this.getWidth());
                ball.handlePaddleCollision(paddle);
                if (ball.isOutOfBounds(this.getHeight())) {
                    info.loseLife();
                    if (info.hasLive()) {
                        resetAfterLifeLost();
                    } else {
                        highScoreManager.updateScore(info.getPlayerName(), scoreManager.getScore());
                        info.setHighScore(highScoreManager.getHighScore(info.getPlayerName()));
                        gameState.setStatus(GameStatus.GAME_OVER);
                    }
                }
                int brokenBricks = brickMap.handleBallCollision(ball, powerUps);

                if (brokenBricks > 0) {
                    scoreManager.increaseScore();
                }
                if (brickMap.isLevelComplete()) {
                    levelManager.nextLevel();
                    startGame();
                    gameState.setStatus(GameStatus.READY);
                }
            }
            repaint();
        }
    }

    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                if (gameState.isReady() || gameState.isRunning()) {
                    paddle.keyPressed(e);
                }
                return; // không xử lý phím khác khi đang ở menu
            }
            if (keyCode == KeyEvent.VK_UP) {
                if (gameState.isReady()) {
                    ball.launch();
                    gameState.setStatus(GameStatus.RUNNING);
                }
            }
            if (keyCode == KeyEvent.VK_ENTER) {
                if (gameState.isMenu() || gameState.isGameOver()) {
                    if (gameState.isGameOver()) {
                        info.reset();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            if (gameState.isRunning() || gameState.isReady()) {
                paddle.keyReleased(e);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}
