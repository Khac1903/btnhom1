import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    private static HashMap<String, Clip> clips = new HashMap<>();

    // Load âm thanh 1 lần trước
    public static void loadSound(String name, String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clips.put(name, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phát âm thanh bất đồng bộ, không chậm
    public static void playSoundAsync(String name) {
        Clip clip = clips.get(name);
        if (clip == null) return;

        new Thread(() -> {
            synchronized (clip) { // tránh xung đột nếu gọi nhiều lần
                if (clip.isRunning()) clip.stop();
                clip.setFramePosition(0);
                clip.start();
            }
        }).start();
    }
}
