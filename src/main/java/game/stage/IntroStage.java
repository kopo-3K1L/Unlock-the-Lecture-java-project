package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

public class IntroStage extends AbstractStage {

    public IntroStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "인트로";
    }

    /**
     * 인트로 스테이지 실행
     *
     * <p>
     * 사용자가 'start'를 입력하면 게임을 시작합니다.
     */
    @Override
    public StageResult play() {
        printStageHeader(1, "                🎮 인트로! 🎮");
        io.println("강의를 탈출하려면 모든 게임을 클리어해야 합니다.");
        io.println("'start'를 입력하면 시작합니다.");
        printCommandGuide();

        String input = io.nextLine();

        StageResult commandResult = checkCommonCommand(input);
        if (commandResult != null) {
            return commandResult;
        }

        if ("start".equalsIgnoreCase(input)) {
            return new StageResult(StageResultType.SUCCESS, "게임을 시작합니다.");
        }

        return new StageResult(StageResultType.FAIL, "입력이 올바르지 않습니다.");
    }
}