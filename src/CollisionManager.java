import java.awt.*;

public class CollisionManager {
    private static CollisionManager instance; // biến truy cập duy nhất

    CollisionManager() {

    }

    public static CollisionManager getInstance() {
        if (instance == null) {
            instance = new CollisionManager();
        }
        return instance;
    }

    public void checkCollision(Ball ball, Paddle paddle, BrickMap bMap, int panelWidth) {
        handleWallCollision(ball, panelWidth);
        handlePaddleCollision(ball, paddle);
        handleBallCollision(ball, bMap);

    }

    /**
     * xử lí bóng va chạm với thanh chắn.
     * @param paddle thanh chắn
     */
    public void handlePaddleCollision(Ball ball, Paddle paddle) {
        Rectangle ballRect = ball.getBound();
        Rectangle paddleRect = paddle.getBound();
        if (ballRect.intersects(paddleRect) && ball.dy > 0) {
            SoundManager.playSound("src/sounds/ball_hit_paddle.wav");

            double paddleCenter = paddle.x + paddle.width / 2.0;
            double ballCenter = ball.x + ball.width / 2.0;
            double offset = (ballCenter - paddleCenter) / (paddle.width / 2.0);

            double speed = Math.sqrt(ball.dx * ball.dx + ball.dy * ball.dy);
            double angle = Math.toRadians(60 * offset);
            double paddleInfluence = paddle.dx * 0.2;

            double newDx = speed * Math.sin(angle) + paddleInfluence;
            double newDy = -Math.abs(speed * Math.cos(angle));

            ball.dx = (int)Math.round(newDx);
            ball.dy = (int)Math.round(newDy);

            ball.y = paddle.y - ball.height;

            if (ball.dx > 0) ball.dx = Math.max(2, Math.min(3, ball.dx));
            else ball.dx = Math.min(-2, Math.max(-3, ball.dx));

            if (ball.dy > 0) ball.dy = Math.max(2, Math.min(3, ball.dy));
            else ball.dy = Math.min(-2, Math.max(-3, ball.dy));
        }
    }

    /**
     * xử lí va chạm của bóng với gạch
     *
     * @param ball bóng
     * @param brickMap bản đồ
     */
    public int handleBallCollision(Ball ball, BrickMap brickMap) {
        int brokenBricks = 0;
        for (int i = 0; i < brickMap.map.length; i++) {
            for (int j = 0; j < brickMap.map[i].length; j++) {
                Bricks brick = brickMap.map[i][j];
                if (brick.isVisible() && ball.getBound().intersects(brick.getBounds())) {

                    Rectangle ballRect = ball.getBound();
                    Rectangle brickRect = brick.getBounds();

                    boolean hitFromLeft   = ballRect.x + ballRect.width - ball.dx <= brickRect.x;
                    boolean hitFromRight  = ballRect.x - ball.dx >= brickRect.x + brickRect.width;
                    boolean hitFromTop    = ballRect.y + ballRect.height - ball.dy <= brickRect.y;
                    boolean hitFromBottom = ballRect.y - ball.dy >= brickRect.y + brickRect.height;

                    // 1️⃣ Luôn phản xạ trước
                    if (hitFromLeft || hitFromRight) {
                        ball.reverseX();
                        // đẩy bóng ra khỏi brick một chút
                        ball.setX(hitFromLeft ? brickRect.x - ballRect.width - 1 : brickRect.x + brickRect.width + 1);
                    } else {
                        ball.reverseY();
                        ball.setY(hitFromTop ? brickRect.y - ballRect.height - 1 : brickRect.y + brickRect.height + 1);
                    }

                    // 2️⃣ Sau đó xử lý loại gạch
                    if (brick.getType() == BrickType.EXPLORE) {
                        brick.setVisible(false);
                        brokenBricks++;
                        brickMap.totalBricks--;
                        brokenBricks += explore(i, j, brickMap);
                        SoundManager.playSound("src/sounds/explore.wav");

                    } else if (brick.getType() == BrickType.INDESTRUCTIBLE) {
                        SoundManager.playSound("src/sounds/indestructible.wav");

                    } else {
                        SoundManager.playSound("src/sounds/ball_hit_brick.wav");
                        if (brick.hit()) {
                            brokenBricks++;
                            brickMap.totalBricks--;
                        }
                    }

                    // 3️⃣ Không return sớm — tiếp tục duyệt
                }
            }
        }
        return brokenBricks;
    }

    private int explore(int row, int col, BrickMap brickMap){
        int extraBroken = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < brickMap.map.length && j >= 0 && j < brickMap.map[0].length) {
                    Bricks neighbor = brickMap.map[i][j];
                    if (neighbor.isVisible() && neighbor.getType() != BrickType.INDESTRUCTIBLE) {
                        neighbor.setVisible(false);
                        extraBroken++;
                        brickMap.totalBricks--;
                    }
                }
            }
        }
        return extraBroken;
    }
    // kiểm tra va chạm với cạnh cửa sổ
    public void handleWallCollision(Ball ball, int panelWidth) {
        if (ball.x <= 0 || ball.x >= panelWidth - ball.width) {
            ball.dx = -ball.dx;
        }
        if (ball.y <= 0) {
            ball.dy = -ball.dy;
        }
    }

}
