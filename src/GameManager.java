import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameManager implements ActionListener {
    private Paddle paddle;
    private ArrayList<Ball> ball;
    private BrickMap brickMap;
    private GameState gameState;
    private ScoreManager scoreManager;
    private LevelManager levelManager;
    private PlayerManager playerManager;
    private HighScoreManager highScoreManager;
    private String playerName = "Player";
    private int selectedMenuIndex = 0;
    private int pauseMenuIndex = 0;
    private int gameOverMenuIndex = 0;
    private ArrayList<PowerUp> powerUps;

    public GameManager(GameState sharedState) {
        this.gameState = sharedState;
        gameState.setStatus(GameStatus.MENU);
        resetGameObjects();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        playerManager = new PlayerManager();
        highScoreManager = new HighScoreManager();
        powerUps = new ArrayList<>();

        askPlayerName();
    }

    private void askPlayerName() {
        String name = JOptionPane.showInputDialog(
                null,
                "Nhập tên của bạn:",
                "Chào mừng đến Arkanoid!",
                JOptionPane.PLAIN_MESSAGE
        );
        if (name != null && !name.trim().isEmpty()) {
            playerName = name.trim();
        } else {
            playerName = "Player";
        }

        // Cập nhật high score cho người chơi
        playerManager.setPlayerInfo(playerName, highScoreManager.getHighScore(playerName));
    }

    private void resetGameObjects() {
        ball = new ArrayList<>();
        ball.add(new Ball(GameConstants.BALL_INITIAL_X_ALT, GameConstants.BALL_INITIAL_Y_ALT, 
                         GameConstants.BALL_SIZE, GameConstants.BALL_SIZE, GameConstants.BALL_DY_ALT, 
                         GameConstants.BALL_COLOR_ALT));
        paddle = new Paddle(GameConstants.PADDLE_INITIAL_X, GameConstants.PADDLE_INITIAL_Y_ALT, 
                           GameConstants.PADDLE_ORIGINAL_WIDTH, GameConstants.PADDLE_HEIGHT, 
                           GameConstants.PADDLE_COLOR_ALT);
        brickMap = new BrickMap(GameConstants.ONE);
        powerUps = new ArrayList<>();

        scoreManager = new ScoreManager();
        playerManager = new PlayerManager();
        levelManager = new LevelManager();
    }

    public void startGame() {
        paddle = new Paddle(GameConstants.PADDLE_INITIAL_X, GameConstants.PADDLE_INITIAL_Y, 
                           GameConstants.PADDLE_ORIGINAL_WIDTH, GameConstants.PADDLE_HEIGHT, 
                           GameConstants.PADDLE_COLOR);
        paddle.resetWidth();
        ball = new ArrayList<>();
        ball.clear();
        ball.add(new Ball(GameConstants.BALL_INITIAL_X, GameConstants.BALL_INITIAL_Y, 
                         GameConstants.BALL_SIZE, GameConstants.BALL_INITIAL_DX, 
                         GameConstants.BALL_INITIAL_DY, GameConstants.BALL_COLOR));
        ball.get(0).stickToPaddle(paddle);
        brickMap = new BrickMap(levelManager.getLevel());
        powerUps.clear();
        gameState.setStatus(GameStatus.READY);
    }

    public void update(int width, int height) {
        if (gameState.isReady() || gameState.isRunning()) {
            if (gameState.isPaused()) return;
            paddle.move(width, height);

            if (gameState.isReady()) {
                ball.get(0).stickToPaddle(paddle);
            } else if (gameState.isRunning()) {
                for (Ball b : ball) {
                    b.updatePosition();
                }

                ball.removeIf(b -> b.isOutOfBounds(height));

                if (ball.isEmpty()) {
                    playerManager.loseLife();
                    if (playerManager.isOutOfLives()) {
                        highScoreManager.updateScore(playerName, scoreManager.getScore());
                        SoundManager.playSound(GameConstants.SOUND_GAMEOVER);
                        gameState.setStatus(GameStatus.GAME_OVER);
                    } else {
                        SoundManager.playSound(GameConstants.SOUND_LOSE_LIVES);
                        ball.add(new Ball(GameConstants.BALL_INITIAL_X, GameConstants.BALL_INITIAL_Y, 
                                         GameConstants.BALL_SIZE, GameConstants.BALL_INITIAL_DX, 
                                         GameConstants.BALL_INITIAL_DY, GameConstants.BALL_COLOR));
                        ball.get(0).stickToPaddle(paddle);

                        gameState.setStatus(GameStatus.READY);
                    }
                }
                // Xử lý va chạm cho mỗi ball
                int totalBroken = 0;
                for (Ball b : ball) {
                    // Va chạm với tường (4 cạnh)
                    b.handleWallCollision(width, height);
                    
                    // Va chạm với paddle
                    b.handlePaddleCollision(paddle);
                    
                    // Va chạm với gạch
                    totalBroken += brickMap.handleBallCollision(b, powerUps);
                }
                
                // Tăng điểm dựa trên số gạch bị phá
                if (totalBroken > 0) {
                    scoreManager.increaseScore();
                }
                for(PowerUp pu : powerUps){
                    if(pu.isVisible()){
                        pu.move();
                    }
                    if (pu.getBounds().intersects(paddle.getBound())) { //
                        applyPowerUps(pu.getType()); // Kích hoạt hiệu ứng
                        pu.setVisible(false); // Ẩn đi
                    }
                }

                powerUps.removeIf(pu -> !pu.isVisible() || pu.isOffScreen(height));
                if (brickMap.isLevelComplete()) {
                    levelManager.nextLevel();
                    startGame();
                }
            }
        }
    }

    private void applyPowerUps(PowerUpType type) {
        switch (type) {
            case MULTI_BALL:
                if (!ball.isEmpty()) {
                    Ball existingBall = ball.get(0);
                    ball.add(new Ball((int) existingBall.x, (int) existingBall.y, 
                                     GameConstants.BALL_SIZE, GameConstants.BALL_INITIAL_DX, 
                                     GameConstants.BALL_INITIAL_DY, GameConstants.BALL_COLOR));
                    ball.add(new Ball((int) existingBall.x, (int) existingBall.y, 
                                     GameConstants.BALL_SIZE, GameConstants.BALL_DX_ALT, 
                                     GameConstants.BALL_INITIAL_DY, GameConstants.BALL_COLOR));
                }
                break;
            case PADDLE_WIDE:
                paddle.expand();
                break;
            case PADDLE_NARROW:
                paddle.shrink();
                break;
            case BALL_FAST:
                for (Ball b : ball) {
                    b.changeSpeed(GameConstants.POWERUP_SPEED_MULTIPLIER);
                }
                break;
        }
        SoundManager.playSound(GameConstants.SOUND_POWERUP);
    }

            public void reset () {
                scoreManager.reset();
                levelManager.reset();
                playerManager.reset();
                startGame();
            }

            public Paddle getPaddle () {
                return paddle;
            }
            public ArrayList<Ball> getBalls () {
                return ball;
            }
            public ArrayList<PowerUp> getPowerUps () {
                return powerUps;
            }
            public BrickMap getBrickMap () {
                return brickMap;
            }
            public GameState getGameState () {
                return gameState;
            }
            public ScoreManager getScoreManager () {
                return scoreManager;
            }
            public LevelManager getLevelManager () {
                return levelManager;
            }
            public PlayerManager getPlayerManager () {
                return playerManager;
            }
            public HighScoreManager getHighScoreManager () {
                return highScoreManager;
            }
            public String getPlayerName () {
                return playerName;
            }

            public int getSelectedMenuIndex () {
                return selectedMenuIndex;
            }

            public void moveMenuSelectionUp () {
                selectedMenuIndex--;
                if (selectedMenuIndex < GameConstants.ZERO) selectedMenuIndex = GameConstants.MENU_MAX_INDEX;
            }

            public void moveMenuSelectionDown () {
                selectedMenuIndex++;
                if (selectedMenuIndex > GameConstants.MENU_MAX_INDEX) selectedMenuIndex = GameConstants.ZERO;
            }

            public void selectMenuOption () {
                switch (selectedMenuIndex) {
                    case GameConstants.MENU_INDEX_START: // Start Game
                        reset();
                        gameState.setStatus(GameStatus.READY);
                        break;
                    case GameConstants.MENU_INDEX_TOP_PLAYERS: // Top 5 Players
                        showTop5Dialog();
                        break;
                    case GameConstants.MENU_INDEX_HOW_TO_PLAY: // How to Play
                        JOptionPane.showMessageDialog(null,
                                "Cách chơi:\n- Dùng ← → để di chuyển thanh đỡ\n- ↑ để phóng bóng\n- P để tạm dừng",
                                "Hướng dẫn",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case GameConstants.MENU_INDEX_EXIT: // Exit
                        highScoreManager.updateScore(playerName, scoreManager.getScore());
                        System.exit(GameConstants.ZERO);
                        break;
                }
            }

            private void showTop5Dialog () {
                java.util.List<HighScoreManager.ScoreEntry> top = highScoreManager.getTopScores(GameConstants.TOP_SCORES_COUNT);
                StringBuilder sb = new StringBuilder();
                if (top.isEmpty()) {
                    sb.append("Chưa có dữ liệu điểm cao.");
                } else {
                    for (int i = 0; i < top.size(); i++) {
                        HighScoreManager.ScoreEntry e = top.get(i);
                        sb.append(String.format("%d) %s - %d", i + GameConstants.ONE, e.name, e.score));
                        if (i < top.size() - GameConstants.ONE) sb.append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, sb.toString(), "Top 5 Players", JOptionPane.INFORMATION_MESSAGE);
            }

            public int getPauseMenuIndex () {
                return pauseMenuIndex;
            }

            public void movePauseMenuUp () {
                pauseMenuIndex--;
                if (pauseMenuIndex < GameConstants.ZERO) pauseMenuIndex = GameConstants.PAUSE_MENU_MAX_INDEX;
            }

            public void movePauseMenuDown () {
                pauseMenuIndex++;
                if (pauseMenuIndex > GameConstants.PAUSE_MENU_MAX_INDEX) pauseMenuIndex = GameConstants.ZERO;
            }

            public void selectPauseMenuOption () {
                switch (pauseMenuIndex) {
                    case GameConstants.PAUSE_MENU_INDEX_RESTART:
                        reset();
                        break;
                    case GameConstants.PAUSE_MENU_INDEX_RESUME:
                        gameState.setStatus(GameStatus.RUNNING);
                        break;
                    case GameConstants.PAUSE_MENU_INDEX_EXIT:
                        reset();
                        gameState.setStatus(GameStatus.MENU);
                        break;
                }
            }

            public int getGameOverMenuIndex () {
                return gameOverMenuIndex;
            }

            public void moveGameOverMenuUp () {
                gameOverMenuIndex--;
                if (gameOverMenuIndex < GameConstants.ZERO) gameOverMenuIndex = GameConstants.GAME_OVER_MENU_MAX_INDEX;
            }

            public void moveGameOverMenuDown () {
                gameOverMenuIndex++;
                if (gameOverMenuIndex > GameConstants.GAME_OVER_MENU_MAX_INDEX) gameOverMenuIndex = GameConstants.ZERO;
            }

            public void selectGameOverOption () {
                switch (gameOverMenuIndex) {
                    case GameConstants.GAME_OVER_MENU_INDEX_MAIN_MENU:
                        highScoreManager.updateScore(playerName, scoreManager.getScore());
                        reset();
                        gameState.setStatus(GameStatus.MENU);
                        break;
                    case GameConstants.GAME_OVER_MENU_INDEX_EXIT:
                        highScoreManager.updateScore(playerName, scoreManager.getScore());
                        System.exit(GameConstants.ZERO);
                        break;
                }
            }

            @Override
            public void actionPerformed (ActionEvent e){
            }
        }