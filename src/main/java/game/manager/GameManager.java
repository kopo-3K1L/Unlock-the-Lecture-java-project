package game.manager;

import game.io.ConsoleIO;
import game.model.PlayerProgress;
import game.repository.ProgressRepository;
import game.stage.Stage;
import game.stage.StageResult;
import game.stage.StageResultType;

import java.util.List;

public class GameManager {
    private final ConsoleIO io;
    private final List<Stage> stages;
    private final PlayerProgress progress;
    private final ProgressRepository repository;

    public GameManager(ConsoleIO io, List<Stage> stages, PlayerProgress progress, ProgressRepository repository) {
        this.io = io;
        this.stages = stages;
        this.progress = progress;
        this.repository = repository;
    }

    public void start() {
        while (true) {
            printMenu();
            int menu = io.nextIntInRange("선택: ", 1, 3);

            switch (menu) {
                case 1:
                    playGame();
                    break;
                case 2:
                    resetProgress();
                    break;
                case 3:
                    io.println("게임을 종료합니다.");
                    return;
            }
        }
    }

    private void printMenu() {
        io.println("\n==============================");
        io.println(" Unlock the Lecture - Console ");
        io.println("==============================");
        io.println("현재 진행 스테이지: " + (progress.getCurrentStageIndex() + 1));
        io.println("1. 이어하기");
        io.println("2. 처음부터");
        io.println("3. 종료");
    }

    private void playGame() {
        while (progress.getCurrentStageIndex() < stages.size()) {
            Stage currentStage = stages.get(progress.getCurrentStageIndex());
            StageResult result = currentStage.play();

            io.println(result.getMessage());

            if (result.getType() == StageResultType.SUCCESS || result.getType() == StageResultType.SKIP) {
                progress.advance();
                repository.save(progress);
            } else if (result.getType() == StageResultType.RETRY) {
                io.println("현재 스테이지를 다시 시작합니다.");
            } else if (result.getType() == StageResultType.EXIT) {
                repository.save(progress);
                return;
            } else if (result.getType() == StageResultType.FAIL) {
                io.println("실패했습니다. 다시 도전하세요.");
            }
        }

        io.println("모든 스테이지를 클리어했습니다!");
        repository.save(progress);
    }

    private void resetProgress() {
        progress.reset();
        repository.save(progress);
        io.println("저장된 진행 상태가 초기화되었습니다.");
    }
}