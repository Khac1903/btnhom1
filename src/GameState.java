
public class GameState {
    private GameStatus status;

    public GameState() {
        this.status = GameStatus.MENU;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isMenu() {
        return status == GameStatus.MENU;
    }

    public boolean isReady() {
        return status == GameStatus.READY;
    }

    public boolean isRunning() {
        return status == GameStatus.RUNNING;
    }

    public boolean isPaused() { return status == GameStatus.PAUSED;}

    public boolean isGameOver() {
        return status == GameStatus.GAME_OVER;
    }
}
