import java.awt.*;

public class PowerUp extends MoveObject {
    private PowerUpType type;
    private boolean active;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, type.getColor(), 2, 0, 20, 20, y);
        this.type = type;
        this.active = true;
    }

    public void updatePosition(int panelHeight) {
        y += dy;
        if (y >= panelHeight) {
            active = false;
        }
    }

    public boolean checkPaddleCollision(Paddle paddle) {
        if (!active) return false;
        Rectangle powerupRect = this.getBound();
        Rectangle paddleRect = paddle.getBound();
        if (powerupRect.intersects(paddleRect)) {
            active = false;
            return true;
        }
        return false;
    }

    public PowerUpType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void draw(Graphics g) {
        if (!active) return;
        
        if (type.getImage() != null) {
            g.drawImage(type.getImage(), x, y, width, height, null);
        } else {
            // Fallback: draw colored rectangle with symbol
            g.setColor(type.getColor());
            g.fillRect(x, y, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            String symbol = getSymbol();
            FontMetrics fm = g.getFontMetrics();
            int textX = x + (width - fm.stringWidth(symbol)) / 2;
            int textY = y + (height + fm.getAscent()) / 2;
            g.drawString(symbol, textX, textY);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }

    private String getSymbol() {
        switch (type) {
            case BIGGER_PADDLE: return "🎄"; // Christmas tree
            case EXTRA_LIFE: return "❤️"; // Heart
            case MULTI_BALL: return "⭐"; // Star
            case SLOW_BALL: return "❄️"; // Snowflake
            case FAST_BALL: return "🎁"; // Gift
            default: return "?";
        }
    }
}
