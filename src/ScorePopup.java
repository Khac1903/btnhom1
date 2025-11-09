import java.awt.*;

public class ScorePopup {
    private int x, y;
    private String text;
    private int life;
    private Color color;
    private Font font;

    public ScorePopup(int x, int y, int score, Color color) {
        this.x = x;
        this.y = y;
        this.text = "+" + score;
        this.color = color;
        this.life = 60; // 1 second at 60fps
        this.font = new Font("Arial", Font.BOLD, 24);
    }

    public void update() {
        y -= 2; // Move up
        life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public void draw(Graphics g) {
        float alpha = (float)life / 60f;
        Color drawColor = new Color(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            (int)(255 * alpha)
        );
        g.setColor(drawColor);
        g.setFont(font);
        
        // Draw with shadow for better visibility
        g.setColor(new Color(0, 0, 0, (int)(100 * alpha)));
        g.drawString(text, x + 2, y + 2);
        g.setColor(drawColor);
        g.drawString(text, x, y);
    }
}

