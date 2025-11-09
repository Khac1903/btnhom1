import java.awt.Color;

public enum PowerUpType {
    PADDLE_WIDE (Color.GREEN),
    MULTI_BALL(Color.YELLOW) ,
    PADDLE_NARROW (Color.RED),
    BALL_FAST (Color.BLUE);

    public final Color color;

    PowerUpType(Color color){
        this.color = color;
    }
}
