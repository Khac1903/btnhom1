import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GameManager implements ActionListener {
    private Paddle paddle;
    private Ball ball;
    private BrickMap brickMap;
    private GameState gameState;
    private ScoreManager scoreManager;
    private LevelManager levelManager;
    private PlayerManager playerManager;
    private int selectedMenuIndex = 0;
    private int pauseMenuIndex = 0;
    private int gameOverMenuIndex = 0;
    private  ArrayList<PowerUp> powerUps;




    public GameManager(GameState sharedState) {
        this.gameState = sharedState;
        gameState.setStatus(GameStatus.MENU);
        resetGameObjects();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        playerManager = new PlayerManager();
        powerUps = new ArrayList<>();

        // 🔹 Preload âm thanh
        SoundManager.loadSound("gameover", "src/sounds/gameover.wav");
        SoundManager.loadSound("loselife", "src/sounds/loselives.wav");
        SoundManager.loadSound("ball_hit", "src/sounds/ball_hit_brick.wav");
    }


    private void resetGameObjects() {
        ball = new Ball(390, 450, 20, 20, 4, Color.RED);
        paddle = new Paddle(350, 550, 100, 15, Color.GREEN);
        brickMap = new BrickMap(1);

        scoreManager = new ScoreManager();
        playerManager = new PlayerManager();
        levelManager = new LevelManager();
    }

    public void startGame() {
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
        ball.stickToPaddle(paddle);
        brickMap = new BrickMap(levelManager.getLevel());
        gameState.setStatus(GameStatus.READY);
    }

    public void update(int width, int height) {
        if (gameState.isReady() || gameState.isRunning()) {
            if(gameState.isPaused()) return;
            paddle.move(width, height);

            if (gameState.isReady()) {
                ball.stickToPaddle(paddle);
            } else if (gameState.isRunning()) {
                ball.updatePosition();
                CollisionManager.getInstance().checkCollision(ball, paddle, brickMap, width, height, powerUps);


                if (ball.isOutOfBounds(height)) {
                    playerManager.loseLife();
                    if (playerManager.isOutOfLives()) {
                        SoundManager.playSoundAsync("gameover");
                        gameState.setStatus(GameStatus.GAME_OVER);
                    } else {
                        SoundManager.playSoundAsync("loselife");
                        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
                        ball.stickToPaddle(paddle);
                        gameState.setStatus(GameStatus.READY);
                    }
                }

                int broken = CollisionManager.getInstance().handleBallCollision(ball, brickMap, powerUps);
                if (broken > 0) scoreManager.increaseScore();

                if (brickMap.isLevelComplete()) {
                    levelManager.nextLevel();
                    startGame();
                }
            }
        }
    }

    public void reset() {
        scoreManager.reset();
        levelManager.reset();
        playerManager.reset();
        startGame();
    }

    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public BrickMap getBrickMap() { return brickMap; }
    public GameState getGameState() { return gameState; }
    public ScoreManager getScoreManager() { return scoreManager; }
    public LevelManager getLevelManager() { return levelManager; }
    public PlayerManager getPlayerManager() { return playerManager; }

    public int getSelectedMenuIndex() {
        return selectedMenuIndex;
    }

    public void moveMenuSelectionUp() {
        selectedMenuIndex--;
        if (selectedMenuIndex < 0) selectedMenuIndex = 2;
    }

    public void moveMenuSelectionDown() {
        selectedMenuIndex++;
        if (selectedMenuIndex > 2) selectedMenuIndex = 0;
    }

    public void selectMenuOption() {
        switch (selectedMenuIndex) {
            case 0: // Start Game
                reset();
                gameState.setStatus(GameStatus.READY);
                break;
            case 1: // How to Play
                JOptionPane.showMessageDialog(null,
                        "Cách chơi:\n- Dùng ← → để di chuyển thanh đỡ\n- ↑ để phóng bóng\n- P để tạm dừng",
                        "Hướng dẫn",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2: // Exit
                System.exit(0);
                break;
        }
    }

    public int getPauseMenuIndex() {
        return pauseMenuIndex;
    }

    public void movePauseMenuUp() {
        pauseMenuIndex--;
        if (pauseMenuIndex < 0) pauseMenuIndex = 2;
    }

    public void movePauseMenuDown() {
        pauseMenuIndex++;
        if (pauseMenuIndex > 2) pauseMenuIndex = 0;
    }

    public void selectPauseMenuOption() {
        switch (pauseMenuIndex) {
            case 0: // Resume
                gameState.setStatus(GameStatus.RUNNING);
                break;

            case 1: // Restart
                reset();
                break;

            case 2: // Exit to Main Menu
                reset();
                gameState.setStatus(GameStatus.MENU);
                break;
        }
    }

    public int getGameOverMenuIndex() {
        return gameOverMenuIndex;
    }

    public void moveGameOverMenuUp() {
        gameOverMenuIndex--;
        if (gameOverMenuIndex < 0) gameOverMenuIndex = 1;
    }

    public void moveGameOverMenuDown() {
        gameOverMenuIndex++;
        if (gameOverMenuIndex > 1) gameOverMenuIndex = 0;
    }

    public void selectGameOverOption() {
        switch (gameOverMenuIndex) {

            case 0: // Return to Main Menu
                reset();
                gameState.setStatus(GameStatus.MENU);
                break;

            case 1: // Exit Game
                System.exit(0);
                break;
        }
    }


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}
