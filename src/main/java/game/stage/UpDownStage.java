package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

public class UpDownStage extends AbstractStage {

    private static final int MIN = 0;
    private static final int MAX = 9;
    private static final int MAX_TURNS = 4;

    public UpDownStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "업다운 게임";
    }

    /**
     * <h2>
     * 업다운 게임
     * </h2>
     * <p>
     * 사용자는 제한된 횟수 안에 정답 숫자를 맞춰야 하며,<br>
     * 입력값에 따라 범위를 점점 좁혀가면서 정답을 추리하는 게임입니다.
     * 공통 명령어(skip, retry, exit) 가능
     *
     */
    @Override
    public StageResult play() {
        printStageHeader(2, "                   ⬆️ 업다운 게임 ⬇️");
        printGuide(
                "0~9 사이의 숫자를 4번 안에 맞춰보세요!",
                "힌트를 보고 범위를 좁혀나가면 수업한다!"
        );
        printCommandGuide();

        printLoading("숫자 뽑는 중", 600, 3);

        int left = MIN;
        int right = MAX;

        // 0 ~ 9 사이의 정답 숫자 생성
        int answer = (int) (Math.random() * (MAX + 1));
        int turn = MAX_TURNS;

        while (turn > 0) {
            io.print("\n▶ 0~9 사이의 숫자 선택 [현재 범위: " + left + "~" + right + "]: ");
            String input = io.nextLine();

            StageResult cmd = checkCommonCommand(input);
            if (cmd != null) {
                return cmd;
            }

            Integer guess = tryParseInt(input);
            if (guess == null) {
                io.println("숫자를 입력하세요.");
                continue;
            }

            if (guess < left || guess > right) {
                io.println("범위 안에서 입력하세요!");
                continue;
            }

            if (guess == answer) {
                io.println("🎯 정답! [수업한다] 를 맞췄습니다!");
                io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
                delay(800);
                return new StageResult(StageResultType.SUCCESS, "업다운 게임 클리어!");
            }

            if (guess < answer) {
                left = guess + 1;
                io.println("Up");
            } else {
                right = guess - 1;
                io.println("Down");
            }

            turn--;
            io.println("남은 기회: " + turn + "번");
        }

        io.println("땡! 정답: " + answer);
        return new StageResult(StageResultType.FAIL, "업다운 게임 실패!");
    }
}