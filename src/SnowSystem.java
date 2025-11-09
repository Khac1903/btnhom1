import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnowSystem {
    private List<Snowflake> snowflakes;
    private int width, height;

    public SnowSystem(int width, int height) {
        snowflakes = new ArrayList<>();
        this.width = width;
        this.height = height;

        // Initialize with some snowflakes
        for (int i = 0; i < 30; i++) {
            snowflakes.add(new Snowflake(
                (float)(Math.random() * width),
                (float)(Math.random() * height)
            ));
        }
    }

    public void update() {
        int toAdd = 0;

        Iterator<Snowflake> iterator = snowflakes.iterator();
        while (iterator.hasNext()) {
            Snowflake snowflake = iterator.next();
            snowflake.update(width, height);

            if (!snowflake.isAlive()) {
                iterator.remove();
                toAdd++; // đánh dấu cần thêm 1 bông tuyết mới
            }
        }

        // Thêm snowflakes sau khi duyệt xong (an toàn)
        for (int i = 0; i < toAdd; i++) {
            snowflakes.add(new Snowflake(
                    (float)(Math.random() * width),
                    -10
            ));
        }

        // Đảm bảo có ít nhất 30 snowflakes
        while (snowflakes.size() < 30) {
            snowflakes.add(new Snowflake(
                    (float)(Math.random() * width),
                    (float)(Math.random() * height)
            ));
        }
    }


    public void draw(Graphics g) {
        for (Snowflake snowflake : snowflakes) {
            snowflake.draw(g);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}

