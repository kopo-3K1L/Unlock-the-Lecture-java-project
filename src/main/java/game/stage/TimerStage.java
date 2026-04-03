package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

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

    /**
     * <h2>5초를 맞춰라</h2>
     *
     * <p>
     * 사용자는 엔터를 눌러 타이머를 시작한 뒤,<br>
     * 정확히 5초라고 생각되는 순간 다시 엔터를 눌러 멈춰야 합니다.
     *
     * <p>
     * 목표 시간과의 오차가 일정 범위 이내이면 성공 <br>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        printStageHeader(3, "                   ⏱️  5초를 맞춰라 ⏱️");
        printGuide(
                "엔터를 눌러 타이머를 시작하세요.",
                "정확히 5초라고 생각되면 엔터를 다시 눌러 멈추세요.",
                "성공 기준: 5.00초 ± 0.30초",
                "공통 명령어는 시작 전 입력 가능: skip / retry / exit"
        );

        String startInput = io.nextLine();
        StageResult commandResult = checkCommonCommand(startInput);
        if (commandResult != null) {
            return commandResult;
        }

        io.println("타이머 시작!");
        io.println("멈추려면 엔터를 누르세요.\n");

        final boolean[] stopped = {false};

        // 엔터 입력을 별도 스레드에서 받아 타이머 정지 여부를 확인
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

        io.println("=========================================================");
        System.out.printf("멈춘 시간: %.2f초%n", result);
        System.out.printf("목표와의 오차: %.2f초%n", diff);

        if (diff <= SUCCESS_RANGE) {
            io.println("🎉 성공! 5초를 거의 완벽하게 맞췄습니다!");
            io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
            delay(800);
            return new StageResult(StageResultType.SUCCESS, "타이머 게임 클리어!");
        } else {
            io.println("❌ 실패! 5초와 너무 차이 납니다.");
            io.println("다시 도전하세요.");
            return new StageResult(StageResultType.FAIL, "타이머 게임 실패!");
        }
    }
}