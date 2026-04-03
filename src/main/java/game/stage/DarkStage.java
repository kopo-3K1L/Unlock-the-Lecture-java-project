package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static game.stage.ui.AnsiColor.*;

public class DarkStage extends AbstractStage {
    private static final int TOTAL_COUNT = 30;

    private String[] board = new String[TOTAL_COUNT];
    private boolean[] isChecked = new boolean[TOTAL_COUNT];

    private final Random random = new Random();

    public DarkStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "암흑 속 진짜 글자 찾기";
    }

    /**
     * <h2>
     * 암흑 속 진짜 글자 찾기
     * </h2>
     *
     * <p>
     * 어두운 화면에 가려진 30개의 위치 중 하나를 선택하여<br>
     * 진짜 [수업한다] 글자를 찾는 게임입니다.
     *
     * <p>
     * 각 위치에는 진짜 글자 1개와 여러 개의 가짜 글자가 섞여 있으며,<br>
     * 사용자는 번호를 선택해 하나씩 확인하면서 정답을 찾아야 합니다.
     *
     * <p>
     * 진짜 글자를 찾으면 게임 클리어 <br>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        initBoard();

        printStageHeader(10, "                🔦 암흑 속 진짜 글자 찾기 🔦");
        delay(600);

        io.println("어두운 화면에 30개의 글자가 엎어져 있습니다.");
        io.println("오직 1개만이 진짜 [수업한다] 입니다.");
        io.println("원하는 번호를 골라 손전등으로 비춰 글자를 확인하세요!\n");

        printLoading("어둠 속으로 들어가는 중", 200, 3);

        boolean isCleared = false;
        int tryCount = 0;

        while (!isCleared) {
            printBoard();
            delay(400);

            printCommandGuide();
            delay(200);

            io.print("\n▶ 몇 번째 위치를 손전등으로 비추시겠습니까? (1~30): ");
            String input = io.nextLine();

            StageResult commandResult = checkCommonCommand(input);
            if (commandResult != null) {
                return commandResult;
            }

            Integer choice = tryParseInt(input);
            if (choice == null) {
                io.println("❌ 숫자만 입력하세요.");
                continue;
            }

            if (choice < 1 || choice > TOTAL_COUNT) {
                io.println("❌ 1부터 30 사이의 숫자를 입력해주세요.");
                continue;
            }

            int index = choice - 1;

            if (isChecked[index]) {
                io.println("⚠️ 이미 확인한 곳입니다.");
                continue;
            }

            isChecked[index] = true;
            tryCount++;

            String foundText = board[index];

            io.println("\n🔦 " + choice + "번 위치를 비췄습니다...\n");
            delay(200);

            if ("수업한다".equals(foundText)) {
                io.println("🎉 정답! [" + foundText + "] 발견!");
                io.println("총 " + tryCount + "번 만에 성공!\n");
                isCleared = true;
            } else {
                io.println("❌ [" + foundText + "] → 가짜입니다!\n");
            }
        }

        io.println("최종 탐색 결과:");
        printBoard();

        io.println("\n🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);

        return new StageResult(StageResultType.SUCCESS, "암흑 속 글자 찾기 클리어!");
    }

    /**
     * <h2>
     * 게임 보드에 진짜 글자와 가짜 글자를 배치
     * </h2>
     *
     * <p>
     * 30개의 위치 중 하나에는 진짜 [수업한다]를 넣고,<br>
     * 나머지 위치에는 여러 종류의 가짜 글자를 무작위로 채운 뒤 섞어서 배치합니다.
     *
     * <p>
     * 각 위치의 확인 여부도 함께 초기화합니다.
     */
    private void initBoard() {
        String[] fakes = {
                "수언한다", "수업할까", "수엄하기", "수업하게",
                "수업하쟈", "수업함다", "수업안함", "수업휴강"
        };

        ArrayList<String> list = new ArrayList<>();
        list.add("수업한다");

        for (int i = 1; i < TOTAL_COUNT; i++) {
            list.add(fakes[random.nextInt(fakes.length)]);
        }

        Collections.shuffle(list);

        for (int i = 0; i < TOTAL_COUNT; i++) {
            board[i] = list.get(i);
            isChecked[i] = false;
        }
    }

    /**
     * <h2>
     * 현재 탐색 보드 상태를 출력
     * </h2>
     *
     * <p>
     * 아직 확인하지 않은 위치는 번호만 보이도록 출력하고,<br>
     * 이미 확인한 위치는 실제 글자를 보여줍니다.
     *
     * <p>
     * 정답 글자는 초록색, 가짜 글자는 빨간색으로 표시하여<br>
     * 탐색 진행 상황을 쉽게 구분할 수 있게 합니다.
     */
    private void printBoard() {
        for (int i = 0; i < TOTAL_COUNT; i++) {

            if (isChecked[i]) {
                if ("수업한다".equals(board[i])) {
                    System.out.print(GREEN_BG + BLACK_TEXT + "[" + board[i] + "]" + RESET + "\t");
                } else {
                    System.out.print(RED_TEXT + "[" + board[i] + "]" + RESET + "\t");
                }
            } else {
                System.out.printf(BLACK_BG + WHITE_TEXT + "[  %02d  ]" + RESET + "\t", i + 1);
            }

            if ((i + 1) % 6 == 0) {
                System.out.println();
            }
        }
    }
}