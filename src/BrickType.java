
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public enum BrickType {
    NORMAL("images/normal.png", "images/normal_damage.png", 2),
    DURABLE("images/durable.png", "images/durable_damage.png", 4),
    INDESTRUCTIBLE("images/indestructible.png", "images/indestructible.png", 8),
    EXPLORE("images/explore.png", "images/explore_damage.png", 6);

    private Image image;
    private Image damage;
    private int baseHealth; // 🔹 thêm để biết “độ bền gốc” của loại này

    // Constructor chính
    BrickType(String path, String damagePath, int baseHealth) {
        this.baseHealth = baseHealth;
        try {
            image = ImageIO.read(new File(path));
            if (damagePath != null) {
                damage = ImageIO.read(new File(damagePath));
            }
        } catch (Exception e) {
            System.out.println(" Lỗi không tải được ảnh " + path);
            image = null;
            damage = null;
        }
    }

    // Constructor phụ nếu không có ảnh nứt
    BrickType(String path, int baseHealth) {
        this(path, null, baseHealth);
    }

    public Image getImage() {
        return image;
    }

    public Image getDamage() {
        return damage;
    }

    public int getBaseHealth() {
        return baseHealth;
    }
}
