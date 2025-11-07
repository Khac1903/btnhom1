import java.awt.*;

public class ScoreManager extends Info {
    public ScoreManager(){
        super();
    }
    @Override
    public void draw(Graphics g){
        g.setColor(Color.cyan);
        g.setFont(new Font("Consolas", Font.ITALIC, 22));
        g.drawString("Score: " + score, 20, 55);
    }
}