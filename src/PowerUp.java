import java.awt.*;

public class PowerUp {
    public int x, y, width, height;
    private int dy;
    private PowerUpType type;
    private boolean isVisible;

    public PowerUp(int x, int y, PowerUpType type) {

        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 15;
        this.dy = 2;
        this.type = type;
        this.isVisible = true;
    }

    public void move(){
        y += dy;
    }
    public void draw (Graphics g){
        if(isVisible){
            g.setColor(type.color);
            g.fillRect(x, y, width, height);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));

            String text = "";
            switch (type) {
                case PADDLE_WIDE:
                    text = "W";
                    break;
                case MULTI_BALL:
                    text = "M";
                    break;
                case PADDLE_NARROW:
                    text = "N";
                    break;
                case BALL_FAST:
                    text = "F";
                    break;
            }


            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            int textX = x + (width - textWidth) / 2;
            int textY = y + ((height - textHeight) / 2) + fm.getAscent();

            g.drawString(text, textX, textY);

        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }
    public PowerUpType getType(){
        return type;
    }
    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }
}
