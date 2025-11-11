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


    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
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

            dx = (int) Math.round(newDx);
            dy = (int) Math.round(newDy);

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

    public void changeSpeed(double multiplier) {
        dy *= multiplier;
        dx *= multiplier;
    }


    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }
}