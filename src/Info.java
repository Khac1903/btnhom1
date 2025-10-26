import java.awt.*;

public class Info {
   // protected int highScore;
    protected int score;
    protected int level;
    protected int lives;


    public Info() {
       // this.highScore = 0;
        this.level = 3;
        this.score = 0;
        this.lives = 3;

    }

    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        //g.drawString("HighScore: " + highScore, 20, 30);
        g.drawString("Level: " + level, 20, 30);
        g.drawString("Score: " + score, 20, 55);
        g.drawString("Lives: " + lives, 20, 80);
    }
    public void increaseScore(){
        score += 10;
    }
    public void nextLevel(){
        level++;
    }
    public void reset(){
        /*if(score > highScore)
        {
            highScore = score;
        }*/
        level = 1;
        score = 0;
    }
    public int getLevel(){
        return level;
    }
}
