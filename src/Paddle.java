import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Paddle extends MoveObject {
    private BufferedImage paddleImage;

    // Constructor tự load ảnh
    public Paddle(int x, int y, int width, int height, Color color) {
        super(x, color, 0, 0, height, width, y);
        try {
            this.paddleImage = ImageIO.read(new File(GameConstants.IMAGE_PADDLE));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh paddle!");
            paddleImage = null;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (paddleImage != null) {
            g.drawImage(paddleImage, x, y, width, height, null);
        } else {
            super.draw(g); // fallback: hình chữ nhật
        }
    }

    @Override
    public void move(int panelWidth, int panelHeight) {
        x += dx;
        if (x <= GameConstants.ZERO) x = GameConstants.ZERO;
        if (x >= panelWidth - width) x = panelWidth - width;
    }

    public void expand(){
        int oldX = x;
        width = Math.min(width + GameConstants.PADDLE_WIDTH_CHANGE, GameConstants.PADDLE_MAX_WIDTH);
        x = oldX - (GameConstants.PADDLE_WIDTH_CHANGE / 2);
    }

    public void shrink(){
        width = Math.max(width - GameConstants.PADDLE_WIDTH_CHANGE, GameConstants.PADDLE_MIN_WIDTH);
    }

    public void resetWidth(){
        width = GameConstants.PADDLE_ORIGINAL_WIDTH;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) dx = -GameConstants.PADDLE_SPEED;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) dx = GameConstants.PADDLE_SPEED;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
            dx = 0;
    }
}