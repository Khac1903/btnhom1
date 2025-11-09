import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    final int PANEL_WIDTH = 800;
    final int PANEL_HEIGHT = 600;

    private GameManager gameManager;
    private GameRenderer gameRenderer;
    private Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setDoubleBuffered(true); // Enable double buffering for smoother rendering
        addKeyListener(new GameKeyAdapter());

        gameManager = new GameManager();
        gameRenderer = new GameRenderer();

        // 16ms = ~60 FPS (smooth gameplay)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.render(g, gameManager, PANEL_WIDTH, PANEL_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameManager.update(getWidth(), getHeight());
        repaint();
    }

    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            GameState state = gameManager.getGameState();

            if(state.isMenu()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.moveMenuSelectionUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.moveMenuSelectionDown();
                } else if (key == KeyEvent.VK_ENTER) {
                    gameManager.selectMenuOption();
                }
                return; // không xử lý phím khác khi đang ở menu
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                if (state.isReady() || state.isRunning())
                    gameManager.getPaddle().keyPressed(e);
            }
            if (key == KeyEvent.VK_UP && state.isReady()) {
                gameManager.getBall().launch();
                state.setStatus(GameStatus.RUNNING);
            }
            if (key == KeyEvent.VK_ENTER && (state.isMenu() || state.isGameOver())) {
                gameManager.reset();
            }
            if (key == KeyEvent.VK_SPACE) {
                if(state.isRunning())
                {
                    state.setStatus(GameStatus.PAUSED);
                }
                else {
                    if(state.isPaused()) {
                        state.setStatus(GameStatus.RUNNING);
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            GameState state = gameManager.getGameState();
            if (state.isRunning() || state.isReady())
                gameManager.getPaddle().keyReleased(e);
        }
    }
}
