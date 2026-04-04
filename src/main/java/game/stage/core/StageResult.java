package game.stage.core;

public class StageResult {
    private final StageResultType type;
    private final String message;

    public StageResult(StageResultType type, String message) {
        this.type = type;
        this.message = message;
    }

    public StageResultType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}