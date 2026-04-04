package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;


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

    /**
     * <h2>색감 천재 모십니다</h2>
     *
     * <p>
     * 사용자는 R, G, B 값을 입력하여 목표 색상과 최대한 비슷한 색을 만듬<br>
     * 입력한 색상과 목표 색상의 차이를 계산하여,<br>
     * 오차가 기준값 이하이면 스테이지를 클리어
     *
     * <p>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {

        printStageHeader(4, "                  🎨 색감 천재 모십니다 🎨");

        io.println("목표: 아래의 [정답 색상]과 최대한 비슷한 색을 조합하세요!");
        io.println("오차가 50 이하가 되면 다음 강의가 열립니다.\n");

        printLoading("팔레트 준비 중", 200, 3);

        boolean isCleared = false;

        while (!isCleared) {

            printColorBlock("🎯 정답 색상:", targetR, targetG, targetB, false);

            delay(800);

            printCommandGuide();
            delay(200);
            io.println("\n▶ R, G, B 값을 차례대로 입력하세요 (0~255)");

            io.print("R: ");
            String rInput = io.nextLine();
            StageResult rCommand = checkCommonCommand(rInput);
            if (rCommand != null) {
                return rCommand;
            }

            Integer parsedR = tryParseInt(rInput);
            if (parsedR == null) {
                io.println("❌ R 값은 숫자만 입력할 수 있습니다! 다시 입력해주세요.");
                continue;
            }

            io.print("G: ");
            String gInput = io.nextLine();
            StageResult gCommand = checkCommonCommand(gInput);
            if (gCommand != null) {
                return gCommand;
            }

            Integer parsedG = tryParseInt(gInput);
            if (parsedG == null) {
                io.println("❌ G 값은 숫자만 입력할 수 있습니다! 다시 입력해주세요.");
                continue;
            }

            io.print("B: ");
            String bInput = io.nextLine();
            StageResult bCommand = checkCommonCommand(bInput);
            if (bCommand != null) {
                return bCommand;
            }

            Integer parsedB = tryParseInt(bInput);
            if (parsedB == null) {
                io.println("❌ B 값은 숫자만 입력할 수 있습니다! 다시 입력해주세요.");
                continue;
            }

            int userR = clamp(parsedR);
            int userG = clamp(parsedG);
            int userB = clamp(parsedB);

            printLoading("\n🎨 색상 조합 및 오차 계산 중", 400, 3);

            printColorBlock("나의 색상:", userR, userG, userB, true);

            // 목표 색상과 사용자 색상의 총 오차 계산
            int diff = Math.abs(targetR - userR)
                    + Math.abs(targetG - userG)
                    + Math.abs(targetB - userB);

            if (diff <= THRESHOLD) {
                io.println("\n✅ [ 수업한다 ] - 오차 범위 만족! (현재 오차: " + diff + " / 기준: 50)");
                isCleared = true;
            } else {
                io.println("\n❌ [ 수업 안 함 ] - 오차 범위 초과! (현재 오차: " + diff + " / 기준: 50)");
                io.println("색상이 너무 다릅니다. 다시 맞춰보세요!\n");
                delay(600);
            }
        }

        io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
        delay(800);

        return new StageResult(StageResultType.SUCCESS, "RGB 게임 클리어!");
    }

    /**
     * <h2>
     * RGB 값을 0 ~ 255 범위로 보정한다.
     * </h2>
     * @param value 사용자 입력값
     * @return 범위를 벗어나지 않도록 보정된 값
     */
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    /**
     * <h2>
     * ANSI 배경색을 사용하여 콘솔에 색상 블록을 출력
     * </h2>
     *
     * @param label 출력할 라벨
     * @param r red 값
     * @param g green 값
     * @param b blue 값
     * @param showNumbers RGB 수치 표시 여부
     */
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
}