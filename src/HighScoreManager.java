import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    public static class ScoreEntry {
        public String name;
        public int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public List<ScoreEntry> getTopScores(int topN) {
        List<ScoreEntry> scores = new ArrayList<>();
        
        for (String key : properties.stringPropertyNames()) {
            try {
                int score = Integer.parseInt(properties.getProperty(key));
                scores.add(new ScoreEntry(key, score));
            } catch (NumberFormatException e) {
                // Bỏ qua các giá trị không hợp lệ
            }
        }
        
        // Sắp xếp theo điểm giảm dần
        scores.sort((a, b) -> Integer.compare(b.score, a.score));
        
        // Trả về top N
        return scores.stream().limit(topN).collect(Collectors.toList());
    }

    public int getHighestScore() {
        List<ScoreEntry> topScores = getTopScores(1);
        if (topScores.isEmpty()) {
            return 0;
        }
        return topScores.get(0).score;
    }
}
