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
        stages.add(new IntroStage(io));
        stages.add(new UpDownStage(io));
        stages.add(new TimerStage(io));
        stages.add(new RGBStage(io));
        stages.add(new DragStage(io));
        stages.add(new MazeStage(io));
        stages.add(new DarkStage(io));
        stages.add(new StairsStage(io));

        GameManager gameManager = new GameManager(io, stages, progress, repository);
        gameManager.start();
    }
}