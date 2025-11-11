import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BrickMap {
    public Bricks[][] map;
    public int totalBricks;
    public Random rand;

    public BrickMap(int level) {
        rand = new Random();
        // Thử load từ file trước, nếu không có thì generate
        if (!loadFromFile(level)) {
            generateLayout(level);
        }
    }
    
    /**
     * Load level từ file txt
     * Format: 
     * - Dòng đầu: số hàng, số cột
     * - Các dòng tiếp theo: mỗi dòng là một hàng, mỗi ký tự là một gạch
     * - Ký tự: N=Normal, D=Durable, I=Indestructible, E=Explore, .=Empty
     */
    private boolean loadFromFile(int level) {
        String fileName = GameConstants.LEVEL_FILE_PATH + level + GameConstants.LEVEL_FILE_EXTENSION;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Đọc số hàng và số cột
            String firstLine = br.readLine();
            if (firstLine == null) return false;
            String[] dimensions = firstLine.trim().split("\\s+");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);
            
            map = new Bricks[rows][cols];
            totalBricks = 0;
            int brickWidth = GameConstants.BRICK_AREA_WIDTH / cols;
            int brickHeight = GameConstants.BRICK_AREA_HEIGHT / rows;
            
            // Đọc từng hàng
            for (int i = 0; i < rows; i++) {
                String line = br.readLine();
                if (line == null) break;
                line = line.trim();
                
                for (int j = 0; j < cols && j < line.length(); j++) {
                    char c = line.charAt(j);
                    int brickX = j * brickWidth + GameConstants.BRICK_OFFSET_X;
                    int brickY = i * brickHeight + GameConstants.BRICK_OFFSET_Y;
                    
                    BrickType type = null;
                    if (c == 'N' || c == 'n') {
                        type = BrickType.NORMAL;
                        totalBricks++;
                    } else if (c == 'D' || c == 'd') {
                        type = BrickType.DURABLE;
                        totalBricks++;
                    } else if (c == 'I' || c == 'i') {
                        type = BrickType.INDESTRUCTIBLE;
                    } else if (c == 'E' || c == 'e') {
                        type = BrickType.EXPLORE;
                        totalBricks++;
                    } else {
                        // Ký tự khác (., space, etc.) = không có gạch
                        type = BrickType.NORMAL;
                        // Tạo gạch ẩn
                        map[i][j] = new Bricks(brickX, brickY, brickWidth, brickHeight, type);
                        map[i][j].setVisible(false);
                        continue;
                    }
                    
                    map[i][j] = new Bricks(brickX, brickY, brickWidth, brickHeight, type);
                }
                
                // Điền các cột còn lại nếu line ngắn hơn cols
                for (int j = line.length(); j < cols; j++) {
                    int brickX = j * brickWidth + GameConstants.BRICK_OFFSET_X;
                    int brickY = i * brickHeight + GameConstants.BRICK_OFFSET_Y;
                    BrickType type = BrickType.NORMAL;
                    map[i][j] = new Bricks(brickX, brickY, brickWidth, brickHeight, type);
                    map[i][j].setVisible(false);
                }
            }
            
            return true;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Không thể load level từ file " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public void generateLayout(int level) {
        int rows = GameConstants.BRICK_DEFAULT_ROWS;
        int cols = GameConstants.BRICK_DEFAULT_COLS;
        map = new Bricks[rows][cols];
        totalBricks = 0;
        BrickType type = null;
        int brickWidth = GameConstants.BRICK_AREA_WIDTH / cols;
        int brickHeight = GameConstants.BRICK_AREA_HEIGHT / rows;
        int durable_chance = GameConstants.BRICK_DURABLE_CHANCE_BASE + (level * GameConstants.BRICK_DURABLE_CHANCE_MULTIPLIER);
        int indestructible_chance = GameConstants.BRICK_INDESTRUCTIBLE_CHANCE_BASE + (level * GameConstants.BRICK_INDESTRUCTIBLE_CHANCE_MULTIPLIER);
        int explore_chance = GameConstants.BRICK_EXPLORE_CHANCE;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int brickX = j * brickWidth + GameConstants.BRICK_OFFSET_X;
                int brickY = i * brickHeight + GameConstants.BRICK_OFFSET_Y;

                if (level == GameConstants.ONE) {
                    type = BrickType.NORMAL;
                } else if (level == GameConstants.TWO) {
                    if (j == GameConstants.ZERO || j == cols - GameConstants.ONE) {
                        type = BrickType.INDESTRUCTIBLE;
                    } else if (i == GameConstants.TWO) {
                        type = BrickType.DURABLE;
                    }
                } else if (level == GameConstants.THREE) {
                    if (i == GameConstants.ZERO || i == rows - GameConstants.ONE || 
                        j == GameConstants.ZERO || j == cols - GameConstants.ONE)
                        type = BrickType.DURABLE;
                    else if ((i == GameConstants.TWO && j == GameConstants.TWO) || (i == GameConstants.TWO && j == cols - GameConstants.THREE))
                        type = BrickType.EXPLORE;
                    else if (i == GameConstants.TWO || j == GameConstants.FOUR)
                        type = BrickType.INDESTRUCTIBLE;
                } else {
                    int roll = rand.nextInt(GameConstants.BRICK_RANDOM_MAX);
                    if (roll < durable_chance) {
                        type = BrickType.DURABLE;
                    } else if (roll < durable_chance + explore_chance) {
                        type = BrickType.EXPLORE;
                    } else if (level > GameConstants.ONE && (i % GameConstants.TWO == GameConstants.ZERO) && (j % GameConstants.THREE == GameConstants.ZERO)
                            && roll < durable_chance + explore_chance + indestructible_chance) {
                        type = BrickType.INDESTRUCTIBLE;
                    } else {
                        type = BrickType.NORMAL;
                    }
                }

                if (type == BrickType.EXPLORE && i == map.length - 1)
                    type = BrickType.NORMAL;
                if (type == BrickType.INDESTRUCTIBLE && i == map.length - 1)
                    type = BrickType.NORMAL;

                map[i][j] = new Bricks(brickX, brickY, brickWidth, brickHeight, type);
                if (map[i][j].getType() != BrickType.INDESTRUCTIBLE) {
                    totalBricks++;
                }
            }
        }
    }

    public void draw(Graphics g) {
        for (Bricks[] row : map) {
            for (Bricks brick : row) {
                brick.draw(g);
            }
        }
    }

    // 🧱 Xử lý va chạm bóng và gạch
    public int handleBallCollision(Ball ball) {
        return handleBallCollision(ball, new ArrayList<PowerUp>());
    }

    public int handleBallCollision(Ball ball, ArrayList<PowerUp> powerUps) {
        int brokenBricks = 0;
        Rectangle ballRect = ball.getBound();
        
        // Lưu vị trí trước đó của ball để xác định hướng va chạm
        int prevX = ball.x - ball.dx;
        int prevY = ball.y - ball.dy;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Bricks brick = map[i][j];
                if (!brick.isVisible()) continue;

                Rectangle brickRect = brick.getBounds();
                if (!ballRect.intersects(brickRect)) continue;

                // Xác định hướng va chạm dựa trên vị trí trước đó
                boolean hitFromLeft = prevX + ball.width <= brickRect.x;
                boolean hitFromRight = prevX >= brickRect.x + brickRect.width;
                boolean hitFromTop = prevY + ball.height <= brickRect.y;
                boolean hitFromBottom = prevY >= brickRect.y + brickRect.height;

                // Đổi chiều dx hoặc dy dựa trên hướng va chạm
                if (hitFromLeft || hitFromRight) {
                    // Va chạm từ trái hoặc phải -> đổi dx
                    ball.reverseX();
                } else if (hitFromTop || hitFromBottom) {
                    // Va chạm từ trên hoặc dưới -> đổi dy
                    ball.reverseY();
                } else {
                    // Va chạm góc -> đổi cả hai hoặc dựa trên overlap
                    int overlapLeft = (ballRect.x + ballRect.width) - brickRect.x;
                    int overlapRight = (brickRect.x + brickRect.width) - ballRect.x;
                    int overlapTop = (ballRect.y + ballRect.height) - brickRect.y;
                    int overlapBottom = (brickRect.y + brickRect.height) - ballRect.y;
                    
                    int minOverlap = Math.min(Math.min(overlapLeft, overlapRight), 
                                             Math.min(overlapTop, overlapBottom));
                    
                    if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                        ball.reverseX();
                    } else {
                        ball.reverseY();
                    }
                }

                // Xử lý loại gạch
                if (brick.getType() == BrickType.EXPLORE) {
                    brick.setVisible(false);
                    brokenBricks++;
                    totalBricks--;
                    brokenBricks += explore(i, j, powerUps);
                    spawnPowerUp(brick.x, brick.y, powerUps);
                    SoundManager.playSound(GameConstants.SOUND_EXPLORE);
                } else if (brick.getType() != BrickType.INDESTRUCTIBLE) {
                    SoundManager.playSound(GameConstants.SOUND_BALL_HIT_BRICK);
                    if (brick.hit()) {
                        brokenBricks++;
                        totalBricks--;
                        spawnPowerUp(brick.x, brick.y, powerUps);
                    }
                } else {
                    SoundManager.playSound(GameConstants.SOUND_INDESTRUCTIBLE);
                }

                // Chỉ xử lý một gạch mỗi frame
                return brokenBricks;
            }
        }
        return brokenBricks;
    }

    private int explore(int row, int col, ArrayList<PowerUp> powerUps) {
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

    private void spawnPowerUp(int x, int y, ArrayList<PowerUp> powerUps) {
        if (rand.nextInt(GameConstants.POWERUP_SPAWN_CHANCE) == GameConstants.ZERO) {
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
        return totalBricks <= GameConstants.ZERO;
    }
}