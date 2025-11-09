import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Paddle extends MoveObject{
   private BufferedImage image;
    public Paddle(int x, int y, int width, int height ,Color color , BufferedImage image){
        super(x,color,0,0,height,width,y);
        this.image = image;
    }
    public Paddle(int x, int y, int width, int height, Color color) {
        super(x,color,0,0,height,width,y);
        try {
           this.image = ImageIO.read(new File("images/paddle.png"));
        }
        catch (IOException e) {
            System.out.println("Không thể tải ảnh paddle!");
            e.printStackTrace();
            this.image = null;
        }
    }
    @Override
    public void move(int panelWidth, int panelHeight){
        x += dx;
        if(x<=0){
            x=0;
        }
        if(x>=panelWidth - width){
            x = panelWidth - width;
        }
    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            dx = -5;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            dx = 5;
        }
    }

    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT){
            dx = 0;
        }
    }
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            // Christmas sleigh paddle
            // Main body (red)
            g.setColor(new Color(220, 20, 60)); // Christmas Red
            g.fillRoundRect(x, y, width, height, 10, 10);
            
            // Green trim
            g.setColor(new Color(34, 139, 34)); // Christmas Green
            g.fillRoundRect(x, y, width, height/3, 10, 10);
            g.fillRoundRect(x, y + 2*height/3, width, height/3, 10, 10);
            
            // Gold runners (sleigh effect)
            g.setColor(new Color(255, 215, 0)); // Gold
            g.fillOval(x - 5, y + height - 3, 10, 6);
            g.fillOval(x + width - 5, y + height - 3, 10, 6);
            
            // Border
            g.setColor(new Color(139, 69, 19)); // Brown
            g.drawRoundRect(x, y, width, height, 10, 10);
        }
    }

    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    public int getOriginalWidth() {
        return 100; // Default width
    }
}
