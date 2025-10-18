import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Ball extends MoveObject {
    public Ball(int x, int y, int size, int dx, int dy, Color color, BufferedImage image){
        super(x, color, dy, dx, size, size, y);
        this.image = image;
    }

    // Constructor phụ: nếu muốn tự load ảnh trực tiếp từ file
    public Ball(int x, int y, int size, int dx, int dy, Color color) {
        super(x, color, dy, dx, size, size, y);
        try {
            this.image = ImageIO.read(new File("images/ball.png")); // đổi đường dẫn cho đúng
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng!");
            e.printStackTrace();
            this.image = null;
        }
    }

    public void updatePosition(){
        x += dx;
        y += dy;
    }

    public void handleWallCollision(int panelWidth) {
        if (x <= 0 || x >= panelWidth - width) {
            dx = -dx;
        }
        if (y <= 0) {
            dy = -dy;
        }
    }

    public void handlePaddleCollision(Paddle paddle) {
        if (this.getBound().intersects(paddle.getBound())) {
            dy = -dy;
            y = paddle.y - height;
        }
    }

    public void reverseDirection() {
        dy = -dy;
    }

    public boolean isOutOfBounds(int panelHeight) {
        return y >= panelHeight;
    }

    public void stickToPaddle(Paddle paddle){
        this.x = paddle.x + (paddle.width) / 2 - (this.width / 2);
        this.y = paddle.y - this.height;
    }

    public void launch(){
        this.dx = 2;
        this.dy = -3;
    }

    // 🎨 Hàm vẽ bóng (ảnh hoặc hình tròn nếu không có ảnh)
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }
}
