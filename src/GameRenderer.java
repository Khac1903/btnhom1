import java.awt.*;

public class GameRenderer {
    private static final Font MENU_FONT = new Font("Arial", Font.BOLD, 30);
    private static final Font GAME_OVER_FONT = new Font("Ink Free", Font.BOLD, 75);

    public void render(Graphics g, GameManager manager, int width, int height) {
        GameState state = manager.getGameState();

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

        manager.getScoreManager().draw(g);
        manager.getLevelManager().draw(g);
        manager.getPlayerManager().draw(g);
    }

    private void drawGame(Graphics g, GameManager manager) {
        manager.getPaddle().draw(g);
        manager.getBall().draw(g);
        manager.getBrickMap().draw(g);
    }

    private void drawMenuScreen(Graphics g, int width, int height, GameManager manager) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 48));
        String title = "AKANOID";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title))/2, height/4);

        g.setFont(new Font("Arial", Font.BOLD, 28));
        String[] options = {"Start Game", "How to Game", "Exit"};
        int selected = manager.getSelectedMenuIndex();
        for(int i=0; i<options.length; i++) {
            if(i == selected) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.drawString(options[i], (width - g.getFontMetrics().stringWidth(options[i]))/2, height/2 + i*50 );
        }
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Use ↑/↓ to move, ENTER to select, SPACE to paused",
                (width - g.getFontMetrics().stringWidth("Use ↑/↓ to move, ENTER to select, SPACE to paused")) / 2,
                height - 40);
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

        String[] options = {"Resume", "Restart", "Exit to Menu"};
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
