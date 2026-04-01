package game.stage;

import game.io.ConsoleIO;

public abstract class AbstractStage implements Stage {
    protected final ConsoleIO io;

    public AbstractStage(ConsoleIO io) {
        this.io = io;
    }

    @Override
    public void showIntro() {
        io.println("\n------------------------------");
        io.println("스테이지: " + getTitle());
        io.println("------------------------------");
        io.println("공통 명령어: skip(넘기기), retry(다시하기), exit(종료)");
    }

    protected StageResult checkCommonCommand(String input) {
        if (input == null) {
            return null;
        }

        return switch (input.trim().toLowerCase()) {
            case "skip" -> new StageResult(StageResultType.SKIP, "현재 스테이지를 넘깁니다.");
            case "retry" -> new StageResult(StageResultType.RETRY, "현재 스테이지를 다시 시작합니다.");
            case "exit" -> new StageResult(StageResultType.EXIT, "게임을 종료합니다.");
            default -> null;
        };
    }

    protected void pause() {
        io.println("계속하려면 엔터를 누르세요.");
        io.nextLine();
    }
}