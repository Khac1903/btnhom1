
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
public enum BrickType {
    NORMAL("images/normal.png"),
    INDESTRUCTIBLE("images/indestructible.jpg"),
    DURABLE("images/durable.jpg"),
    EXPLORE("images/explore.png");

    private Image image;

    BrickType(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println("Lỗi không tải được ảnh " + path);
            image = null;
        }
    }

    public Image getImage() {
        return image;
    }
}
