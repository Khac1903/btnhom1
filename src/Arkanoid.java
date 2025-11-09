import  javax.swing.JFrame;
import java.awt.*;
public class Arkanoid extends JFrame {
    public Arkanoid(){
        setTitle("Arkanoid Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false); // Prevent resizing for better performance
        add(new GamePanel());
        setVisible(true);
    }
    public static void main(String[] args) {
        new Arkanoid();
    }
}