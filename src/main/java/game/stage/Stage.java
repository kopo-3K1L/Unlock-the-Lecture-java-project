package game.stage;

public interface Stage {
    String getTitle();
    void showIntro();
    StageResult play();
}