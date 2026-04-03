package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.Random;

public class MazeStage extends AbstractStage {

    private static final int PATH_LENGTH = 5;
    private static final int CLEAR_LINES = 20;
    private static final int LEFT = 1;
    private static final int CENTER = 2;
    private static final int RIGHT = 3;

    private final Random random = new Random();

    public MazeStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "미로 탈출 게임";
    }

    /**
     * <h2>
     * 미로 탈출 게임
     * </h2>
     *
     * <p>
     * 무작위로 생성된 5단계 경로를 잠시 보여준 뒤,<br>
     * 사용자가 이를 기억하여 순서대로 입력하는 게임입니다.
     *
     * <p>
     * 각 단계에서 제시된 방향과 동일한 선택을 해야 하며,<br>
     * 5단계를 모두 통과하면 게임 클리어
     *
     * <p>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        printStageHeader(9, "                🧩 미로 탈출 게임 🧩");
        io.println("제시되는 경로를 기억하고 순서대로 입력하세요!");
        io.println("5단계를 모두 통과하면 수업한다!");
        printCommandGuide();

        printLoading("미로 생성 중", 600, 3);

        int[] path = generatePath();

        io.println("제시되는 경로를 기억하세요!");
        delay(3000);

        showPath(path);

        delay(500);
        clearGuideLines();

        io.println("--- 이제 입력하세요 ---");

        for (int stage = 0; stage < PATH_LENGTH; stage++) {
            io.println("\n단계 " + (stage + 1));
            io.println("1. 왼쪽   2. 가운데   3. 오른쪽");

            StageResult result = readAnswer(path[stage]);
            if (result != null) {
                return result;
            }
        }

        io.println("\n🧩 탈출 성공! [수업한다] 경로를 찾았습니다!");
        io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);
        return new StageResult(StageResultType.SUCCESS, "미로 탈출 클리어!");
    }

    /**
     * <h2>
     * 정답 경로를 무작위로 생성
     * </h2>
     *
     * <p>
     * 왼쪽, 가운데, 오른쪽 중 하나를 5번 무작위로 선택하여<br>
     * 사용자가 외워야 할 경로 배열을 생성합니다.
     *
     * @return 생성된 정답 경로 배열
     */
    private int[] generatePath() {
        int[] path = new int[PATH_LENGTH];
        for (int i = 0; i < PATH_LENGTH; i++) {
            path[i] = random.nextInt(3) + 1;
        }
        return path;
    }

    /**
     * <h2>
     * 생성된 경로를 화면에 출력
     * </h2>
     *
     * <p>
     * 경로 배열에 들어 있는 방향 값을 문자열로 변환하여<br>
     * 일정 시간 간격을 두고 순서대로 화면에 보여줍니다.
     *
     * @param path 출력할 경로 배열
     */
    private void showPath(int[] path) {
        for (int direction : path) {
            io.print(toDirection(direction) + " ");
            delay(800);
        }
    }

    /**
     * <h2>
     * 안내 문구를 화면에서 밀어내기 위해 빈 줄을 출력
     * </h2>
     *
     * <p>
     * 사용자가 이전에 보여준 경로를 바로 보지 못하도록<br>
     * 여러 줄의 공백을 출력하여 화면을 정리합니다.
     */
    private void clearGuideLines() {
        for (int i = 0; i < CLEAR_LINES; i++) {
            io.println("");
        }
    }

    /**
     * <h2>
     * 현재 단계의 사용자 입력을 판정
     * </h2>
     *
     * <p>
     * 사용자로부터 1~3 사이의 숫자를 입력받아 정답 방향과 비교하고,<br>
     * 맞으면 다음 단계로 진행하며 틀리면 즉시 실패 처리합니다.
     *
     * <p>
     * 공통 명령어(skip, retry, exit) 입력도 함께 처리합니다.
     *
     * @param correct 현재 단계의 정답 방향
     * @return 실패 또는 공통 명령어 처리 결과, 정답이면 null
     */
    private StageResult readAnswer(int correct) {
        while (true) {
            io.print("선택: ");
            String raw = io.nextLine();

            StageResult cmd = checkCommonCommand(raw);
            if (cmd != null) {
                return cmd;
            }

            Integer input = tryParseInt(raw);
            if (input == null) {
                io.println("숫자를 입력하세요.");
                continue;
            }

            if (input < LEFT || input > RIGHT) {
                io.println("1 ~ 3 사이의 숫자를 입력하세요.");
                continue;
            }

            if (input == correct) {
                io.println("통과!");
                return null;
            }

            io.println("으아아악 게임 오버");
            return new StageResult(StageResultType.FAIL, "미로 탈출 실패!");
        }
    }

    /**
     * <h2>
     * 방향 번호를 방향 문자열로 변환
     * </h2>
     *
     * @param n 방향 번호
     * @return 변환된 방향 문자열
     */
    private String toDirection(int n) {
        return switch (n) {
            case LEFT -> "왼쪽";
            case CENTER -> "가운데";
            case RIGHT -> "오른쪽";
            default -> "";
        };
    }
}