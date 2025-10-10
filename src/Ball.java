import java.awt.*;

public class Ball extends MoveObject {
    public Ball(int x, int y, int size, int dx, int dy, Color color){
        super(x,color,dy,dx,size,size,y);
    }
    public void updatePosition(){
        x += dx;
        y+= dy;
    }
    public void handleWallCollision(int panelWidth) {
        if (x <= 0 || x >= panelWidth - width) {
            dx = -dx;
        }
        if (y <= 0) {
            dy = -dy;
        }
    }
    public void handlePaddleCollision(Paddle paddle) {
        if (this.getBound().intersects(paddle.getBound())) {
            dy = -dy;
            y = paddle.y - height;
        }
    }
    public void reverseDirection() {
        dy = -dy;
    }
    public boolean isOutOfBounds(int panelHeight) {
        return y >= panelHeight;
    }
    public void stickToPaddle(Paddle paddle){
        this.x= paddle.x + (paddle.width)/2 - (this.width/2);
        this.y= paddle.y - this.height;
    }
    public void launch(){
        this.dx = 2;
        this.dy = -3;
    }

}
