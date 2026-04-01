package game.stage;

import game.io.ConsoleIO;

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

    @Override
    public StageResult play() {
        System.out.println("    ____  ____  __  ___   ______     _____");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\  / ___/");
        System.out.println("  / /_/ / / / / / / /  |/ / / / / / /__  ");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /  \\__, / ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/  /____/  ");
        System.out.println("                                          ");
        System.out.println("=========================================================");
        System.out.println("                   ⬆️ 업다운 게임 ⬇️                   ");
        System.out.println("=========================================================");
        System.out.println("0~9 사이의 숫자를 4번 안에 맞춰보세요!");
        System.out.println("힌트를 보고 범위를 좁혀나가면 수업한다!");
        System.out.println("공통 명령어: skip(넘기기), retry(다시하기), exit(종료)\n");

        System.out.print("숫자 뽑는 중");
        for (int i = 0; i < 3; i++) {
            sleep(600);
            System.out.print(".");
        }
        System.out.println("\n");

        int left = MIN;
        int right = MAX;
        int answer = (int) (Math.random() * (MAX + 1));
        int turn = MAX_TURNS;

        while (turn > 0) {
            io.print("\n▶ 0~9 사이의 숫자 선택 [현재 범위: " + left + "~" + right + "]: ");
            String input = io.nextLine();

            StageResult cmd = checkCommonCommand(input);
            if (cmd != null) return cmd;

            int guess;
            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
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

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
