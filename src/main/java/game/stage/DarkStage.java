package game.stage;

import game.io.ConsoleIO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DarkStage extends AbstractStage {
    private final int TOTAL_COUNT = 42;
    private String[] board = new String[TOTAL_COUNT];
    private boolean[] isChecked = new boolean[TOTAL_COUNT];

    private final String RESET = "\u001b[0m";
    private final String DARK_BG = "\u001b[40m";
    private final String WHITE_TXT = "\u001b[37m";
    private final String RED_TXT = "\u001b[31m";
    private final String GREEN_BG = "\u001b[42m";
    private final String BLACK_TXT = "\u001b[30m";

    public DarkStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "암흑 속 진짜 글자 찾기";
    }

    @Override
    public StageResult play() {
        initBoard();

        System.out.println("    ____  ____  __  ___   ______     _______ ");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\   <  / __ \\");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /   / / / / /");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /   / / /_/ / ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/   /_/\\____/  ");
        System.out.println("                                             ");
        System.out.println("=========================================================");
        System.out.println("                🔦 암흑 속 진짜 글자 찾기 🔦                ");
        System.out.println("=========================================================");
        System.out.println("어두운 화면에 42개의 글자가 엎어져 있습니다.");
        System.out.println("오직 1개만이 진짜 [수업한다] 입니다.");
        System.out.println("원하는 번호를 골라 손전등으로 비춰 글자를 확인하세요!\n");

        System.out.print("어둠 속으로 들어가는 중");
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(800);
                System.out.print(".");
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isCleared = false;
        int tryCount = 0;

        while (!isCleared) {
            printBoard();

            System.out.print("\n▶ 몇 번째 위치를 손전등으로 비추시겠습니까? (1~42): ");
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

            if (choice < 1 || choice > 42) {
                System.out.println("❌ 1부터 42 사이의 숫자를 입력해주세요.");
                continue;
            }

            int index = choice - 1;

            if (isChecked[index]) {
                System.out.println("⚠️ 이미 확인한 곳입니다. 다른 곳을 비춰보세요!");
                continue;
            }

            isChecked[index] = true;
            tryCount++;
            String foundText = board[index];

            System.out.println("\n🔦 " + choice + "번 위치를 비췄습니다...");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (foundText.equals("수업한다")) {
                System.out.println("🎉 정답입니다! 진짜 [" + foundText + "] 를 찾았습니다!");
                System.out.println("총 " + tryCount + "번 만에 성공하셨습니다.\n");
                isCleared = true;
            } else {
                System.out.println("❌ 앗! [" + foundText + "] (이)라고 적혀있네요. 가짜입니다!");
            }
        }

        System.out.println("최종 탐색 결과:");
        printBoard();

        System.out.println("\n🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);

        return new StageResult(StageResultType.SUCCESS, "암흑 속 글자 찾기 클리어!");
    }

    private void initBoard() {
        String[] fakes = {
                "수언한다", "수업할까", "수엄하기", "수업하게",
                "수업하쟈", "수업함다", "수업안함", "수업휴강"
        };

        board = new String[TOTAL_COUNT];
        isChecked = new boolean[TOTAL_COUNT];

        ArrayList<String> list = new ArrayList<>();
        list.add("수업한다");

        Random random = new Random();
        for (int i = 1; i < TOTAL_COUNT; i++) {
            list.add(fakes[random.nextInt(fakes.length)]);
        }

        Collections.shuffle(list);

        for (int i = 0; i < TOTAL_COUNT; i++) {
            board[i] = list.get(i);
        }
    }

    private void printBoard() {
        for (int i = 0; i < TOTAL_COUNT; i++) {
            if (isChecked[i]) {
                if (board[i].equals("수업한다")) {
                    System.out.print(GREEN_BG + BLACK_TXT + "[" + board[i] + "]" + RESET + "\t");
                } else {
                    System.out.print(RED_TXT + "[" + board[i] + "]" + RESET + "\t");
                }
            } else {
                System.out.printf(
                            DARK_BG + WHITE_TXT + "[  %02d  ]" + RESET + "\t", (i + 1));
            }

            if ((i + 1) % 6 == 0) {
                System.out.println();
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