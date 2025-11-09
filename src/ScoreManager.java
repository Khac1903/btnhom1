import java.awt.*;

public class ScoreManager extends Info {
    public ScoreManager(){
        super();
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(Color.cyan);
        g.setFont(new Font("Consolas", Font.ITALIC, 22));
        g.drawString("Score: " + score, 20, 55);
    }
}
