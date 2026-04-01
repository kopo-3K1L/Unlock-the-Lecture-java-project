package game.stage;

import game.io.ConsoleIO;

public class RGBStage extends AbstractStage {
    private final int targetR = 34;
    private final int targetG = 197;
    private final int targetB = 94;
    private final int THRESHOLD = 50;

    public RGBStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "색감 천재 모십니다";
    }

    @Override
    public StageResult play() {
        System.out.println("    ____  ____  __  ___   ______     __ __");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\   / // /");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /  / // /_");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /  /__  __/");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/     /_/   ");
        System.out.println("                                          ");
        System.out.println("=========================================================");
        System.out.println("                  🎨 색감 천재 모십니다 🎨                  ");
        System.out.println("=========================================================");
        System.out.println("목표: 아래의 [정답 색상]과 최대한 비슷한 색을 조합하세요!");
        System.out.println("오차가 50 이하가 되면 다음 강의가 열립니다.\n");

        System.out.print("팔레트 준비 중");
        delayAction(600, 3);
        System.out.println("\n");

        boolean isCleared = false;

        while (!isCleared) {
            printColorBlock("🎯 정답 색상:", targetR, targetG, targetB, false);

            System.out.println("\n▶ R, G, B 값을 차례대로 입력하세요 (0~255)");
            System.out.println("공통 명령어를 입력하려면 R 값에 skip / retry / exit 입력");

            io.print("R: ");
            String firstInput = io.nextLine();
            StageResult commandResult = checkCommonCommand(firstInput);
            if (commandResult != null) {
                return commandResult;
            }

            int userR;
            try {
                userR = clamp(Integer.parseInt(firstInput));
            } catch (NumberFormatException e) {
                System.out.println("❌ 에러: 숫자만 입력해주세요!");
                continue;
            }

            int userG = clamp(io.nextInt("G: "));
            int userB = clamp(io.nextInt("B: "));

            System.out.print("\n색상 조합 및 오차 계산 중");
            delayAction(400, 3);
            System.out.println("\n");

            printColorBlock("나의 색상:", userR, userG, userB, true);

            int diff = Math.abs(targetR - userR) + Math.abs(targetG - userG) + Math.abs(targetB - userB);

            if (diff <= THRESHOLD) {
                System.out.println("\n✅ [ 수업한다 ] - 오차 범위 만족! (현재 오차: " + diff + " / 기준: 50)");
                isCleared = true;
            } else {
                System.out.println("\n❌ [ 수업 안 함 ] - 오차 범위 초과! (현재 오차: " + diff + " / 기준: 50)");
                System.out.println("색상이 너무 다릅니다. 다시 맞춰보세요!");
            }
        }

        System.out.println("🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);

        return new StageResult(StageResultType.SUCCESS, "RGB 게임 클리어!");
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private void printColorBlock(String label, int r, int g, int b, boolean showNumbers) {
        String colorCode = String.format("\u001b[48;2;%d;%d;%dm", r, g, b);
        String resetCode = "\u001b[0m";

        System.out.print(label + " " + colorCode + "               " + resetCode);

        if (showNumbers) {
            System.out.println(" (R: " + r + ", G: " + g + ", B: " + b + ")");
        } else {
            System.out.println(" (R: ???, G: ???, B: ???)");
        }
    }

    private void delayAction(int millis, int dots) {
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

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}