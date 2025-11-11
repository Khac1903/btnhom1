import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    final int PANEL_WIDTH = 800;
    final int PANEL_HEIGHT = 600;

<<<<<<< HEAD
    private GameManager gameManager;
    private GameRenderer gameRenderer;
    Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new GameKeyAdapter());

        gameManager = new GameManager();
        gameRenderer = new GameRenderer();
=======

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

        highScoreManager = new HighScoreManager();
        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        gameManager = new GameManager(gameState);
        gameRenderer = new GameRenderer();
        info = new Info();
        askPlayerName();
>>>>>>> 84b6e8bb8aa00db7e58033b658bf4532aea774f7


        timer = new Timer(10, this);
        timer.start();
    }

<<<<<<< HEAD
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.render(g, gameManager, PANEL_WIDTH, PANEL_HEIGHT);
=======
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

//    public void startGame(){
//        paddle = new Paddle(350, 500, 100, 15, Color.WHITE,paddleImage);
//        ball = new Ball(400,300,20,2,-3,Color.YELLOW,ballImage);
//        ball.stickToPaddle(paddle);
//        brickMap = new BrickMap(levelManager.getLevel());
//    }

    private void drawGameOver(Graphics g){
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
>>>>>>> 84b6e8bb8aa00db7e58033b658bf4532aea774f7
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameManager.update(getWidth(), getHeight());
        repaint();
    }

    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
<<<<<<< HEAD
            int key = e.getKeyCode();
            GameState state = gameManager.getGameState();

            if (state.isMenu()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.moveMenuSelectionUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.moveMenuSelectionDown();
                } else if (key == KeyEvent.VK_ENTER) {
                    gameManager.selectMenuOption();
                }
                return;
            }

            if (state.isPaused()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.movePauseMenuUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.movePauseMenuDown();
                } else if (key == KeyEvent.VK_ENTER) {
                    gameManager.selectPauseMenuOption();
                } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_P) {
                    state.setStatus(GameStatus.RUNNING);
=======
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
>>>>>>> 84b6e8bb8aa00db7e58033b658bf4532aea774f7
                }
                return;
            }

<<<<<<< HEAD
            if (state.isGameOver()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.moveGameOverMenuUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.moveGameOverMenuDown();
                } else if (key == KeyEvent.VK_ENTER) {
=======
            // ----- XỬ LÝ MENU GAME OVER -----
            if (gameState.isGameOver()) {
                if (keyCode == KeyEvent.VK_UP) {
                    gameManager.moveGameOverMenuUp();
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    gameManager.moveGameOverMenuDown();
                } else if (keyCode == KeyEvent.VK_ENTER) {
>>>>>>> 84b6e8bb8aa00db7e58033b658bf4532aea774f7
                    gameManager.selectGameOverOption();
                }
                return;
            }

<<<<<<< HEAD
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                if (state.isReady() || state.isRunning()) {
                    gameManager.getPaddle().keyPressed(e);
                }
            }

            if (key == KeyEvent.VK_UP && state.isReady()) {
                gameManager.getBall().launch();
                state.setStatus(GameStatus.RUNNING);
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_P) {
                if (state.isRunning()) {
                    state.setStatus(GameStatus.PAUSED);
=======
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
>>>>>>> 84b6e8bb8aa00db7e58033b658bf4532aea774f7
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
<<<<<<< HEAD
            GameState state = gameManager.getGameState();
            if (state.isRunning() || state.isReady()) {
                gameManager.getPaddle().keyReleased(e);
=======
            if (gameState.isRunning() || gameState.isReady()) {
                Paddle paddle = gameManager.getPaddle();
                if (paddle != null) {
                    paddle.keyReleased(e);
                }
>>>>>>> 84b6e8bb8aa00db7e58033b658bf4532aea774f7
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}