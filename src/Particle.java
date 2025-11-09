import java.awt.*;

public class Particle {
    private float x, y;
    private float vx, vy;
    private Color color;
    private int life;
    private int maxLife;
    private int size;

    public Particle(float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.maxLife = 30 + (int)(Math.random() * 20);
        this.life = maxLife;
        this.size = 3 + (int)(Math.random() * 4);
        
        // Random velocity
        double angle = Math.random() * Math.PI * 2;
        double speed = 1 + Math.random() * 3;
        this.vx = (float)(Math.cos(angle) * speed);
        this.vy = (float)(Math.sin(angle) * speed);
    }

    public void update() {
        x += vx;
        y += vy;
        vy += 0.15f; // Gravity
        life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public void draw(Graphics g) {
        float alpha = (float)life / maxLife;
        Color drawColor = new Color(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            (int)(255 * alpha)
        );
        g.setColor(drawColor);
        g.fillOval((int)x, (int)y, size, size);
    }
}

