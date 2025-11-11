import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Paddle extends MoveObject {
    private BufferedImage paddleImage;
    private BufferedImage defaultImage;
    private int defaultWidth;

    // Constructor tự load ảnh
    public Paddle(int x, int y, int width, int height, Color color) {
        super(x, color, 0, 0, height, width, y);
        defaultWidth = width;
        try {

            this.defaultImage = ImageIO.read(new File("images/paddle.png")); // giống Ball
            this.paddleImage = defaultImage;
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

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) dx = -5;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) dx = 5;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
            dx = 0;
    }

    public void applyPowerUp(PowerUpType type) {
        try {
            switch (type) {
                case PADDLE_WIDE:
                    setImage(ImageIO.read(new File("images/paddle_wide.png")));
                    width = Math.min(300, width + 50);
                    break;

                case PADDLE_NARROW:
                    setImage(ImageIO.read(new File("images/paddle_narrow.png")));
                    width = Math.max(40, width - 40);
                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setImage(BufferedImage read) {
        this.paddleImage = read;
    }

    public void removePowerUp(PowerUpType type) {
        switch (type) {
            case PADDLE_WIDE:
            case PADDLE_NARROW:
                resetImage();
                setWidth(100);
                break;

            default:
                break;
        }
    }

    private void setWidth(int width) {
        this.width = width;
    }

    private void resetImage() {
        this.paddleImage = defaultImage;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
