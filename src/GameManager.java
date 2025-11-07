import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameManager implements ActionListener {
    private Paddle paddle;
    private Ball ball;
    private BrickMap brickMap;
    private GameState gameState;
    private ScoreManager scoreManager;
    private LevelManager levelManager;
    private PlayerManager playerManager;

    public GameManager() {
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        playerManager = new PlayerManager();
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
            paddle.move(width, height);

            if (gameState.isReady()) {
                ball.stickToPaddle(paddle);
            } else if (gameState.isRunning()) {
                ball.updatePosition();
                CollisionManager.getInstance().checkCollision(ball, paddle, brickMap, width);


                if (ball.isOutOfBounds(height)) {
                    playerManager.loseLife();
                    if (playerManager.isOutOfLives()) {
                        SoundManager.playSound("src/sounds/gameover.wav");
                        gameState.setStatus(GameStatus.GAME_OVER);
                    } else {
                        SoundManager.playSound("src/sounds/loselives.wav");
                        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
                        ball.stickToPaddle(paddle);
                        gameState.setStatus(GameStatus.READY);
                    }
                }

                int broken = brickMap.handleBallCollision(ball);
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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}
