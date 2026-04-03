package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.Random;

import static game.stage.ui.AnsiColor.BLACK_TEXT;
import static game.stage.ui.AnsiColor.GREEN_BG;
import static game.stage.ui.AnsiColor.RED_BG;
import static game.stage.ui.AnsiColor.RESET;
import static game.stage.ui.AnsiColor.WHITE_TEXT;

public class DragStage extends AbstractStage {
    private static final int ROWS = 4;
    private static final int COLS = 4;
    private static final int TOTAL_SLOTS = ROWS * COLS;
    private static final int RED_BTN_COUNT = 30;

    private int[] stackCounts = new int[TOTAL_SLOTS];
    private int targetIndex;

    public DragStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "버튼 발굴 현장";
    }

    /**
     * <h2>버튼 발굴 현장</h2>
     *
     * <p>
     * 16개의 구역 중 단 한 곳의 맨 아래에 정답 버튼이 숨어 있습니다.<br>
     * 사용자는 구역 번호를 입력해 빨간 버튼을 하나씩 제거하며 정답을 찾아야 합니다.
     *
     * <p>
     * 빨간 버튼이 남아 있는 구역은 계속 파낼 수 있고,<br>
     * 정답 구역의 마지막 버튼까지 제거하면 게임 클리어 <br>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        initBoard();

        printStageHeader(5, "                   ⛏️ 버튼 발굴 현장 ⛏️");
        printGuide(
                "16개의 구역에 30개의 🔴[수업 안 함] 버튼이 쌓여있습니다.",
                "단 한 곳의 맨 밑바닥에 🟢[수업한다] 버튼이 깔려있습니다.",
                "구역 번호를 입력해 겹겹이 쌓인 붉은 버튼을 치워보세요!\n"
        );

        printLoading("발굴 장비 챙기는 중", 400, 3);

        boolean isCleared = false;
        int digCount = 0;

        while (!isCleared) {
            printBoard();
            delay(400);

            printCommandGuide();
            delay(200);
            io.print("\n▶ 파낼 구역의 번호를 입력하세요 (1~16): ");
            String input = io.nextLine();

            StageResult commandResult = checkCommonCommand(input);
            if (commandResult != null) {
                return commandResult;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                io.println("❌ 에러: 숫자만 입력할 수 있습니다! 다시 입력해주세요.");
                continue;
            }

            if (choice < 1 || choice > TOTAL_SLOTS) {
                io.println("❌ 1부터 16 사이의 구역 번호를 입력해주세요.");
                continue;
            }

            int index = choice - 1;
            digCount++;

            printLoading("⛏️ 땅을 파는 중", 200, 3);

            if (stackCounts[index] > 0) {
                stackCounts[index]--;
                io.println("으랏차! [" + choice + "번] 구역의 🔴[수업 안 함] 버튼을 치웠습니다.\n");
                delay(200);

                // 현재 구역이 정답 위치이고,
                // 마지막 빨간 버튼까지 모두 제거된 경우 클리어 처리
                if (stackCounts[index] == 0 && index == targetIndex) {
                    io.println("심봤다! [" + choice + "번] 구역 맨 밑에서 🔵[수업한다] 버튼이 나왔습니다!");
                    io.println("총 " + digCount + "번의 삽질 끝에 발굴에 성공하셨습니다.\n");
                    delay(200);
                    isCleared = true;
                }
            } else {
                io.println("앗... [" + choice + "번] 구역은 흙바닥뿐입니다. 아무것도 없습니다.\n");
                delay(200);
            }
        }

        io.println("\n최종 발굴 현장 모습:");
        printBoard();
        io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);

        return new StageResult(StageResultType.SUCCESS, "버튼 발굴 게임 클리어!");
    }

    /**
     * <h2>
     * 게임 보드를 초기화하는 메서드
     * </h2>
     *
     * <p>
     * 16개의 구역 중 하나를 정답 위치로 무작위 선택하고,<br>
     * 총 30개의 빨간 버튼을 각 구역에 랜덤하게 분배합니다.
     *
     * <p>
     * 정답 구역은 반드시 최소 1개의 버튼을 가지도록 설정하여<br>
     * 사용자가 마지막까지 파내야 정답이 드러나게 만듭니다.
     */
    private void initBoard() {
        Random random = new Random();
        targetIndex = random.nextInt(TOTAL_SLOTS);
        stackCounts = new int[TOTAL_SLOTS];

        int minTargetStack = 1;
        stackCounts[targetIndex] = minTargetStack;

        int remainingRedBtns = RED_BTN_COUNT - minTargetStack;

        for (int i = 0; i < remainingRedBtns; i++) {
            int randomSlot = random.nextInt(TOTAL_SLOTS);
            stackCounts[randomSlot]++;
        }
    }

    /**
     * <h2>
     * 현재 발굴 보드 상태를 출력하는 메서드
     * </h2>
     *
     * <p>
     * 버튼이 남아 있는 구역은 빨간색과 층수로 표시하고,<br>
     * 비어 있는 구역은 텅 빈 상태로 출력합니다.
     *
     * <p>
     * 정답 구역의 버튼이 모두 제거된 경우에는<br>
     * 초록색으로 정답 위치를 보여줍니다.
     */
    private void printBoard() {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            int slotNum = i + 1;

            if (stackCounts[i] == 0) {
                if (i == targetIndex) {
                    System.out.printf(GREEN_BG + BLACK_TEXT + "[%02d:정답!]" + RESET + "  ", slotNum);
                } else {
                    System.out.printf("[%02d: 텅빔]  ", slotNum);
                }
            } else {
                System.out.printf(RED_BG + WHITE_TEXT + "[%02d: %2d층]" + RESET + "  ", slotNum, stackCounts[i]);
            }

            if (slotNum % COLS == 0) {
                System.out.println("\n");
            }
        }
    }
}