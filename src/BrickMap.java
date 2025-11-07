import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BrickMap {
    public Bricks[][] map;
    private int totalBricks;
    private Random rand;

    public BrickMap(int level) {
        rand = new Random();
        generateLayout(level);
    }

    public void generateLayout(int level) {
        int rows = 5, cols = 9;
        map = new Bricks[rows][cols];
        totalBricks = 0;
        BrickType type = null;
        int brickWidth = 700 / cols;
        int brickHeight = 200 / rows;
        int durable_chance = 10 + (level * 4);
        int indestructible_chance = 5 + (level * 2);
        int explore_chance = 5;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int brickX = j * brickWidth + 50;
                int brickY = i * brickHeight + 50;
                if (level == 1) {
                    type = BrickType.NORMAL;
                } else if (level == 2) {
                    if (j == 0 || j == cols - 1) {
                        type = BrickType.INDESTRUCTIBLE;
                    } else if (i == 2) {
                        type = BrickType.DURABLE;
                    }
                } else if(level == 3)   {
                    if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) type = BrickType.DURABLE;
                    else if ((i == 2 && j == 2) || (i == 2 && j == cols - 3)) type = BrickType.EXPLORE;
                    else if (i == 2 || j == 4) type = BrickType.INDESTRUCTIBLE;
                } else {
                    int roll = rand.nextInt(100);
                    if(roll < durable_chance ){
                        type = BrickType.DURABLE;
                    }else if(roll < durable_chance + explore_chance){
                        type = BrickType.EXPLORE;
                    } else if (level> 1 && (i%2 == 0) && (j%3 == 0) && roll < durable_chance + explore_chance + indestructible_chance ){
                        type = BrickType.INDESTRUCTIBLE;
                    } else {
                        type = BrickType.NORMAL;
                    }
                }
                if (type == BrickType.EXPLORE && i == map.length - 1) {
                    type = BrickType.NORMAL;
                }
                if (type == BrickType.INDESTRUCTIBLE && i == map.length - 1) {
                    type = BrickType.NORMAL;
                }


                map[i][j] = new Bricks(brickX,brickY,brickWidth,brickHeight,type);
                if(map[i][j].getType() != BrickType.INDESTRUCTIBLE){
                    totalBricks++;
                }
            }
        }
    }

    public void draw(Graphics g) {
        for(Bricks[] row : map){
            for(Bricks bricks : row){
                bricks.draw(g);
            }
        }
    }
    public int handleBallCollision(Ball ball, ArrayList<PowerUp> powerUps){
        int brokenBricks = 0;
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[i].length;j++){
                Bricks brick = map[i][j];
                if (brick.isVisible() && ball.getBound().intersects(brick.getBounds())) {
                    Rectangle ballRect = ball.getBound();
                    Rectangle brickRect = brick.getBounds();
                    boolean hitFromLeft   = ballRect.x + ballRect.width - ball.dx <= brickRect.x;
                    boolean hitFromRight  = ballRect.x - ball.dx >= brickRect.x + brickRect.width;
                    boolean hitFromTop    = ballRect.y + ballRect.height - ball.dy <= brickRect.y;
                    boolean hitFromBottom = ballRect.y - ball.dy >= brickRect.y + brickRect.height;

                    if (hitFromLeft || hitFromRight) {
                        ball.reverseX();
                    } else if (hitFromTop || hitFromBottom) {
                        ball.reverseY();
                    } else {
                        ball.reverseY();
                    }
                    if (brick.getType() == BrickType.EXPLORE) {

                        brick.setVisible(false);
                        brokenBricks++;
                        totalBricks--;
                        brokenBricks += explore(i, j, powerUps);
                        spawnPowerUp(brick.x, brick.y, powerUps);
                        //SoundManager.playSound("src/sounds/explore.wav");
                    } else if (brick.getType() == BrickType.INDESTRUCTIBLE) {
                        //SoundManager.playSound("src/sounds/indestructible.wav");
                    } else {
                        //SoundManager.playSound("src/sounds/ball_hit_brick.wav");
                        if (brick.hit()) {

                            brokenBricks++;
                            totalBricks--;
                            spawnPowerUp(brick.x, brick.y, powerUps);
                        }
                    }
                    return brokenBricks;
                }

            }
        }
        return brokenBricks;
    }

    private int explore(int row, int col, ArrayList<PowerUp> powerUps){
        int extraBroken = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < map.length && j >= 0 && j < map[0].length) {
                    Bricks neighbor = map[i][j];
                    if (neighbor.isVisible() && neighbor.getType() != BrickType.INDESTRUCTIBLE) {
                        neighbor.setVisible(false);
                        extraBroken++;
                        totalBricks--;
                    }
                }
            }
        }
        return extraBroken;
    }
    private void spawnPowerUp(int x, int y, ArrayList<PowerUp> powerUps){
        if(rand.nextInt(4) == 0){
            PowerUpType randomType = PowerUpType.values()[rand.nextInt(PowerUpType.values().length)];
            powerUps.add(new PowerUp(x, y, randomType));
        }
    }

    public void breakBrick() {
        totalBricks--;
    }

    public int getTotalBricks() {
        return totalBricks;
    }

    public boolean isLevelComplete() {
        return totalBricks <= 0;
    }
}