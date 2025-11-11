import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Ball extends MoveObject {
    private BufferedImage image;

    public Ball(int x, int y, int size, int dx, int dy, Color color, BufferedImage image) {
        super(x, color, dx, dy, size, size, y);
        this.image = image;
    }

    public Ball(int x, int y, int size, int dx, int dy, Color color) {
        super(x, color, dx, dy, size, size, y);
        try {
            this.image = ImageIO.read(new File(GameConstants.IMAGE_BALL));
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

    public void handleWallCollision(int panelWidth, int panelHeight) {
        // Va chạm với cạnh trái
        if (x <= GameConstants.ZERO) {
            x = GameConstants.ZERO;
            dx = -dx;
        }
        // Va chạm với cạnh phải
        if (x >= panelWidth - width) {
            x = panelWidth - width;
            dx = -dx;
        }
        // Va chạm với cạnh trên
        if (y <= GameConstants.ZERO) {
            y = GameConstants.ZERO;
            dy = -dy;
        }
        // Cạnh dưới được xử lý bởi isOutOfBounds() trong GameManager
    }

    public boolean handlePaddleCollision(Paddle paddle) {
        Rectangle ballRect = this.getBound();
        Rectangle paddleRect = paddle.getBound();
        
        // Chỉ xử lý khi bóng đang di chuyển xuống và có va chạm
        if (ballRect.intersects(paddleRect) && dy > GameConstants.ZERO) {
            SoundManager.playSound(GameConstants.SOUND_BALL_HIT_PADDLE);
            
            // Đặt bóng lên trên paddle để tránh bị kẹt
            y = paddle.y - height;
            
            // Đổi chiều dy (bóng đi lên)
            dy = -Math.abs(dy);
            
            // Điều chỉnh dx một chút dựa trên vị trí va chạm trên paddle
            double paddleCenter = paddle.x + paddle.width / 2.0;
            double ballCenter = x + width / 2.0;
            double offset = (ballCenter - paddleCenter) / (paddle.width / 2.0);
            
            // Thay đổi dx dựa trên vị trí va chạm
            if (Math.abs(dx) < GameConstants.BALL_DX_THRESHOLD) {
                dx += (int)(offset * GameConstants.BALL_DX_ADJUSTMENT);
            }
            
            return true;
        }
        return false;
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
        this.dx = GameConstants.BALL_INITIAL_DX;
        this.dy = GameConstants.BALL_INITIAL_DY;
    }

    public void changeSpeed(double multiplier) {
        dy *= multiplier;
        dx *= multiplier;
    }


    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int)x, (int)y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval((int) x , (int) y, width, height);
        }
    }
}
