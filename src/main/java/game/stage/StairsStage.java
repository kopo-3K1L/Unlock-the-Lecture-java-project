package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.Random;

public class StairsStage extends AbstractStage {

    private static final int FLOORS = 5;
    private static final int DIR_UP = 0;
    private static final int DIR_DOWN = 1;
    private static final int DIR_LEFT = 2;
    private static final int DIR_RIGHT = 3;

    private final Random random = new Random();

    public StairsStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "계단 오르기 게임";
    }

    /**
     * <h2>
     * 계단 오르기 게임
     * </h2>
     *
     * <p>
     * 각 층마다 무작위로 제시되는 화살표 방향을 보고<br>
     * 알맞은 W / A / S / D 키를 입력하는 반응 게임입니다.
     *
     * <p>
     * 5층을 모두 통과하면 게임 클리어이며,<br>
     * 한 번이라도 틀리면 즉시 실패
     *
     * <p>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        printStageHeader(12, "               🪜 계단 오르기 게임 🪜");
        io.println("화살표 방향을 보고 W / A / S / D 를 빠르게 입력하세요!");
        io.println("5층을 모두 오르면 수업한다!");
        printCommandGuide();

        printLoading("계단 준비 중", 600, 3);

        for (int floor = 0; floor < FLOORS; floor++) {
            int direction = random.nextInt(4);

            io.println("\n" + (floor + 1) + "층");
            io.println(toArrow(direction));

            String input = io.nextLine().toLowerCase();

            StageResult cmd = checkCommonCommand(input);
            if (cmd != null) {
                return cmd;
            }

            if (isCorrect(direction, input)) {
                io.println("성공!");
            } else {
                io.println("으아아악 떨어짐");
                return new StageResult(StageResultType.FAIL, "계단 오르기 실패!");
            }
        }

        io.println("\n🪜 정상 도달! [수업한다]");
        io.println("🎉 모든 스테이지(강의)를 클리어했습니다!");
        delay(800);
        return new StageResult(StageResultType.SUCCESS, "계단 오르기 클리어!");
    }

    /**
     * <h2>
     * 방향 상수를 화살표 문자열로 변환
     * </h2>
     *
     * @param dir 방향 상수
     * @return 화면에 출력할 화살표 문자열
     */
    private String toArrow(int dir) {
        return switch (dir) {
            case DIR_UP -> "↑";
            case DIR_DOWN -> "↓";
            case DIR_LEFT -> "←";
            case DIR_RIGHT -> "→";
            default -> "";
        };
    }

    /**
     * <h2>
     * 입력한 키가 현재 방향과 일치하는지 판정
     * </h2>
     *
     * <p>
     * 방향 상수와 사용자가 입력한 문자를 비교하여<br>
     * 올바른 방향키 입력인지 확인합니다.
     *
     * @param dir 현재 제시된 방향
     * @param input 사용자 입력 문자열
     * @return 정답 여부
     */
    private boolean isCorrect(int dir, String input) {
        return (dir == DIR_UP && input.equals("w")) ||
                (dir == DIR_DOWN && input.equals("s")) ||
                (dir == DIR_LEFT && input.equals("a")) ||
                (dir == DIR_RIGHT && input.equals("d"));
    }
}