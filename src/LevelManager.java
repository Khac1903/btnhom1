import java.awt.*;

public class LevelManager extends Info{
    public LevelManager() {
        super();
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Verdana", Font.BOLD, 22));

        g.drawString("Level: " + level, 20, 30);
    }
}