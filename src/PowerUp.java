
import java.awt.*;

public class PowerUp extends MoveObject {
    public PowerUp(int x, Color color, int dy, int dx,
                   int height, int width, int y) {
        super(x, color, dy, dx, height, width, y);
    }

    public void updatePosition() {
        y += dy;
    }

    public void handlePaddleCollision(Paddle paddle) {
        Rectangle powerupRect = this.getBound();
        Rectangle paddleRect = paddle.getBound();
    }
}
