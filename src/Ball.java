import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Ball extends MoveObject {
    private BufferedImage image;

    public Ball(int x, int y, int size, int dx, int dy, Color color, BufferedImage image) {
        super(x, color, dy, dx, size, size, y);
        this.image = image;
    }

    // Constructor phụ: tự load ảnh nếu không truyền vào
    public Ball(int x, int y, int size, int dx, int dy, Color color) {
        super(x, color, dy, dx, size, size, y);
        try {
            this.image = ImageIO.read(new File("images/c.png")); // đổi đường dẫn nếu cần
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng!");
            e.printStackTrace();
            this.image = null;
        }
    }

    /**
     * cap nhat vi tri.
     */
    public void updatePosition() {
        x += dx;
        y += dy;
    }

    public void reverseY() {
        dy = -dy;
    }

    public void reverseX() {
        dx = -dx;
    }

    public boolean isOutOfBounds(int panelHeight) {
        return y >= panelHeight;
    }

    public void stickToPaddle(Paddle paddle) {
        this.x = paddle.x + (paddle.width) / 2 - (this.width / 2);
        this.y = paddle.y - this.height;
    }

    public void launch() {
        this.dx = 2;
        this.dy = -3;
    }

    public void changeSpeed(double multiplier){
        dy *= multiplier;
        dx *= multiplier;
    }


    // 🎨 Vẽ bóng (ảnh nếu có, không thì vẽ hình tròn)
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }
}
