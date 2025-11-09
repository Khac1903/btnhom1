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
        // Draw animated background gradient
        drawAnimatedBackground(g, manager);
        
        manager.getBrickMap().draw(g);
        
        // Draw all balls with trail effect
        for (Ball ball : manager.getBalls()) {
            drawBallTrail(g, ball);
            ball.draw(g);
        }
        
        manager.getPaddle().draw(g);
        
        // Draw all active powerups with glow effect
        for (PowerUp powerUp : manager.getActivePowerUps()) {
            drawPowerUpGlow(g, powerUp);
            powerUp.draw(g);
        }
        
        // Draw particles
        manager.getParticleSystem().draw(g);
        
        // Draw score popups
        for (ScorePopup popup : manager.getScorePopups()) {
            popup.draw(g);
        }
        
        // Draw combo indicator
        if (manager.getComboCount() > 1) {
            drawComboIndicator(g, manager);
        }
        
        // Draw active powerup indicators
        drawPowerUpIndicators(g, manager);
    }
    
    private void drawAnimatedBackground(Graphics g, GameManager manager) {
        // Christmas night sky background
        // Dark blue gradient (night sky)
        for (int y = 0; y < 600; y += 2) {
            float brightness = 0.05f + (y / 600f) * 0.1f; // Darker at top, slightly lighter at bottom
            Color color = Color.getHSBColor(0.6f, 0.7f, brightness); // Blue tones
            g.setColor(color);
            g.fillRect(0, y, 800, 2);
        }
        
        // Add some stars
        g.setColor(new Color(255, 255, 255, 150));
        int time = (int)(System.currentTimeMillis() / 1000) % 100;
        for (int i = 0; i < 20; i++) {
            int starX = (i * 37 + time) % 800;
            int starY = (i * 23 + time * 2) % 300;
            int size = 1 + (i % 3);
            g.fillOval(starX, starY, size, size);
        }
        
        // Draw snowflakes
        manager.getSnowSystem().draw(g);
    }
    
    private void drawBallTrail(Graphics g, Ball ball) {
        // Christmas trail effect (white glow like snow)
        g.setColor(new Color(255, 255, 255, 80));
        g.fillOval(ball.x - 5, ball.y - 5, ball.width + 10, ball.height + 10);
    }
    
    private void drawPowerUpGlow(Graphics g, PowerUp powerUp) {
        // Glow effect for powerups
        Color glowColor = new Color(
            powerUp.getType().getColor().getRed(),
            powerUp.getType().getColor().getGreen(),
            powerUp.getType().getColor().getBlue(),
            100
        );
        g.setColor(glowColor);
        int glowSize = 5;
        g.fillOval(
            powerUp.x - glowSize,
            powerUp.y - glowSize,
            powerUp.width + glowSize * 2,
            powerUp.height + glowSize * 2
        );
    }
    
    private void drawComboIndicator(Graphics g, GameManager manager) {
        int combo = manager.getComboCount();
        float alpha = Math.min(1.0f, manager.getComboTimer() / 60f);
        
        g.setColor(new Color(255, 165, 0, (int)(255 * alpha)));
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String comboText = combo + "x COMBO!";
        FontMetrics fm = g.getFontMetrics();
        int x = 400 - fm.stringWidth(comboText) / 2;
        int y = 100;
        
        // Shadow
        g.setColor(new Color(0, 0, 0, (int)(150 * alpha)));
        g.drawString(comboText, x + 3, y + 3);
        
        // Main text
        g.setColor(new Color(255, 165, 0, (int)(255 * alpha)));
        g.drawString(comboText, x, y);
    }
    
    private void drawPowerUpIndicators(Graphics g, GameManager manager) {
        int x = 650;
        int y = 30;
        int spacing = 25;
        int index = 0;
        
        // Draw bigger paddle indicator
        if (manager.getBiggerPaddleTimer() > 0) {
            float progress = (float)manager.getBiggerPaddleTimer() / 600f;
            drawPowerUpIndicator(g, x, y + index * spacing, "P+", Color.GREEN, progress);
            index++;
        }
        
        // Draw active powerups count
        int activeCount = manager.getActivePowerUps().size();
        if (activeCount > 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("PowerUps: " + activeCount, x, y + index * spacing);
        }
    }
    
    private void drawPowerUpIndicator(Graphics g, int x, int y, String text, Color color, float progress) {
        // Background bar
        g.setColor(new Color(50, 50, 50));
        g.fillRect(x, y, 100, 15);
        
        // Progress bar
        g.setColor(color);
        g.fillRect(x, y, (int)(100 * progress), 15);
        
        // Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(text, x + 5, y + 12);
    }

    private void drawMenuScreen(Graphics g, int width, int height, GameManager manager) {
        // Christmas night sky background
        for (int y = 0; y < height; y += 2) {
            float brightness = 0.05f + (y / (float)height) * 0.1f;
            Color color = Color.getHSBColor(0.6f, 0.7f, brightness);
            g.setColor(color);
            g.fillRect(0, y, width, 2);
        }
        
        // Add stars
        g.setColor(new Color(255, 255, 255, 200));
        int time = (int)(System.currentTimeMillis() / 1000) % 100;
        for (int i = 0; i < 30; i++) {
            int starX = (i * 37 + time) % width;
            int starY = (i * 23 + time * 2) % height;
            int size = 1 + (i % 3);
            g.fillOval(starX, starY, size, size);
        }
        
        // Draw snowflakes in menu
        manager.getSnowSystem().draw(g);

        // Title with Christmas glow effect
        g.setFont(new Font("Consolas", Font.BOLD, 56));
        String title = "CHRISTMAS ARKANOID";
        FontMetrics titleFm = g.getFontMetrics();
        int titleX = (width - titleFm.stringWidth(title)) / 2;
        int titleY = height / 4;
        
        // Red and green alternating glow
        int pulse = (int)(Math.sin(System.currentTimeMillis() / 500.0) * 30 + 30);
        for (int i = 5; i > 0; i--) {
            Color glowColor = (i % 2 == 0) ? 
                new Color(220, 20, 60, pulse / i) : // Red
                new Color(34, 139, 34, pulse / i);  // Green
            g.setColor(glowColor);
            g.drawString(title, titleX - i, titleY - i);
            g.drawString(title, titleX + i, titleY + i);
        }
        
        // Main title (red and green gradient effect)
        g.setColor(new Color(220, 20, 60)); // Christmas Red
        g.drawString(title, titleX, titleY);

        // Menu options with animation
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String[] options = {"Start Game", "How to Play", "Exit"};
        int selected = manager.getSelectedMenuIndex();
        int baseY = height / 2;
        
        for(int i=0; i<options.length; i++) {
            int optionY = baseY + i * 60;
            int optionX = (width - g.getFontMetrics().stringWidth(options[i])) / 2;
            
            if(i == selected) {
                // Selected option - Christmas animated glow
                int optionPulse = (int)(Math.sin(System.currentTimeMillis() / 200.0) * 40 + 40);
                g.setColor(new Color(220, 20, 60, optionPulse)); // Red glow
                g.fillRect(optionX - 10, optionY - 30, 
                          g.getFontMetrics().stringWidth(options[i]) + 20, 40);
                
                g.setColor(Color.WHITE);
                g.drawString(options[i], optionX, optionY);
                
                // Christmas tree arrow indicator
                g.setColor(new Color(34, 139, 34)); // Green
                g.fillPolygon(
                    new int[]{optionX - 30, optionX - 15, optionX - 15},
                    new int[]{optionY - 5, optionY - 15, optionY + 5},
                    3
                );
            } else {
                g.setColor(new Color(200, 200, 200));
                g.drawString(options[i], optionX, optionY);
            }
        }
        
        // Instructions
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String instruction = "Use ↑/↓ to navigate, ENTER to select, SPACE to pause";
        g.drawString(instruction,
                (width - g.getFontMetrics().stringWidth(instruction)) / 2,
                height - 30);
    }

    private void drawPauseScreen(Graphics g, int width, int height) {
        g.setColor( Color.RED );
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics m = g.getFontMetrics();
        String msg = "TẠM DỪNG";
        g.drawString(msg, (width - m.stringWidth(msg)) / 2, height /2);

    }

    private void drawGameOver(Graphics g, int width, int height) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);
        
        // Animated "Game Over" text
        int pulse = (int)(Math.sin(System.currentTimeMillis() / 300.0) * 30 + 30);
        g.setColor(new Color(255, 0, 0, 200 + pulse));
        g.setFont(GAME_OVER_FONT);
        FontMetrics m = g.getFontMetrics(g.getFont());
        String msg = "GAME OVER";
        int msgX = (width - m.stringWidth(msg)) / 2;
        int msgY = height / 2;
        
        // Shadow
        g.setColor(new Color(0, 0, 0, 150));
        g.drawString(msg, msgX + 5, msgY + 5);
        
        // Main text
        g.setColor(new Color(255, 0, 0, 200 + pulse));
        g.drawString(msg, msgX, msgY);
        
        // Restart hint
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String hint = "Press ENTER to restart";
        g.drawString(hint, (width - g.getFontMetrics().stringWidth(hint)) / 2, msgY + 80);
    }
}
