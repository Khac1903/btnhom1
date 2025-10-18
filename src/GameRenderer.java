import java.awt.*;

public class GameRenderer {
    private static final Font MENU_FONT = new Font("Arial", Font.BOLD, 30);
    private static final Font GAME_OVER_FONT = new Font("Ink Free", Font.BOLD, 75);

    public void render(Graphics g, GameManager manager, int width, int height) {
        GameState state = manager.getGameState();

        switch (state.getStatus()) {
            case MENU:
                drawStartScreen(g, width, height);
                break;
            case READY:
            case RUNNING:
                drawGame(g, manager);
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

    private void drawStartScreen(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);
        g.setFont(MENU_FONT);
        String msg = "Nhấn Enter để bắt đầu";
        FontMetrics m = g.getFontMetrics(g.getFont());
        g.drawString(msg, (width - m.stringWidth(msg)) / 2, height / 2);
    }

    private void drawGameOver(Graphics g, int width, int height) {
        g.setColor(Color.RED);
        g.setFont(GAME_OVER_FONT);
        FontMetrics m = g.getFontMetrics(g.getFont());
        String msg = "Game Over";
        g.drawString(msg, (width - m.stringWidth(msg)) / 2, height / 2);
    }
}
