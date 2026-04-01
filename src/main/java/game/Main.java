package game;

import game.io.ConsoleIO;
import game.manager.GameManager;
import game.model.PlayerProgress;
import game.repository.FileProgressRepository;
import game.repository.ProgressRepository;
import game.stage.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConsoleIO io = new ConsoleIO();

        ProgressRepository repository = new FileProgressRepository("save.txt");
        PlayerProgress progress = repository.load();

        List<Stage> stages = new ArrayList<>();
        stages.add(new IntroStage(io)); // 교체예정 1번
        stages.add(new UpDownStage(io)); // 2번
        stages.add(new TimerStage(io)); // 3번
        stages.add(new RGBStage(io)); // 4번
        stages.add(new DragStage(io)); // 5번
        stages.add(new MemoryStage(io)); // 6번
        stages.add(new MazeStage(io)); // 9번
        stages.add(new DarkStage(io)); // 10번
        stages.add(new PhotoStage(io)); // 11번
        stages.add(new StairsStage(io)); // 12번

        GameManager gameManager = new GameManager(io, stages, progress, repository);
        gameManager.start();
    }
}