import java.awt.*;

public class PlayerManager extends Info {
    public PlayerManager() {
        super();
    }

    public void loseLife() {
        if (lives > 0) {
            lives --;
        }
    }

    public void gainLife() {
        lives++;
    }

    public void reset() {
        this.lives = 3;
    }

    public int getLives() {
        return this.lives;
    }

    public boolean isOutOfLives() {
        return this.lives <= 0;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Lives: " + lives , 20, 80);
    }
}
