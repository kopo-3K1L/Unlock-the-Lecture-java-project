package game.stage.core;

public interface Stage {
    String getTitle();
    void showIntro();
    StageResult play();
}