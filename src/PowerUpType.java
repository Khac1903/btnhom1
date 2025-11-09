import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public enum PowerUpType {
    BIGGER_PADDLE(new Color(34, 139, 34), "images/powerup_bigger.png"), // Christmas Green
    EXTRA_LIFE(new Color(220, 20, 60), "images/powerup_life.png"), // Christmas Red
    MULTI_BALL(new Color(255, 215, 0), "images/powerup_multi.png"), // Gold
    SLOW_BALL(new Color(255, 255, 255), "images/powerup_slow.png"), // White (snow)
    FAST_BALL(new Color(255, 20, 147), "images/powerup_fast.png"); // Deep Pink (ornament)

    private Color color;
    private BufferedImage image;

    PowerUpType(Color color, String imagePath) {
        this.color = color;
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                this.image = ImageIO.read(file);
            } else {
                this.image = null;
            }
        } catch (Exception e) {
            this.image = null;
        }
    }

    public Color getColor() {
        return color;
    }

    public BufferedImage getImage() {
        return image;
    }
}

