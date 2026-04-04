package game.stage.core;

import game.io.ConsoleIO;
import game.stage.ui.StageAsciiPrinter;

/**
 * 모든 스테이지의 공통 기능을 제공하는 추상 클래스
 *
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>스테이지 제목 및 안내 출력</li>
 *     <li>공통 명령어(skip, retry, exit) 처리</li>
 *     <li>입력 대기(pause) 기능 제공</li>
 * </ul>
 *
 * <p>
 * 각 스테이지는 이 클래스를 상속받아 구현
 */
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

    /**
     * 공통 명령어를 처리하는 메서드
     *
     * <p>
     * 입력값이 skip, retry, exit 중 하나일 경우
     * 해당 결과를 반환한다.
     *
     * @param input 사용자 입력 문자열
     * @return 처리된 StageResult 또는 null
     */
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

    protected void delayAction(int millis, int dots) {
        try {
            for (int i = 0; i < dots; i++) {
                Thread.sleep(millis);
                System.out.print(".");
            }
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void printStageHeader(int round, String title) {
        StageAsciiPrinter.printRound(io, round);
        delay(600);
        io.println("=========================================================");
        io.println(title);
        io.println("=========================================================");
        delay(600);
    }

    protected void printGuide(String... lines) {
        for (String line : lines) {
            io.println(line);
        }
    }

    protected void printLoading(String message, int millis, int dots) {
        System.out.print(message);
        delayAction(millis, dots);
        System.out.println("\n");
    }

    protected void printCommandGuide() {
        io.println("공통 명령어: skip(넘기기), retry(다시하기), exit(종료)");
    }

    /**
     * 문자열을 안전하게 정수로 변환
     *
     * <p>
     * NumberFormatException을 방지하기 위해 예외를 발생시키지 않고 null을 반환
     *
     * <p>
     * 모든 스테이지에서 입력값 검증을 통일하기 위해 사용
     *
     * @param input 사용자 입력 문자열
     * @return 변환된 정수 또는 실패 시 null
     */
    protected Integer tryParseInt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}