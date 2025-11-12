import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements ActionListener {
    final int PANEL_WIDTH = 800;
    final int PANEL_HEIGHT = 600;


    private Map<PowerUpType, Integer> powerUpTimers = new HashMap<>();
    Paddle paddle;
    Ball ball;
    BrickMap brickMap;
    Timer timer;
    GameState gameState;
    ScoreManager scoreManager;
    LevelManager levelManager;
    GameManager gameManager;
    GameRenderer gameRenderer;
    ArrayList<PowerUp> powerUps;
    Info info;
    HighScoreManager highScoreManager;

    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);

        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());

        powerUps = new ArrayList<>();
        powerUpTimers = new HashMap<>();
        highScoreManager = new HighScoreManager();
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        gameManager = new GameManager(gameState);
        gameRenderer = new GameRenderer();
        info = new Info();
        askPlayerName();
        initGame();


        timer = new Timer(10, this);
        timer.start();
    }

    private void initGame() {

        paddle = new Paddle(300, 450, 100, 20, Color.BLUE);
        ball = new Ball(350, 400, 15, 2, -3, Color.RED);
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

    private void updateGame() {
        ball.updatePosition();
        paddle.move(getWidth(), getHeight());

        // 👉 Đặt vòng for xử lý PowerUp ở đây:
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            p.move(); // cho rơi xuống

            // Nếu rơi khỏi màn hình -> xoá
            if (p.isOffScreen(getHeight())) {
                powerUps.remove(i);
                continue;
            }

            // Nếu nhặt được
            if (p.getBounds().intersects(paddle.getBounds())) {
                applyPowerUp(p.getType()); // kích hoạt hiệu ứng
                powerUps.remove(i);
            }
        }
    }

    public void resetAfterLifeLost() {
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        powerUps.clear();
        ball = new Ball(400, 300, 20, 2, -3, Color.YELLOW);
        ball.stickToPaddle(paddle);
        gameState.isReady();
    }

//    public void startGame(){
//        paddle = new Paddle(350, 500, 100, 15, Color.WHITE,paddleImage);
//        ball = new Ball(400,300,20,2,-3,Color.YELLOW,ballImage);
//        ball.stickToPaddle(paddle);
//        brickMap = new BrickMap(levelManager.getLevel());
//    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (PANEL_WIDTH - metrics.stringWidth("Game Over")) / 2, PANEL_HEIGHT / 2);
    }

//    private void drawStartScreen(Graphics g){
//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Arial", Font.BOLD, 30));
//        FontMetrics metrics = getFontMetrics(g.getFont());
//        String startMessage = "Nhấn Enter để bắt đầu";
//        g.drawString(startMessage, (PANEL_WIDTH - metrics.stringWidth(startMessage)) / 2, PANEL_HEIGHT / 2);
//    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Thay thế toàn bộ khối switch/case hiện tại (chỉ giữ lại 3 dòng cuối) bằng
        gameRenderer.render(g, gameManager, PANEL_WIDTH, PANEL_HEIGHT);

        // Xóa hoặc làm mờ đoạn code này nếu nó gây ra lỗi trùng lặp hiển thị:
    /*
    scoreManager.draw(g);
    levelManager.draw(g);
    */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePowerUps();
        updateGame();
        repaint();
        gameManager.update(getWidth(), getHeight());
        repaint();
    }

    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            // ----- XỬ LÝ MENU CHÍNH -----
            if (gameState.isMenu()) {
                if (keyCode == KeyEvent.VK_UP) {
                    gameManager.moveMenuSelectionUp();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    gameManager.moveMenuSelectionDown();
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    gameManager.selectMenuOption();
                }
                return; // Dừng tại đây, không xử lý phím khác khi đang ở menu
            }

            // ----- XỬ LÝ MENU TẠM DỪNG -----
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

            // ----- XỬ LÝ MENU GAME OVER -----
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

            // ----- XỬ LÝ KHI GAME ĐANG READY HOẶC RUNNING -----
            if (gameState.isReady() || gameState.isRunning()) {
                Paddle currentPaddle = gameManager.getPaddle(); // ✅ lấy paddle từ GameManager (an toàn hơn)
                if (currentPaddle != null) {
                    if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                        currentPaddle.keyPressed(e);
                    }
                }

                // ----- NÚT LÊN ĐỂ PHÓNG BÓNG -----
                if (keyCode == KeyEvent.VK_UP && gameState.isReady()) {
                    Ball currentBall = gameManager.getBall();
                    if (currentBall != null) {
                        currentBall.launch();
                        gameState.setStatus(GameStatus.RUNNING);
                    }
                }

            }

            // ----- PHÍM SPACE ĐỂ TẠM DỪNG / TIẾP TỤC -----
            if (keyCode == KeyEvent.VK_SPACE) {
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
        public void keyTyped(KeyEvent e) {
        }
    }

    private void applyPowerUp(PowerUpType type) {
        int extraTime = 5 * 60; // ví dụ 5 giây * 60 FPS = 300 đơn vị
        int newTime = powerUpTimers.getOrDefault(type, 0) + extraTime;
        powerUpTimers.put(type, newTime);

        if (type == PowerUpType.PADDLE_WIDE || type == PowerUpType.PADDLE_NARROW)
            paddle.applyPowerUp(type);
        else
            ball.applyPowerUp(type);
    }


    private void deactivateEffect(PowerUpType type) {
        switch (type) {
            case PADDLE_WIDE:
            case PADDLE_NARROW:
                paddle.removePowerUp(type);
                break;
            case BALL_FAST:
            case MULTI_BALL:
                ball.removePowerUp(type);
                break;
        }
    }

    // Giả sử gọi mỗi frame (~16ms)
    private void updatePowerUps() {
        // Lặp ngược để vừa cập nhật vừa xóa khỏi ArrayList
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);

            // Di chuyển PowerUp xuống
            p.move();

            // Nếu PowerUp ra khỏi màn hình, xóa khỏi danh sách
            if (p.isOffScreen(getHeight()) || !p.isVisible()) {
                powerUps.remove(i);
                continue;
            }

            // Nếu PowerUp đang active (đang có thời gian còn lại)
            PowerUpType type = p.getType();
            if (powerUpTimers.containsKey(type)) {
                int timeLeft = powerUpTimers.get(type) - 1; // giảm 1 đơn vị
                if (timeLeft <= 0) {
                    // Hết thời gian → remove effect và khỏi map
                    powerUpTimers.remove(type);
                    deactivateEffect(type); // Ball/Paddle reset
                } else {
                    powerUpTimers.put(type, timeLeft);
                }
            }
        }
    }


}