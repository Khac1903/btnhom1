import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Ball extends MoveObject {
    private BufferedImage[] images;  // mảng ảnh animation
    private int currentFrame = 0;    // frame hiện tại
    private int frameDelay = 5;      // số lần updatePosition gọi mới đổi frame
    private int frameDelayCounter = 0;


    private BufferedImage defaultImage;
    private int defaultX, defaultY;

    public Ball(int x, int y, int size, int dx, int dy, Color color, BufferedImage image) {
        super(x, color, dy, dx, size, size, y);
        defaultX = dx;
        defaultY = dy;
        this.defaultImage = image;
        this.images = new BufferedImage[] { image }; // mặc định chỉ 1 ảnh nếu ko có animation
    }

    // Constructor phụ: tự load ảnh nếu không truyền vào
    public Ball(int x, int y, int size, int dx, int dy, Color color) {
        super(x, color, dy, dx, size, size, y);
        defaultX = dx;
        defaultY = dy;
        try {
            // load nhiều ảnh cho animation, ví dụ 3 frame
            images = new BufferedImage[3];
            images[0] = ImageIO.read(new File("images/ball.png"));
            images[1] = ImageIO.read(new File("images/ball2.png"));
            images[2] = ImageIO.read(new File("images/ball3.png"));

            defaultImage = images[0];
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng!");
            e.printStackTrace();
            images = new BufferedImage[1];
            images[0] = null;
            defaultImage = null;
        }
    }

    public void stickToPaddle(Paddle paddle) {
        this.x = paddle.x + (paddle.width / 2) - (this.width / 2);
        this.y = paddle.y - this.height;
    }

    public boolean isOutOfBounds(int panelHeight) {
        return y >= panelHeight;
    }

    public void launch() {
        this.dx = 2;
        this.dy = -3;
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    public void updatePosition() {
        x += dx;
        y += dy;

        // cập nhật animation frame
        frameDelayCounter++;
        if (frameDelayCounter >= frameDelay) {
            frameDelayCounter = 0;
            currentFrame = (currentFrame + 1) % images.length;
        }
    }

    // Các hàm reverseY, reverseX, isOutOfBounds, stickToPaddle, launch, changeSpeed, v.v... giữ nguyên

    public void draw(Graphics g) {
        if (images[currentFrame] != null) {
            g.drawImage(images[currentFrame], x, y, width + 5, height + 5, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }

    // Khi apply power-up, có thể thay đổi bộ images nếu muốn animation khác
    public void applyPowerUp(PowerUpType type) {
        try {
            switch (type) {
                case BALL_FAST:
                    // Ví dụ: đổi animation thành ảnh nhanh
                    images = new BufferedImage[] {
                            ImageIO.read(new File("images/ball_fast_1.png")),
                            ImageIO.read(new File("images/ball_fast_2.png")),
                            ImageIO.read(new File("images/ball_fast_3.png"))
                    };
                    currentFrame = 0; // reset frame
                    changeSpeed(1.5);
                    break;

                case MULTI_BALL:
                    // Bạn có thể đổi animation tương tự
                    images = new BufferedImage[] {
                            ImageIO.read(new File("images/ball_multi_1.png")),
                            ImageIO.read(new File("images/ball_multi_2.png")),
                            ImageIO.read(new File("images/ball_multi_3.png"))
                    };
                    currentFrame = 0;
                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePowerUp(PowerUpType type) {
        switch (type) {
            case BALL_FAST:
            case MULTI_BALL:
                images = new BufferedImage[] { defaultImage };  // quay lại ảnh mặc định
                currentFrame = 0;
                changeSpeed(1.0 / 1.5);
                break;

            default:
                break;
        }
    }

    public void changeSpeed(double multiplier) {
        dy *= multiplier;
        dx *= multiplier;
    }


    public void resetImage() {
        images = new BufferedImage[] { defaultImage };
        currentFrame = 0;
    }

    public void resetSpeed() {
        dx = defaultX;
        dy = defaultY;
    }

    public void resetPosition(int startX, int startY) {
        // Đặt bóng về vị trí ban đầu
        this.x = startX;
        this.y = startY;

        // Reset tốc độ về giá trị mặc định
        this.dx = defaultX;
        this.dy = defaultY;

        // Reset animation
        resetImage();
    }

}
