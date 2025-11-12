import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameRenderer {
    private static final Font MENU_FONT = new Font("Arial", Font.BOLD, 30);
    private static final Font GAME_OVER_FONT = new Font("Ink Free", Font.BOLD, 75);
    private BufferedImage backgroundImage;
    private BufferedImage menuImage;
    private BufferedImage titleImage;
    public GameRenderer(){
        try {
            backgroundImage = ImageIO.read(new File("images/back_ground_game.jpg"));
        } catch (IOException e) {
            System.err.println("Khong the in anh background");
            e.printStackTrace();
            backgroundImage = null;
        }
        try {
            menuImage = ImageIO.read(new File("images/background.jpg"));
        }
        catch (IOException e) {
            System.err.println("Khong the in anh background");
            e.printStackTrace();
            menuImage = null;
        }
        try {
            titleImage = ImageIO.read(new File("images/sf.png"));
        }
        catch (IOException e) {
            System.err.println("Khong the in anh");
            e.printStackTrace();
            titleImage = null;
        }
    }
    public void render(Graphics g, GameManager manager, int width, int height) {
        GameState state = manager.getGameState();
        if (state.isReady() || state.isRunning() || state.isPaused() || state.isGameOver()) {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, width, height, null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);
            }
        }
        switch (state.getStatus()) {
            case MENU:
                drawMenuScreen(g, width, height, manager);
                break;
            case READY:
            case RUNNING:
                drawGame(g, manager);
                break;
            case PAUSED:
                drawGame(g, manager);
                drawPauseMenu(g, width, height, manager);
                break;
            case GAME_OVER:
                drawGameOverMenu(g, width, height, manager);
                break;
        }

        // Hiển thị Highest Score và tên người chơi CHỈ trong các menu (MENU, PAUSED, GAME_OVER)
        if (state.isMenu() || state.isPaused() || state.isGameOver()) {
            g.setFont(new Font("Arial", Font.BOLD, 18));

            // Highest score (trên cùng)
            g.setColor(Color.YELLOW);
            int highest = manager.getHighScoreManager().getHighestScore();
            g.drawString("Highest Score: " + highest, 10, 22);

            // Player name (ngay dưới Highest Score)
            g.setColor(Color.CYAN);
            g.drawString("Player: " + manager.getPlayerName(), 10, 44);
        }

        // Chỉ hiển thị Score, Level, Lives trong quá trình chơi (READY, RUNNING)
        if (state.isReady() || state.isRunning()) {
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.setColor(Color.GREEN);
            String levelText = "Level: " + manager.getLevelManager().getLevel();
            g.drawString(levelText, 10, 22);

            g.setColor(Color.CYAN);
            String scoreText = "Score: " + manager.getScoreManager().getScore();
            int scoreX = (width - g.getFontMetrics().stringWidth(scoreText)) / 2;
            g.drawString(scoreText, scoreX, 22);

            g.setColor(Color.PINK);
            String livesText = "Lives: " + manager.getPlayerManager().getLives();
            int livesX = width - g.getFontMetrics().stringWidth(livesText) - 30;
            g.drawString(livesText, livesX, 22);
        }
    }

    private void drawGame(Graphics g, GameManager manager) {
        manager.getPaddle().draw(g);
        for(Ball ball : manager.getBalls()) {
            ball.draw(g);
        }
        for(PowerUp pu : manager.getPowerUps()){
            pu.draw(g);
        }
        manager.getBrickMap().draw(g);
    }

    private void drawMenuScreen(Graphics g, int width, int height, GameManager manager) {
            if (menuImage != null) {
                g.drawImage(menuImage, 0, 0, width, height, null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);
            }

        // Vẽ tiêu đề gần chính giữa main menu
        if (titleImage != null) {
            g.drawImage(titleImage, 210 , 20, 400, 200, null);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 48));
            String title = "ARKANOID";
            g.drawString(title, (width - g.getFontMetrics().stringWidth(title))/2, height / 4);
        }


        // Vẽ menu options ở trung tâm
        String[] options = {"Start Game", "Top 5 Players", "How to Game", "Exit"};
        int selected = manager.getSelectedMenuIndex();

        int menuStartY = height / 2 - options.length * 20;
        g.setFont(new Font("Arial", Font.BOLD, 28));
        for (int i = 0; i < options.length; i++) {
            if (i == selected) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            String option = options[i];
            g.drawString(option,
                    (width - g.getFontMetrics().stringWidth(option)) / 2,
                    menuStartY + i * 50);
        }

        // Vẽ hướng dẫn
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Use ↑/↓ to move, ENTER to select, SPACE to paused",
                (width - g.getFontMetrics().stringWidth("Use ↑/↓ to move, ENTER to select, SPACE to paused")) / 2,
                height - 50);
    }

    private void drawGameOverMenu(Graphics g, int width, int height, GameManager manager) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "GAME OVER";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, height / 3);

        String[] options = {"Main Menu", "Exit"};
        int selected = manager.getGameOverMenuIndex();

        g.setFont(new Font("Arial", Font.BOLD, 28));
        for (int i = 0; i < options.length; i++) {
            if (i == selected)
                g.setColor(Color.YELLOW);
            else
                g.setColor(Color.LIGHT_GRAY);

            g.drawString(
                    options[i],
                    (width - g.getFontMetrics().stringWidth(options[i])) / 2,
                    height / 2 + i * 50
            );
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.WHITE);
        g.drawString("Use ↑/↓ to move, ENTER to select",
                (width - g.getFontMetrics().stringWidth("Use ↑/↓ to move, ENTER to select")) / 2,
                height - 40);
    }

    private void drawPauseMenu(Graphics g, int width, int height, GameManager manager) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String title = "PAUSED";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, height / 3);

        String[] options = {"Restart", "Resume", "Exit to Menu"};
        int selected = manager.getPauseMenuIndex();

        g.setFont(new Font("Arial", Font.BOLD, 28));
        for (int i = 0; i < options.length; i++) {
            if (i == selected)
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.LIGHT_GRAY);

            g.drawString(
                    options[i],
                    (width - g.getFontMetrics().stringWidth(options[i])) / 2,
                    height / 2 + i * 50
            );
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.WHITE);
        g.drawString("Use ↑/↓ to move, ENTER to select",
                (width - g.getFontMetrics().stringWidth("Use ↑/↓ to move, ENTER to select")) / 2,
                height - 40);
    }
}