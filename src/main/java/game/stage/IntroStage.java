package game.stage;

import game.io.ConsoleIO;

public class IntroStage extends AbstractStage {

    public IntroStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "인트로";
    }

    @Override
    public StageResult play() {
        showIntro();
        io.println("강의를 탈출하려면 모든 게임을 클리어해야 합니다.");
        io.println("'start'를 입력하면 시작합니다.");

        String input = io.nextLine();

        StageResult commandResult = checkCommonCommand(input);
        if (commandResult != null) {
            return commandResult;
        }

        if ("start".equalsIgnoreCase(input)) {
            return new StageResult(StageResultType.SUCCESS, "게임을 시작합니다.");
        } else {
            return new StageResult(StageResultType.FAIL, "입력이 올바르지 않습니다.");
        }
    }
}