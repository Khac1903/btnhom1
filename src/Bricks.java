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
                this.color = new Color(220, 20, 60); // Christmas Red
                break;
            case DURABLE:
                this.health = 2;
                this.color = new Color(34, 139, 34); // Christmas Green
                break;
            case INDESTRUCTIBLE:
                this.health = Integer.MAX_VALUE;
                this.color = new Color(255, 215, 0); // Gold
                break;
            case EXPLORE:
                this.health = 1;
                this.color = new Color(255, 20, 147); // Deep Pink (Christmas ornament)
                break;
        }
    }

    public void draw(Graphics g) {
        if(!isVisible) return;;

        Image img = type.getImage();
        if(img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            // Christmas-themed brick drawing
            g.setColor(color);
            g.fillRect(x, y, width, height);
            
            // Add Christmas pattern
            if (type == BrickType.NORMAL) {
                // Red brick with white snowflake pattern
                g.setColor(Color.WHITE);
                drawSnowflake(g, x + width/2, y + height/2, Math.min(width, height)/3);
            } else if (type == BrickType.DURABLE) {
                // Green brick with gold star
                if (health == 2) {
                    g.setColor(new Color(255, 215, 0));
                    drawStar(g, x + width/2, y + height/2, Math.min(width, height)/3);
                } else {
                    // Cracked - show damage
                    g.setColor(new Color(100, 50, 0));
                    g.drawLine(x, y, x + width, y + height);
                    g.drawLine(x + width, y, x, y + height);
                }
            } else if (type == BrickType.INDESTRUCTIBLE) {
                // Gold brick with star pattern
                g.setColor(Color.WHITE);
                drawStar(g, x + width/2, y + height/2, Math.min(width, height)/3);
            } else if (type == BrickType.EXPLORE) {
                // Pink brick with ornament pattern
                g.setColor(Color.WHITE);
                g.fillOval(x + width/4, y + height/4, width/2, height/2);
            }

            // Border
            g.setColor(new Color(139, 69, 19)); // Brown border
            g.drawRect(x, y, width, height);
        }
    }
    
    private void drawSnowflake(Graphics g, int x, int y, int size) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI * i / 3;
            int x2 = (int)(x + Math.cos(angle) * size);
            int y2 = (int)(y + Math.sin(angle) * size);
            g.drawLine(x, y, x2, y2);
        }
    }
    
    private void drawStar(Graphics g, int x, int y, int size) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI * i / 5 - Math.PI / 2;
            int r = (i % 2 == 0) ? size : size / 2;
            xPoints[i] = (int)(x + Math.cos(angle) * r);
            yPoints[i] = (int)(y + Math.sin(angle) * r);
        }
        g.fillPolygon(xPoints, yPoints, 10);
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
            this.color = new Color(0, 100, 0); // Darker green when damaged
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
