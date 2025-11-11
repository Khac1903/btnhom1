
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
public enum BrickType {
    NORMAL("images/normal.png"),
    INDESTRUCTIBLE("images/indestructible.png"),
    DURABLE("images/durable.png","images/durable_damage.png"),
    EXPLORE("images/explore.png");

    private Image image;
    private Image damage;

    BrickType(String path) {
        this(path, null);
    }

    BrickType(String path, String damagePath) {
        try {
            image = ImageIO.read(new File(path));
            if (damagePath != null) {
                damage = ImageIO.read(new File(damagePath));
            }
        } catch (Exception e) {
            System.out.println("Lỗi không tải được ảnh " + path);
            image = null;
        }
    }

    public Image getImage() {
        return image;
    }


    public Image getDamage() {
        return damage;
    }
}
