package game.model;

public class PlayerProgress {
    private int currentStageIndex;

    public PlayerProgress() {
        this.currentStageIndex = 0;
    }

    public PlayerProgress(int currentStageIndex) {
        this.currentStageIndex = Math.max(0, currentStageIndex);
    }

    public int getCurrentStageIndex() {
        return currentStageIndex;
    }

    public void advance() {
        currentStageIndex++;
    }

    public void reset() {
        currentStageIndex = 0;
    }
}