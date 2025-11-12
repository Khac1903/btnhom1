import java.awt.Color;

/**
 * GameConstants - Chứa tất cả các hằng số được sử dụng trong game
 */
public final class GameConstants {
    
    // Private constructor để ngăn việc khởi tạo
    private GameConstants() {
        throw new AssertionError("Cannot instantiate GameConstants");
    }
    
    // ========== PANEL CONSTANTS ==========
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;
    public static final int TIMER_DELAY = 10;
    
    // ========== COMMON CONSTANTS ==========
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    
    // ========== BALL CONSTANTS ==========
    public static final int BALL_SIZE = 20;
    public static final int BALL_INITIAL_DX = 2;
    public static final int BALL_INITIAL_DY = -3;
    public static final int BALL_DX_THRESHOLD = 3;
    public static final int BALL_DX_ADJUSTMENT = 2;
    public static final int BALL_INITIAL_X = 400;
    public static final int BALL_INITIAL_Y = 300;
    public static final int BALL_INITIAL_X_ALT = 390;
    public static final int BALL_INITIAL_Y_ALT = 450;
    public static final int BALL_DX_ALT = -2;
    public static final int BALL_DY_ALT = 4;
    public static final Color BALL_COLOR = Color.YELLOW;
    public static final Color BALL_COLOR_ALT = Color.RED;
    
    // ========== PADDLE CONSTANTS ==========
    public static final int PADDLE_ORIGINAL_WIDTH = 100;
    public static final int PADDLE_MAX_WIDTH = 150;
    public static final int PADDLE_MIN_WIDTH = 50;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_INITIAL_X = 350;
    public static final int PADDLE_INITIAL_Y = 500;
    public static final int PADDLE_INITIAL_Y_ALT = 550;
    public static final int PADDLE_SPEED = 5;
    public static final int PADDLE_WIDTH_CHANGE = 25;
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final Color PADDLE_COLOR_ALT = Color.GREEN;
    
    // ========== BRICK CONSTANTS ==========
    public static final int BRICK_AREA_WIDTH = 700;
    public static final int BRICK_AREA_HEIGHT = 200;
    public static final int BRICK_OFFSET_X = 50;
    public static final int BRICK_OFFSET_Y = 50;
    public static final int BRICK_DEFAULT_ROWS = 5;
    public static final int BRICK_DEFAULT_COLS = 9;
    
    // Brick generation chances
    public static final int BRICK_DURABLE_CHANCE_BASE = 10;
    public static final int BRICK_DURABLE_CHANCE_MULTIPLIER = 4;
    public static final int BRICK_INDESTRUCTIBLE_CHANCE_BASE = 5;
    public static final int BRICK_INDESTRUCTIBLE_CHANCE_MULTIPLIER = 2;
    public static final int BRICK_EXPLORE_CHANCE = 5;
    public static final int BRICK_RANDOM_MAX = 100;
    
    // ========== POWER UP CONSTANTS ==========
    public static final int POWERUP_SPAWN_CHANCE = 4; // 1/4 = 25%
    public static final double POWERUP_SPEED_MULTIPLIER = 1.2;
    
    // ========== MENU CONSTANTS ==========
    public static final int MENU_INDEX_START = 0;
    public static final int MENU_INDEX_TOP_PLAYERS = 1;
    public static final int MENU_INDEX_HOW_TO_PLAY = 2;
    public static final int MENU_INDEX_EXIT = 3;
    public static final int MENU_MAX_INDEX = 3;
    
    public static final int PAUSE_MENU_INDEX_RESTART = 0;
    public static final int PAUSE_MENU_INDEX_RESUME = 1;
    public static final int PAUSE_MENU_INDEX_EXIT = 2;
    public static final int PAUSE_MENU_MAX_INDEX = 2;
    
    public static final int GAME_OVER_MENU_INDEX_MAIN_MENU = 0;
    public static final int GAME_OVER_MENU_INDEX_EXIT = 1;
    public static final int GAME_OVER_MENU_MAX_INDEX = 1;
    
    public static final int TOP_SCORES_COUNT = 5;
    
    // ========== LEVEL CONSTANTS ==========
    public static final int LEVEL_FILE_START = 1;
    public static final String LEVEL_FILE_PATH = "levels/level";
    public static final String LEVEL_FILE_EXTENSION = ".txt";
    
    // ========== SOUND FILE PATHS ==========
    public static final String SOUND_BALL_HIT_PADDLE = "src/sounds/ball_hit_paddle.wav";
    public static final String SOUND_BALL_HIT_BRICK = "src/sounds/ball_hit_brick.wav";
    public static final String SOUND_EXPLORE = "src/sounds/explore.wav";
    public static final String SOUND_INDESTRUCTIBLE = "src/sounds/indestructible.wav";
    public static final String SOUND_GAMEOVER = "src/sounds/gameover.wav";
    public static final String SOUND_LOSE_LIVES = "src/sounds/loselives.wav";
    public static final String SOUND_POWERUP = "src/sounds/powerup.wav";
    
    // ========== IMAGE FILE PATHS ==========
    public static final String IMAGE_BALL = "images/ball.png";
    public static final String IMAGE_PADDLE = "images/paddle.png";
}

