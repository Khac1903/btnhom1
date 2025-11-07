import java.awt.*;

public class BrickMap {
    public Bricks[][] map; // mảng 2 chiều chứa các viên gạch
    public int totalBricks; // số viên gạch cần phá để qua màn

    public BrickMap(int level) {
        generateLayout(level);
    }

    public void generateLayout(int level) {
        int rows = 5, cols = 9;
        map = new Bricks[rows][cols];
        totalBricks = 0;
        int brickWidth = 700 / cols;
        int brickHeight = 200 / rows;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int brickX = j * brickWidth + 50;
                int brickY = i * brickHeight + 50;
                BrickType type = BrickType.NORMAL;
                if (level == 1) {
                    type = BrickType.NORMAL;
                } else if (level == 2) {
                    if (j == 0 || j == cols - 1) {
                        type = BrickType.INDESTRUCTIBLE;
                    } else if (i == 2) {
                        type = BrickType.DURABLE;
                    }
                } else {
                    if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) type = BrickType.DURABLE;
                    else if ((i == 2 && j == 2) || (i == 2 && j == cols - 3)) type = BrickType.EXPLORE;
                    else if (i == 2 || j == 4) type = BrickType.INDESTRUCTIBLE;
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
    public int handleBallCollision(Ball ball){
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
                        brokenBricks += explore(i, j);
                        SoundManager.playSound("src/sounds/explore.wav");
                    } else if (brick.getType() == BrickType.INDESTRUCTIBLE) {
                        SoundManager.playSound("src/sounds/indestructible.wav");
                    } else {
                        SoundManager.playSound("src/sounds/ball_hit_brick.wav");
                        if (brick.hit()) {

                            brokenBricks++;
                            totalBricks--;
                        }
                    }
                    return brokenBricks;
                }

            }
        }
        return brokenBricks;
    }

    // hàm kiểm tra phạm vi nổ của gạch
    private int explore(int row, int col){
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
