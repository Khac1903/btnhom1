import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    BufferedImage ballImage;





    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);

        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());

        gameState = new GameState();
        scoreManager = new ScoreManager();
        levelManager = new LevelManager();
        try {
            ballImage = ImageIO.read(new File("images/ball.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng!");
            e.printStackTrace();
            ballImage = null;
        }

        timer = new Timer(10, this);
        timer.start();
    }
    public void startGame(){
        paddle = new Paddle(350, 500, 100, 15, Color.WHITE);
        ball = new Ball(400,300,20,2,-3,Color.YELLOW,ballImage);
        ball.stickToPaddle(paddle);
        brickMap = new BrickMap(levelManager.getLevel());
    }

    private void drawGameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (PANEL_WIDTH - metrics.stringWidth("Game Over")) / 2, PANEL_HEIGHT / 2);
    }

    private void drawStartScreen(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String startMessage = "Nhấn Enter để bắt đầu";
        g.drawString(startMessage, (PANEL_WIDTH - metrics.stringWidth(startMessage)) / 2, PANEL_HEIGHT / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (gameState.getStatus()){
            case MENU :
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
            } else if(gameState.isRunning()){
                ball.updatePosition();
                ball.handleWallCollision(this.getWidth());
                ball.handlePaddleCollision(paddle);
                if (ball.isOutOfBounds(this.getHeight())) {
                    gameState.setStatus(GameStatus.GAME_OVER);
                }
                int brokenBricks = brickMap.handleBallCollision(ball);
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
            if(keyCode == KeyEvent.VK_LEFT||keyCode == KeyEvent.VK_RIGHT){
                if(gameState.isReady()||gameState.isRunning()){
                    paddle.keyPressed(e);
                }
            }
            if(keyCode == KeyEvent.VK_UP){
                if(gameState.isReady()){
                    ball.launch();
                    gameState.setStatus(GameStatus.RUNNING);
                }
            }
            if(keyCode == KeyEvent.VK_ENTER){
                if(gameState.isMenu()||gameState.isGameOver()){
                    if(gameState.isGameOver()){
                        scoreManager.reset();
                        levelManager.reset();
                    }
                    startGame();
                    gameState.setStatus(GameStatus.READY);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(gameState.isRunning()||gameState.isReady()){
                paddle.keyReleased(e);
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}