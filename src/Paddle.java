import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends MoveObject{
    public Paddle(int x, int y, int width, int height ,Color color){
        super(x,color,0,0,height,width,y);
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
}
