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
