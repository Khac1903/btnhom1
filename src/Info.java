import java.awt.*;

public class Info {
    protected int score;
    protected int level;
    protected int lives;


    public Info() {
        this.level = 3;
        this.score = 0;
        this.lives = 3;

    }

    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString("Level: " + level, 20, 30);
        g.drawString("Score: " + score, 20, 55);

    }
    public void increaseScore(){
        score += 10;
    }
    public void nextLevel(){
        level++;
    }
    public void reset(){
        level = 1;
        score = 0;
    }
    public int getLevel(){
        return level;
    }
}
