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
            this.image = ImageIO.read(new File("images/ball.png")); // đổi đường dẫn nếu cần
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng!");
            e.printStackTrace();
            this.image = null;
        }
    }

    public void updatePosition() {
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
        Rectangle ballRect = this.getBound();
        Rectangle paddleRect = paddle.getBound();
        if (ballRect.intersects(paddleRect) && dy > 0) {
            SoundManager.playSound("src/sounds/ball_hit_paddle.wav");

            double paddleCenter = paddle.x + paddle.width / 2.0;
            double ballCenter = x + width / 2.0;
            double offset = (ballCenter - paddleCenter) / (paddle.width / 2.0);

            double speed = Math.sqrt(dx * dx + dy * dy);
            double angle = Math.toRadians(60 * offset);
            double paddleInfluence = paddle.dx * 0.2;

            double newDx = speed * Math.sin(angle) + paddleInfluence;
            double newDy = -Math.abs(speed * Math.cos(angle));

            dx = (int)Math.round(newDx);
            dy = (int)Math.round(newDy);

            y = paddle.y - height;

            if (dx > 0) dx = Math.max(2, Math.min(3, dx));
            else dx = Math.min(-2, Math.max(-3, dx));

            if (dy > 0) dy = Math.max(2, Math.min(3, dy));
            else dy = Math.min(-2, Math.max(-3, dy));
        }
    }

    public void reverseY() {
        dy = -dy;
    }

    public void reverseX() {
        dx = -dx;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
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

    // 🎨 Vẽ bóng (ảnh nếu có, không thì vẽ hình tròn)
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            // Christmas snowball or ornament
            // Draw snowball with gradient effect
            g.setColor(Color.WHITE);
            g.fillOval(x, y, width, height);
            
            // Add highlight for 3D effect
            g.setColor(new Color(255, 255, 255, 200));
            g.fillOval(x + width/4, y + height/4, width/3, height/3);
            
            // Add red/green stripe for Christmas ornament effect
            g.setColor(new Color(220, 20, 60, 150));
            g.fillOval(x, y + height/3, width, height/3);
            
            // Border
            g.setColor(new Color(200, 200, 200));
            g.drawOval(x, y, width, height);
        }
    }
}
