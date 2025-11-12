import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PowerUp {
    public int x, y, width, height;
    private int dy;
    private PowerUpType type;
    private boolean isVisible;
    private BufferedImage image;

    public PowerUp(int x, int y, PowerUpType type) {

        this.x = x;
        this.y = y;
        this.dy = 2;
        this.type = type;
        this.isVisible = true;
        switch (type) {
            case PADDLE_WIDE, PADDLE_NARROW:
                this.width = 35;
                this.height = 15;
                break;
            default:
                this.width = 25;
                this.height = 25;
        }

        String imagePath = "images/";
        String fileName = "";

        switch (type) {
            case MULTI_BALL:
                fileName = "multiBall.png";
                break;
            case PADDLE_WIDE:
                fileName = "paddleExpand.png";
                break;
            case PADDLE_NARROW:
                fileName = "paddleShrink.png";
                break;
            case BALL_FAST:
                fileName = "fastBall.png";
                break;
        }
        try {
            this.image = ImageIO.read(new File(imagePath + fileName));
        } catch (IOException e) {
            System.err.println("Không thể tải ảnh PowerUp: " + imagePath + fileName);
            this.image = null; // Nếu lỗi, đặt ảnh là null
        }
    }

    public void move(){
        y += dy;
    }
    public void draw(Graphics g) {
        if (isVisible) {
            if (image != null) {
                g.drawImage(image, x, y, width, height, null);
            } else {
                g.setColor(type.color);
                g.fillRect(x, y, width, height);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);


                switch (type) {
                    case PADDLE_WIDE, PADDLE_NARROW:
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        break;
                    case MULTI_BALL, BALL_FAST:
                        g.fillOval(x, y, width, height); // Vẽ hình tròn
                        g.setColor(Color.BLACK);
                        g.drawOval(x, y, width, height); // Vẽ viền tròn
                        break;
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 12));

                String text = "";
                switch (type) {
                    case PADDLE_WIDE:
                        text = "W";
                        break;
                    case MULTI_BALL:
                        text = "M";
                        break;
                    case PADDLE_NARROW:
                        text = "N";
                        break;
                    case BALL_FAST:
                        text = "F";
                        break;

                }


                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();

                int textX = x + (width - textWidth) / 2;
                int textY = y + ((height - textHeight) / 2) + fm.getAscent();

                g.drawString(text, textX, textY);
            }
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }
    public PowerUpType getType(){
        return type;
    }
    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }
}