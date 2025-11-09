import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager implements ActionListener {
    private Paddle paddle;
    private Ball ball;
    private List<Ball> balls;
    private BrickMap brickMap;
    private GameState gameState;
    private ScoreManager scoreManager;
    private LevelManager levelManager;
    private PlayerManager playerManager;
    private int selectedMenuIndex = 0;
    private List<PowerUp> activePowerUps;
    private int biggerPaddleTimer;
    private int originalPaddleWidth;
    private ParticleSystem particleSystem;
    private List<ScorePopup> scorePopups;
    private int comboCount;
    private int comboTimer;
    private int lastBrickBreakTime;
    private SnowSystem snowSystem;

    public GameManager() {
        gameState = new GameState();
        gameState.setStatus(GameStatus.MENU); // 👈 bắt đầu ở menu
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        playerManager = new PlayerManager();
        activePowerUps = new ArrayList<>();
        biggerPaddleTimer = 0;
        particleSystem = new ParticleSystem();
        scorePopups = new ArrayList<>();
        comboCount = 0;
        comboTimer = 0;
        snowSystem = new SnowSystem(800, 600);
        resetGameObjects();
    }

    private void resetGameObjects() {
        ball = new Ball(390, 450, 20, 20, 4, Color.RED);
        balls = new ArrayList<>();
        balls.add(ball);
        paddle = new Paddle(350, 550, 100, 15, Color.GREEN);
        originalPaddleWidth = 100;
        brickMap = new BrickMap(1);
        if (activePowerUps == null) {
            activePowerUps = new ArrayList<>();
        } else {
            activePowerUps.clear();
        }
        biggerPaddleTimer = 0;

        scoreManager = new ScoreManager();
        playerManager = new PlayerManager();
        levelManager = new LevelManager();
    }

    public void startGame() {
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        originalPaddleWidth = 100;
        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
        balls = new ArrayList<>();
        balls.add(ball);
        ball.stickToPaddle(paddle);
        brickMap = new BrickMap(levelManager.getLevel());
        activePowerUps.clear();
        biggerPaddleTimer = 0;
        gameState.setStatus(GameStatus.READY);
    }

    public void update(int width, int height) {
        if (gameState.isReady() || gameState.isRunning()) {
            if(gameState.isPaused()) return;
            paddle.move(width, height);

            // Update powerups
            updatePowerUps(width, height);

            // Update bigger paddle timer
            if (biggerPaddleTimer > 0) {
                biggerPaddleTimer--;
                if (biggerPaddleTimer == 0) {
                    paddle.setWidth(originalPaddleWidth);
                }
            }

            if (gameState.isReady()) {
                ball.stickToPaddle(paddle);
            } else if (gameState.isRunning()) {
                // Update all balls
                Iterator<Ball> ballIterator = balls.iterator();
                while (ballIterator.hasNext()) {
                    Ball currentBall = ballIterator.next();
                    currentBall.updatePosition();
                    currentBall.handleWallCollision(width);
                    currentBall.handlePaddleCollision(paddle);

                    if (currentBall.isOutOfBounds(height)) {
                        ballIterator.remove();
                        if (currentBall == ball && balls.size() > 0) {
                            ball = balls.get(0); // Use first remaining ball
                        }
                    }
                }

                // Check if all balls are lost
                if (balls.isEmpty()) {
                    playerManager.loseLife();
                    if (playerManager.isOutOfLives()) {
                        SoundManager.playSound("src/sounds/gameover.wav");
                        gameState.setStatus(GameStatus.GAME_OVER);
                    } else {
                        SoundManager.playSound("src/sounds/loselives.wav");
                        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
                        balls = new ArrayList<>();
                        balls.add(ball);
                        ball.stickToPaddle(paddle);
                        gameState.setStatus(GameStatus.READY);
                    }
                } else {
                    // Handle collisions for all balls
                    int totalBroken = 0;
                    int currentTime = (int)System.currentTimeMillis();
                    for (Ball currentBall : balls) {
                        int broken = brickMap.handleBallCollision(currentBall);
                        if (broken > 0) {
                            totalBroken += broken;
                            
                            // Check for combo (bricks broken within 1 second)
                            if (currentTime - lastBrickBreakTime < 1000 && lastBrickBreakTime > 0) {
                                comboCount += broken; // Add to combo
                                comboTimer = 120; // 2 seconds
                            } else {
                                comboCount = broken; // Start new combo
                                comboTimer = 120;
                            }
                            lastBrickBreakTime = currentTime;
                            
                            // Add Christmas particles (snow/glitter) at ball location
                            for (int i = 0; i < broken; i++) {
                                Color particleColor = new Color(
                                    220 + (int)(Math.random() * 35), // Red
                                    20 + (int)(Math.random() * 35),  // Red
                                    60 + (int)(Math.random() * 195)  // Red to white
                                );
                                particleSystem.addExplosion(
                                    currentBall.x + currentBall.width/2 + (int)(Math.random() * 20 - 10),
                                    currentBall.y + currentBall.height/2 + (int)(Math.random() * 20 - 10),
                                    particleColor,
                                    8
                                );
                            }
                            
                            // Calculate score with combo multiplier
                            int baseScore = 10 * broken;
                            int comboMultiplier = Math.min(comboCount / broken, 5); // Max 5x combo
                            if (comboMultiplier < 1) comboMultiplier = 1;
                            int finalScore = baseScore * comboMultiplier;
                            scoreManager.addScore(finalScore);
                            
                            // Add score popup
                            scorePopups.add(new ScorePopup(
                                currentBall.x,
                                currentBall.y - 20,
                                finalScore,
                                comboCount > broken ? Color.ORANGE : Color.WHITE
                            ));
                        }
                    }
                }
                
                // Update combo timer
                if (comboTimer > 0) {
                    comboTimer--;
                    if (comboTimer == 0) {
                        comboCount = 0;
                    }
                }
                
                // Update particles and score popups
                particleSystem.update();
                Iterator<ScorePopup> popupIterator = scorePopups.iterator();
                while (popupIterator.hasNext()) {
                    ScorePopup popup = popupIterator.next();
                    popup.update();
                    if (!popup.isAlive()) {
                        popupIterator.remove();
                    }
                }
                
                // Update snow system
                snowSystem.update();

                // Collect powerups from brickMap (only if there are new ones)
                List<PowerUp> newPowerUps = brickMap.getDroppedPowerUps();
                if (!newPowerUps.isEmpty()) {
                    for (PowerUp powerUp : newPowerUps) {
                        if (powerUp.isActive()) {
                            activePowerUps.add(powerUp);
                        }
                    }
                    brickMap.clearPowerUps();
                }

                if (brickMap.isLevelComplete()) {
                    levelManager.nextLevel();
                    startGame();
                }
            }
        }
    }

    private void updatePowerUps(int width, int height) {
        Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.updatePosition(height);

            if (!powerUp.isActive()) {
                iterator.remove();
            } else if (powerUp.checkPaddleCollision(paddle)) {
                applyPowerUp(powerUp.getType());
                iterator.remove();
            }
        }
    }

    private void applyPowerUp(PowerUpType type) {
        switch (type) {
            case BIGGER_PADDLE:
                originalPaddleWidth = paddle.width;
                paddle.setWidth(150);
                biggerPaddleTimer = 600; // 10 seconds at 60fps (600 frames at 16ms per frame)
                break;
            case EXTRA_LIFE:
                playerManager.gainLife();
                break;
            case MULTI_BALL:
                if (ball != null && balls.size() < 3) {
                    Ball newBall1 = new Ball(ball.x, ball.y, ball.width, -ball.getDx(), ball.getDy(), Color.YELLOW);
                    Ball newBall2 = new Ball(ball.x, ball.y, ball.width, ball.getDx(), -ball.getDy(), Color.YELLOW);
                    balls.add(newBall1);
                    balls.add(newBall2);
                }
                break;
            case SLOW_BALL:
                for (Ball b : balls) {
                    int newDx = (int)(b.getDx() * 0.7);
                    int newDy = (int)(b.getDy() * 0.7);
                    if (Math.abs(newDx) < 1) newDx = newDx > 0 ? 1 : -1;
                    if (Math.abs(newDy) < 1) newDy = newDy > 0 ? 1 : -1;
                    b.setDx(newDx);
                    b.setDy(newDy);
                }
                break;
            case FAST_BALL:
                for (Ball b : balls) {
                    b.setDx((int)(b.getDx() * 1.3));
                    b.setDy((int)(b.getDy() * 1.3));
                }
                break;
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
    public List<Ball> getBalls() { return balls; }
    public List<PowerUp> getActivePowerUps() { return activePowerUps; }
    public BrickMap getBrickMap() { return brickMap; }
    public ParticleSystem getParticleSystem() { return particleSystem; }
    public List<ScorePopup> getScorePopups() { return scorePopups; }
    public int getComboCount() { return comboCount; }
    public int getComboTimer() { return comboTimer; }
    public int getBiggerPaddleTimer() { return biggerPaddleTimer; }
    public SnowSystem getSnowSystem() { return snowSystem; }
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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}
