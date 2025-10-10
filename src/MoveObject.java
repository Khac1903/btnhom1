import java.awt.*;

public class MoveObject {
    protected int x, y;
    protected int width, height;
    protected int dx, dy;
    protected Color color;

    public MoveObject(int x, Color color, int dy, int dx, int height, int width, int y) {
        this.x = x;
        this.color = color;
        this.dy = dy;
        this.dx = dx;
        this.height = height;
        this.width = width;
        this.y = y;
    }
    public void move(int panelWidth, int panelHeight){
        x += dx;
        y += dy;
        if( x <= 0 || x >= panelWidth - width){
            dx = -dx;
        }
        if(y<=0 || y >= panelHeight - height){
            dy = -dy;
        }
    }

    public void draw(Graphics g){
        g.setColor(this.color);
        g.fillRect(x,y,width,height);
    }
    public Rectangle getBound(){
        return new Rectangle(x,y,width,height);
    }
}
