import java.awt.*;

public class Snowflake {
    private float x, y;
    private float vy;
    private float size;
    private float rotation;
    private float rotationSpeed;
    private int life;

    public Snowflake(float x, float y) {
        this.x = x;
        this.y = y;
        this.vy = 0.5f + (float)(Math.random() * 1.5f);
        this.size = 3 + (float)(Math.random() * 5);
        this.rotation = (float)(Math.random() * Math.PI * 2);
        this.rotationSpeed = (float)(Math.random() * 0.1f - 0.05f);
        this.life = 1000 + (int)(Math.random() * 500);
    }

    public void update(int panelWidth, int panelHeight) {
        y += vy;
        x += Math.sin(y / 50) * 0.5f; // Gentle side-to-side movement
        rotation += rotationSpeed;
        
        if (x < 0) x = panelWidth;
        if (x > panelWidth) x = 0;
        if (y > panelHeight) {
            y = -10;
            x = (float)(Math.random() * panelWidth);
        }
        life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(x, y);
        g2d.rotate(rotation);
        
        g2d.setColor(new Color(255, 255, 255, 200));
        
        // Draw snowflake pattern
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI * i / 3;
            int x1 = 0, y1 = 0;
            int x2 = (int)(Math.cos(angle) * size);
            int y2 = (int)(Math.sin(angle) * size);
            g2d.drawLine(x1, y1, x2, y2);
            
            // Add side branches
            int midX = (int)(Math.cos(angle) * size / 2);
            int midY = (int)(Math.sin(angle) * size / 2);
            double perpAngle1 = angle + Math.PI / 2;
            double perpAngle2 = angle - Math.PI / 2;
            int branchSize = (int)(size / 3);
            
            int bx1 = (int)(midX + Math.cos(perpAngle1) * branchSize);
            int by1 = (int)(midY + Math.sin(perpAngle1) * branchSize);
            int bx2 = (int)(midX + Math.cos(perpAngle2) * branchSize);
            int by2 = (int)(midY + Math.sin(perpAngle2) * branchSize);
            
            g2d.drawLine(midX, midY, bx1, by1);
            g2d.drawLine(midX, midY, bx2, by2);
        }
        
        g2d.dispose();
    }
}

