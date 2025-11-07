import java.awt.*;

public class Info {


    protected int score;
    protected int level;
    protected int lives;
    protected String playerName = "Player";
    protected int highScore = 0;


    public Info() {
       // this.highScore = 0;
        this.level = 3;
        this.score = 0;
        this.lives = 3;

        this.playerName = "Player";
        reset();
    }

    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));


        g.drawString("Player: " + playerName,20, 30);
        g.drawString("HighScore: " + highScore, 600, 30);
        g.drawString("Level: " + level, 600, 55);
        g.drawString("Score: " + score, 20, 55);
        g.drawString("Lives: " + lives, 20, 80);
    }
    public void setPlayerInfo(String name, int highScore){
        this.playerName = name;
        this.highScore = highScore;
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
        lives = 3;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void loseLife(){
        lives--;
    }

    public boolean hasLive(){
        return lives > 0;
    }

    public void resetForNextLevel(){
        level++;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setHighScore(int score) {
        this.highScore = score;
    }
}