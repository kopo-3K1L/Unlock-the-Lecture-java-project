package game.stage;

import game.io.ConsoleIO;
import java.util.Random;

public class StairsStage extends AbstractStage {

    private static final int FLOORS = 5;
    private static final int DIR_UP    = 0;
    private static final int DIR_DOWN  = 1;
    private static final int DIR_LEFT  = 2;
    private static final int DIR_RIGHT = 3;

    private final Random rand = new Random();

    public StairsStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "계단 오르기 게임";
    }

    @Override
    public StageResult play() {
        System.out.println("    ____  ____  __  ___   ______     _______");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\      ___/ ");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /     / /   ");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /     / /    ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/     /_/     ");
        System.out.println("                                             ");
        System.out.println("=========================================================");
        System.out.println("               🪜 계단 오르기 게임 🪜               ");
        System.out.println("=========================================================");
        System.out.println("화살표 방향을 보고 W / A / S / D 를 빠르게 입력하세요!");
        System.out.println("5층을 모두 오르면 수업한다!");
        System.out.println("공통 명령어: skip(넘기기), retry(다시하기), exit(종료)\n");

        System.out.print("계단 준비 중");
        for (int i = 0; i < 3; i++) {
            sleep(600);
            System.out.print(".");
        }
        System.out.println("\n");

        for (int i = 0; i < FLOORS; i++) {
            int dir = rand.nextInt(4);

            io.println("\n" + (i + 1) + "층");
            io.println(toArrow(dir));

            String input = io.nextLine().toLowerCase();

            StageResult cmd = checkCommonCommand(input);
            if (cmd != null) return cmd;

            if (isCorrect(dir, input)) {
                io.println("성공!");
            } else {
                io.println("으아아악 떨어짐");
                return new StageResult(StageResultType.FAIL, "계단 오르기 실패!");
            }
        }

        io.println("\n🪜 정상 도달! [수업한다]");
        io.println("🎉 모든 스테이지(강의)를 클리어했습니다!");
        return new StageResult(StageResultType.SUCCESS, "계단 오르기 클리어!");
    }

    private String toArrow(int dir) {
        return switch (dir) {
            case DIR_UP    -> "↑";
            case DIR_DOWN  -> "↓";
            case DIR_LEFT  -> "←";
            case DIR_RIGHT -> "→";
            default -> "";
        };
    }

    private boolean isCorrect(int dir, String input) {
        return (dir == DIR_UP    && input.equals("w")) ||
               (dir == DIR_DOWN  && input.equals("s")) ||
               (dir == DIR_LEFT  && input.equals("a")) ||
               (dir == DIR_RIGHT && input.equals("d"));
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
