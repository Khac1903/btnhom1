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
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }


    public void changeWidth(int amount) {
        this.width += amount;

        if(this.width < 50){
            this.width = 50;
        }
        if(this.width > 200){
            this.width = 200;
        }
    }
    public void resetWidth(){
        this.width = 125;
    }
}
