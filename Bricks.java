import java.awt.*;

public class Bricks {
    public int x, y, width, height;
    private boolean isVisible;
    private Color color;
    private BrickType type;
    private int health;

    public Bricks(int x, int y, int width, int height, BrickType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.isVisible = true;
        switch (type){
            case NORMAL:
                this.health = 1;
                this.color = Color.yellow;
                break;
            case DURABLE:
                this.health = 2;
                this.color = Color.cyan;
                break;
            case INDESTRUCTIBLE:
                this.health = Integer.MAX_VALUE;
                this.color = Color.gray;
                break;
            case EXPLORE:
                this.health = 1;
                this.color = Color.red;
                break;
        }
    }

    public void draw(Graphics g) {
        if(!isVisible) return;;

        Image img = type.getImage();
        if(img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            // trong trường hợp không tìm được ảnh thì vẽ tạm
            g.setColor(color );
            g.fillRect(x, y, width, height);

            if (type == BrickType.DURABLE && health == 1) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x + width, y + height);
                g.drawLine(x + width, y, x, y + height);
            }

            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }
    public boolean hit(){
        if(type == BrickType.INDESTRUCTIBLE){
            return false;
        }
        health --;
        if(health <= 0){
            isVisible = false;
            return true;
        }
        if(type==BrickType.DURABLE){
            this.color = Color.blue;
        }
        return false;
    }
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public BrickType getType(){
        return type;
    }
}
