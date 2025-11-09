import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleSystem {
    private List<Particle> particles;

    public ParticleSystem() {
        particles = new ArrayList<>();
    }

    public void addExplosion(float x, float y, Color color, int count) {
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(x, y, color));
        }
    }

    public void update() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle p = iterator.next();
            p.update();
            if (!p.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics g) {
        for (Particle p : particles) {
            p.draw(g);
        }
    }

    public void clear() {
        particles.clear();
    }
}

