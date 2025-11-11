import java.awt.*;

public class Bricks {
    public int x, y, width, height;
    private boolean isVisible;
    private BrickType type;
    private int health;
    private boolean damaged; // đã bị đánh ít nhất 1 lần

    public Bricks(int x, int y, int width, int height, BrickType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.isVisible = true;
        this.damaged = false;
        this.health = type.getBaseHealth();
    }

    public void draw(Graphics g) {
        if (!isVisible) return;

        Image img;

        // Nếu health < baseHealth thì dùng ảnh damage
        if (damaged && health < type.getBaseHealth() && type.getDamage() != null) {
            img = type.getDamage();
        } else {
            img = type.getImage();
        }

        // Vẽ ảnh
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }

    public boolean hit() {
        health--;           // giảm máu
        damaged = true;     // đã bị đánh

        if (health <= 0) {
            isVisible = false;
            return true;    // gạch vỡ
        }

        // Không đổi type ngay, chỉ đổi khi health trùng baseHealth loại khác nhưng chưa vẽ damage
        for (BrickType t : BrickType.values()) {
            if (t != type && health == t.getBaseHealth()) {
                type = t; // đổi type sang loại khác
                break;
            }
        }

        return false; // còn sống
    }



    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public BrickType getType() { return type; }

}
