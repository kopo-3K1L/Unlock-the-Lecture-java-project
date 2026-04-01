package game.stage;

import game.io.ConsoleIO;

import java.util.Random;

public class DragStage extends AbstractStage {
    private final int ROWS = 5;
    private final int COLS = 5;
    private final int TOTAL_SLOTS = ROWS * COLS;
    private final int RED_BTN_COUNT = 50;

    private int[] stackCounts = new int[TOTAL_SLOTS];
    private int targetIndex;

    private final String RESET = "\u001b[0m";
    private final String RED_BG = "\u001b[41m";
    private final String GREEN_BG = "\u001b[42m";
    private final String WHITE_TXT = "\u001b[37m";
    private final String BLACK_TXT = "\u001b[30m";

    public DragStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "버튼 발굴 현장";
    }

    @Override
    public StageResult play() {
        initBoard();

        System.out.println("    ____  ____  __  ___   ______     ______");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\   / ____/");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /  /___ \\  ");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /  ____/ /  ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/  /_____/   ");
        System.out.println("                                           ");
        System.out.println("=========================================================");
        System.out.println("                   ⛏️ 버튼 발굴 현장 ⛏️                   ");
        System.out.println("=========================================================");
        System.out.println("25개의 구역에 75개의 🔴[수업 안 함] 버튼이 쌓여있습니다.");
        System.out.println("단 한 곳의 맨 밑바닥에 🟢[수업한다] 버튼이 깔려있습니다.");
        System.out.println("구역 번호를 입력해 겹겹이 쌓인 붉은 버튼을 치워보세요!\n");

        System.out.print("발굴 장비 챙기는 중");
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(600);
                System.out.print(".");
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isCleared = false;
        int digCount = 0;

        while (!isCleared) {
            printBoard();

            System.out.print("\n▶ 파낼 구역의 번호를 입력하세요 (1~25): ");
            String input = io.nextLine();

            StageResult commandResult = checkCommonCommand(input);
            if (commandResult != null) {
                return commandResult;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ 에러: 숫자만 입력할 수 있습니다! 다시 입력해주세요.");
                continue;
            }

            if (choice < 1 || choice > TOTAL_SLOTS) {
                System.out.println("❌ 1부터 25 사이의 구역 번호를 입력해주세요.");
                continue;
            }

            int index = choice - 1;
            digCount++;

            System.out.print("⛏️ 땅을 파는 중");
            try {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(300);
                    System.out.print(".");
                }
                System.out.println();
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (stackCounts[index] > 0) {
                stackCounts[index]--;
                System.out.println("으랏차! [" + choice + "번] 구역의 🔴[수업 안 함] 버튼을 치웠습니다.");
                System.out.println("   (남은 붉은 버튼: " + stackCounts[index] + "개)");
            } else if (index == targetIndex) {
                System.out.println("심봤다! [" + choice + "번] 구역 맨 밑에서 🟢[수업한다] 버튼이 나왔습니다!");
                System.out.println("총 " + digCount + "번의 삽질 끝에 발굴에 성공하셨습니다.");
                isCleared = true;
            } else {
                System.out.println("앗... [" + choice + "번] 구역은 흙바닥뿐입니다. 아무것도 없습니다.");
            }
        }

        System.out.println("\n최종 발굴 현장 모습:");
        printBoard();
        System.out.println("🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);

        return new StageResult(StageResultType.SUCCESS, "버튼 발굴 게임 클리어!");
    }

    private void initBoard() {
        Random random = new Random();
        targetIndex = random.nextInt(TOTAL_SLOTS);
        stackCounts = new int[TOTAL_SLOTS];

        for (int i = 0; i < RED_BTN_COUNT; i++) {
            int randomSlot = random.nextInt(TOTAL_SLOTS);
            stackCounts[randomSlot]++;
        }
    }

    private void printBoard() {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            int slotNum = i + 1;

            if (stackCounts[i] == 0) {
                if (i == targetIndex) {
                    System.out.printf(GREEN_BG + BLACK_TXT + "[%02d:정답!]" + RESET + "  ", slotNum);
                } else {
                    System.out.printf("[%02d: 텅빔]  ", slotNum);
                }
            } else {
                System.out.printf(RED_BG + WHITE_TXT + "[%02d: %2d층]" + RESET + "  ", slotNum, stackCounts[i]);
            }

            if (slotNum % COLS == 0) {
                System.out.println("\n");
            }
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