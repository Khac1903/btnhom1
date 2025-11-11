import javax.sound.sampled.*;
import java.io.File;

public class SoundThread implements Runnable {
    private String filePath;

    public SoundThread(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            // Đợi clip kết thúc
            Thread.sleep(clip.getMicrosecondLength() / 1000);
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

