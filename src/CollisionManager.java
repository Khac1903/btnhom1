import java.awt.*;
import java.util.ArrayList;

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

    public void checkCollision(Ball ball, Paddle paddle, BrickMap bMap, int panelWidth, int panelHeight, ArrayList<PowerUp> powerUps) {
        handleWallCollision(ball, panelWidth, panelHeight);
        handlePaddleCollision(ball, paddle);
        handleBallCollision(ball, bMap, powerUps);

    }

    /**
     * xử lí bóng va chạm với thanh chắn.
     * @param paddle thanh chắn
     */
    public void handlePaddleCollision(Ball ball, Paddle paddle) {
        Rectangle ballRect = ball.getBound();
        Rectangle paddleRect = paddle.getBound();
        if (ballRect.intersects(paddleRect) && ball.dy > 0) {


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
    public int handleBallCollision(Ball ball, BrickMap brickMap, ArrayList<PowerUp> powerUps) {
        int brokenBricks = 0;

        Rectangle ballRect = ball.getBound();

        for (int i = 0; i < brickMap.map.length; i++) {
            for (int j = 0; j < brickMap.map[i].length; j++) {
                Bricks brick = brickMap.map[i][j];
                if (brick.isVisible() && ballRect.intersects(brick.getBounds())) {

                    Rectangle brickRect = brick.getBounds();

                    boolean hitFromLeft   = ballRect.x + ballRect.width - ball.dx <= brickRect.x;
                    boolean hitFromRight  = ballRect.x - ball.dx >= brickRect.x + brickRect.width;
                    boolean hitFromTop    = ballRect.y + ballRect.height - ball.dy <= brickRect.y;
                    boolean hitFromBottom = ballRect.y - ball.dy >= brickRect.y + brickRect.height;

                    // 🔹 Xử lý hướng bật
                    if (hitFromLeft) {
                        ball.reverseX();
                        ball.setX(brickRect.x - ballRect.width - 1);
                    } else if (hitFromRight) {
                        ball.reverseX();
                        ball.setX(brickRect.x + brickRect.width + 1);
                    } else if (hitFromTop) {
                        ball.reverseY();
                        ball.setY(brickRect.y - ballRect.height - 1);
                    } else if (hitFromBottom) {
                        ball.reverseY();
                        ball.setY(brickRect.y + brickRect.height + 1);
                    } else {
                        ball.reverseY(); // fallback
                    }

                    boolean broken = false;

                    // 🔹 Sau khi xử lý vật lý thì xử lý loại gạch
                    if (brick.getType() == BrickType.EXPLORE) {
                        brick.setVisible(false);
                        brokenBricks++;
                        brickMap.totalBricks--;
                        brokenBricks += explore(i, j, brickMap, powerUps);
                        spawnPowerUp(brick.x, brick.y, brickMap, powerUps);
                    } else {
                        broken = brick.hit();

                        SoundManager.playSoundAsync("ball_hit");

                        if (broken) {
                            brokenBricks++;
                            brickMap.totalBricks--;
                            spawnPowerUp(brick.x, brick.y, brickMap, powerUps);
                        }
                    }

                    // 🔹 Chỉ cần xử lý va chạm 1 gạch mỗi frame là đủ
                    return brokenBricks;
                }
            }
        }
        return brokenBricks;
    }


    private int explore(int row, int col, BrickMap brickMap, ArrayList<PowerUp> powerUps){
        int extraBroken = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < brickMap.map.length && j >= 0 && j < brickMap.map[0].length) {
                    Bricks neighbor = brickMap.map[i][j];
                    if (neighbor.isVisible() ) {
                        neighbor.setVisible(false);
                        extraBroken++;
                        brickMap.totalBricks--;
                    }
                }
            }
        }
        return extraBroken;
    }

    private void spawnPowerUp(int x, int y, BrickMap brickMap, ArrayList<PowerUp> powerUps){
        if(brickMap.rand.nextInt(4) == 0){
            PowerUpType randomType = PowerUpType.values()[brickMap.rand.nextInt(PowerUpType.values().length)];
            powerUps.add(new PowerUp(x, y,  randomType));
        }
    }

    public boolean handleWallCollision(Ball ball, int panelWidth, int panelHeight) {
        // Cạnh trái
        if (ball.x <= 0) {
            ball.reverseX();
            ball.x = 0;
        }
        // Cạnh phải
        else if (ball.x >= panelWidth - ball.width) {
            ball.reverseX();
            ball.x = panelWidth - ball.width;
        }

        // Cạnh trên
        if (ball.y <= 0) {
            ball.reverseY();
            ball.y = 0;
        }

        // Cạnh dưới (rơi ra khỏi màn hình)
        if (ball.y >= panelHeight - ball.height) {
            return true; // 🔹 báo cho GameManager biết là bóng rơi ra ngoài
        }

        return false; // 🔹 chưa rơi
    }



}
