import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    final int PANEL_WIDTH = 800;
    final int PANEL_HEIGHT = 600;

    private GameManager gameManager;
    private GameRenderer gameRenderer;
    Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new GameKeyAdapter());

        gameManager = new GameManager();
        gameRenderer = new GameRenderer();

        timer = new Timer(10, this);
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

            if (state.isMenu()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.moveMenuSelectionUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.moveMenuSelectionDown();
                } else if (key == KeyEvent.VK_ENTER) {
                    gameManager.selectMenuOption();
                }
                return;
            }

            if (state.isPaused()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.movePauseMenuUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.movePauseMenuDown();
                } else if (key == KeyEvent.VK_ENTER) {
                    gameManager.selectPauseMenuOption();
                } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_P) {
                    state.setStatus(GameStatus.RUNNING);
                }
                return;
            }

            if (state.isGameOver()) {
                if (key == KeyEvent.VK_UP) {
                    gameManager.moveGameOverMenuUp();
                } else if (key == KeyEvent.VK_DOWN) {
                    gameManager.moveGameOverMenuDown();
                } else if (key == KeyEvent.VK_ENTER) {
                    gameManager.selectGameOverOption();
                }
                return;
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                if (state.isReady() || state.isRunning()) {
                    gameManager.getPaddle().keyPressed(e);
                }
            }

            if (key == KeyEvent.VK_UP && state.isReady()) {
                gameManager.getBall().launch();
                state.setStatus(GameStatus.RUNNING);
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_P) {
                if (state.isRunning()) {
                    state.setStatus(GameStatus.PAUSED);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            GameState state = gameManager.getGameState();
            if (state.isRunning() || state.isReady()) {
                gameManager.getPaddle().keyReleased(e);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}
