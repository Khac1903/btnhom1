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
    private HighScoreManager highScoreManager;
    private String playerName = "Player";
    private int selectedMenuIndex = 0;
    private int pauseMenuIndex = 0;
    private int gameOverMenuIndex = 0;

    public GameManager() {
        gameState = new GameState();
        gameState.setStatus(GameStatus.MENU); // 👈 bắt đầu ở menu

        resetGameObjects();
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        playerManager = new PlayerManager();
        highScoreManager = new HighScoreManager();
        
        // Yêu cầu tên người chơi khi vào menu lần đầu
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
                ball.handleWallCollision(width);
                ball.handlePaddleCollision(paddle);

                if (ball.isOutOfBounds(height)) {
                    playerManager.loseLife();
                    if (playerManager.isOutOfLives()) {
                        // Lưu điểm khi game over
                        highScoreManager.updateScore(playerName, scoreManager.getScore());
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
    public HighScoreManager getHighScoreManager() { return highScoreManager; }
    public String getPlayerName() { return playerName; }
    public int getSelectedMenuIndex() {
        return selectedMenuIndex;
    }

    public void moveMenuSelectionUp() {
        selectedMenuIndex--;
        if (selectedMenuIndex < 0) selectedMenuIndex = 3;
    }

    public void moveMenuSelectionDown() {
        selectedMenuIndex++;
        if (selectedMenuIndex > 3) selectedMenuIndex = 0;
    }

    public void selectMenuOption() {
        switch (selectedMenuIndex) {
            case 0: // Start Game
                reset();
                gameState.setStatus(GameStatus.READY);
                break;
            case 1: // Top 5 Players
                showTop5Dialog();
                break;
            case 2: // How to Play
                JOptionPane.showMessageDialog(null,
                        "Cách chơi:\n- Dùng ← → để di chuyển thanh đỡ\n- ↑ để phóng bóng\n- P để tạm dừng",
                        "Hướng dẫn",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case 3: // Exit
                // Cập nhật highest score trước khi thoát
                highScoreManager.updateScore(playerName, scoreManager.getScore());
                System.exit(0);
                break;
        }
    }

    private void showTop5Dialog() {
        java.util.List<HighScoreManager.ScoreEntry> top = highScoreManager.getTopScores(5);
        StringBuilder sb = new StringBuilder();
        if (top.isEmpty()) {
            sb.append("Chưa có dữ liệu điểm cao.");
        } else {
            for (int i = 0; i < top.size(); i++) {
                HighScoreManager.ScoreEntry e = top.get(i);
                sb.append(String.format("%d) %s - %d", i + 1, e.name, e.score));
                if (i < top.size() - 1) sb.append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Top 5 Players", JOptionPane.INFORMATION_MESSAGE);
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
            case 0: // Restart
                reset();
                break;

            case 1: // Resume
                gameState.setStatus(GameStatus.RUNNING);
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
                // Lưu điểm trước khi về menu
                highScoreManager.updateScore(playerName, scoreManager.getScore());
                reset();
                gameState.setStatus(GameStatus.MENU);
                break;

            case 1: // Exit Game
                // Cập nhật highest score trước khi thoát
                highScoreManager.updateScore(playerName, scoreManager.getScore());
                System.exit(0);
                break;
        }
    }


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }
}
