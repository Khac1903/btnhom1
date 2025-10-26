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
                drawPauseScreen(g, width, height);
                break;
            case GAME_OVER:
                drawGameOver(g, width, height);
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

    private void drawPauseScreen(Graphics g, int width, int height) {
        g.setColor( Color.RED );
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics m = g.getFontMetrics();
        String msg = "TẠM DỪNG";
        g.drawString(msg, (width - m.stringWidth(msg)) / 2, height /2);

    }

    private void drawGameOver(Graphics g, int width, int height) {
        g.setColor(Color.RED);
        g.setFont(GAME_OVER_FONT);
        FontMetrics m = g.getFontMetrics(g.getFont());
        String msg = "Game Over";
        g.drawString(msg, (width - m.stringWidth(msg)) / 2, height / 2);
    }
}
