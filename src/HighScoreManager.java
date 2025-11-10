import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class HighScoreManager {
    private Properties properties;
    private final String highScoreFile = "highScores.properties";

    public HighScoreManager(){
        properties = new Properties();
        loadScore();
    }
    private void loadScore(){
        File file = new File(highScoreFile);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Không thể tải file điểm cao: " + e.getMessage());
            }
        }
    }
    private void saveScores() {
        try (FileOutputStream fos = new FileOutputStream(highScoreFile)) {
            properties.store(fos, "Arkanoid High Scores");
        } catch (IOException e) {
            System.err.println("Không thể lưu file điểm cao: " + e.getMessage());
        }
    }
    public int getHighScore(String playerName) {
        String scoreStr = properties.getProperty(playerName, "0");
        try {
            return Integer.parseInt(scoreStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void updateScore(String playerName, int newScore) {
        int currentHighScore = getHighScore(playerName);
        if (newScore > currentHighScore) {
            properties.setProperty(playerName, String.valueOf(newScore));
            saveScores();
        }
    }
}