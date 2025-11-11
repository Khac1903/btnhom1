import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Paddle extends MoveObject {
    private BufferedImage paddleImage;

    private final int ORIGINAL_WIDTH = 100;
    private final int MAX_WIDTH = 150;
    private final int MIN_WIDTH = 50;


    // Constructor tự load ảnh
    public Paddle(int x, int y, int width, int height, Color color) {
        super(x, color, 0, 0, height, width, y);
        try {
            this.paddleImage = ImageIO.read(new File("images/paddle.png")); // giống Ball
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
        if (x <= 0) x = 0;
        if (x >= panelWidth - width) x = panelWidth - width;
    }

    public void expand(){
        int oldX = x;
        width = Math.min(width + 25, MAX_WIDTH);
        x = oldX - (25/2);
    }

    public void shrink(){
        width = Math.max(width - 25, MIN_WIDTH);
    }

    public void resetWidth(){
        width = ORIGINAL_WIDTH;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) dx = -5;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) dx = 5;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
            dx = 0;
    }
}