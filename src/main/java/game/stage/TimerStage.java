package game.stage;

import game.io.ConsoleIO;

public class TimerStage extends AbstractStage {

    private static final double TARGET_TIME = 5.00;
    private static final double SUCCESS_RANGE = 0.30;

    public TimerStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "5초를 맞춰라";
    }

    @Override
    public StageResult play() {
        showIntro();

        System.out.println("    ____  ____  __  ___   ______     ______");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\   / ____/");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /  /___ \\  ");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /  ____/ /  ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/  /_____/   ");
        System.out.println("                                           ");
        System.out.println("=========================================================");
        System.out.println("                   ⏱️  5초를 맞춰라 ⏱️                   ");
        System.out.println("=========================================================");
        System.out.println("엔터를 눌러 타이머를 시작하세요.");
        System.out.println("정확히 5초라고 생각되면 엔터를 다시 눌러 멈추세요.");
        System.out.println("성공 기준: 5.00초 ± 0.30초");
        System.out.println("공통 명령어는 시작 전 입력 가능: skip / retry / exit");
        System.out.println();

        String startInput = io.nextLine();
        StageResult commandResult = checkCommonCommand(startInput);
        if (commandResult != null) {
            return commandResult;
        }

        System.out.println("타이머 시작!");
        System.out.println("멈추려면 엔터를 누르세요.\n");

        final boolean[] stopped = {false};

        Thread inputThread = new Thread(() -> {
            io.nextLine();
            stopped[0] = true;
        });

        long startTime = System.currentTimeMillis();
        inputThread.start();

        while (!stopped[0]) {
            long now = System.currentTimeMillis();
            double elapsed = (now - startTime) / 1000.0;

            System.out.printf("\r현재 시간: %.2f초", elapsed);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new StageResult(StageResultType.FAIL, "타이머 실행 중 오류가 발생했습니다.");
            }
        }

        long endTime = System.currentTimeMillis();
        double result = (endTime - startTime) / 1000.0;
        double diff = Math.abs(TARGET_TIME - result);

        System.out.println();
        System.out.println("=========================================================");
        System.out.printf("멈춘 시간: %.2f초%n", result);
        System.out.printf("목표와의 오차: %.2f초%n", diff);

        if (diff <= SUCCESS_RANGE) {
            System.out.println("🎉 성공! 5초를 거의 완벽하게 맞췄습니다!");
            System.out.println("🎉 다음 스테이지(강의)가 열렸습니다!");
            delay(800);
            return new StageResult(StageResultType.SUCCESS, "타이머 게임 클리어!");
        } else {
            System.out.println("❌ 실패! 5초와 너무 차이 납니다.");
            System.out.println("다시 도전하세요.");
            return new StageResult(StageResultType.FAIL, "타이머 게임 실패!");
        }
    }

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}