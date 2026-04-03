package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.Random;

public class LineStage extends AbstractStage {
    private static final int MAX_HEIGHT = 20;
    private static final int COLUMN_POS = 9;
    private static final int ROW_OFFSET = 12;
    private static final int TARGET_MIN = 2;
    private static final int TARGET_MAX = 16;
    private static final int FRAME_WIDTH = 26;
    private static final int BLOCK_DELAY = 80;

    private final Random random = new Random();

    private volatile boolean running;
    private int blockPos = MAX_HEIGHT;
    private int targetLine;

    public LineStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "선 넘지 마세요 제발";
    }

    /**
     * <h2>
     * 선 넘지 마세요 제발
     * </h2>
     *
     * <p>
     * 위에서 아래로 이동하는 블럭이 골든 라인에 도달하는 순간<br>
     * 사용자가 엔터를 눌러 타이밍을 맞추는 게임입니다.
     *
     * <p>
     * 블럭 위치와 목표 라인의 차이가 1칸 이하이면 성공으로 처리하며,<br>
     * 그보다 많이 차이나면 실패
     *
     * <p>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        running = true;
        blockPos = MAX_HEIGHT;

        clearConsole();

        printStageHeader(7, "                🚨 선 넘지 마세요 제발! 🚨");
        io.println("움직이는 블럭이 골든 라인에 닿는 순간 [Enter]를 누르세요!");
        io.println("(명령어 입력: skip / retry / exit)");

        targetLine = random.nextInt(TARGET_MAX - TARGET_MIN + 1) + TARGET_MIN;

        drawStaticFrame();
        startDisplayThread();

        String input = io.nextLine();
        running = false;

        StageResult commandResult = checkCommonCommand(input);
        if (commandResult != null) {
            return commandResult;
        }

        moveCursor(ROW_OFFSET + MAX_HEIGHT + 3, 1);

        if (isSuccess(blockPos)) {
            io.println("\n🎉 축하합니다! 다음 강의(스테이지)가 열렸습니다!");
            delay(1200);
            return new StageResult(StageResultType.SUCCESS, "선 넘지 마세요 제발 클리어!");
        }

        io.println("\n💀 다시 도전하세요.");
        delay(1200);
        return new StageResult(StageResultType.FAIL, "타이밍 맞추기 실패");
    }

    /**
     * <h2>
     * 콘솔 화면을 초기화
     * </h2>
     *
     * <p>
     * ANSI 이스케이프 코드를 사용하여 콘솔 화면을 지우고<br>
     * 커서를 좌측 상단으로 이동시킵니다. <br>
     * 이거 콘솔은 먹는데 제 기억상으로 인텔리제이 실행창에서는 안먹습니다.
     *
     */
    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * <h2>
     * 게임의 고정 프레임을 출력
     * </h2>
     *
     * <p>
     * 블럭이 이동할 세로 라인과 골든 라인을 화면에 그리고,<br>
     * 하단에 사용자 입력 안내 문구를 출력합니다.
     */
    private void drawStaticFrame() {
        moveCursor(ROW_OFFSET - 1, 1);
        System.out.println("-".repeat(FRAME_WIDTH));

        for (int i = 0; i <= MAX_HEIGHT; i++) {
            moveCursor(ROW_OFFSET + i, 1);
            if (i == targetLine) {
                System.out.print("  [ GOLDEN LINE ] <--- 타겟");
            } else {
                System.out.print("        |");
            }
        }

        moveCursor(ROW_OFFSET + MAX_HEIGHT + 1, 1);
        System.out.println("-".repeat(FRAME_WIDTH));
        System.out.print("지금이다! [입력/Enter]!!");
    }

    /**
     * <h2>
     * 블럭 이동을 위한 출력 스레드를 시작
     * </h2>
     *
     * <p>
     * 별도 스레드에서 블럭을 일정 시간 간격으로 한 칸씩 위로 이동시키며,<br>
     * 이전 위치를 지우고 현재 위치에 블럭을 다시 출력합니다.
     *
     * <p>
     * 블럭이 화면 끝까지 이동하면 다시 아래쪽부터 반복 이동합니다.
     */
    private void startDisplayThread() {
        Thread thread = new Thread(() -> {
            int lastPos = -1;

            try {
                while (running) {
                    if (lastPos != -1) {
                        moveCursor(ROW_OFFSET + lastPos, COLUMN_POS);
                        if (lastPos == targetLine) {
                            System.out.print("]");
                        } else {
                            System.out.print("|");
                        }
                    }

                    moveCursor(ROW_OFFSET + blockPos, COLUMN_POS);
                    System.out.print("■");

                    lastPos = blockPos;
                    blockPos--;

                    if (blockPos < 0) {
                        blockPos = MAX_HEIGHT;
                    }

                    Thread.sleep(BLOCK_DELAY);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    /**
     * <h2>
     * 콘솔 커서를 지정한 위치로 이동
     * </h2>
     *
     * @param row 이동할 행 위치
     * @param col 이동할 열 위치
     */
    private void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    /**
     * <h2>
     * 블럭 위치가 성공 범위인지 판정
     * </h2>
     *
     * <p>
     * 사용자가 입력한 순간의 블럭 위치와 목표 라인의 차이를 계산하여<br>
     * 차이가 1칸 이하이면 성공, 아니면 실패로 처리합니다.
     *
     * @param pos 입력 순간의 블럭 위치
     * @return 성공 여부
     */
    private boolean isSuccess(int pos) {
        int diff = Math.abs(pos - targetLine);

        if (diff <= 1) {
            io.println("★ SUCCESS! 완벽한 타이밍입니다!");
            return true;
        }

        io.println("FAIL... " + diff + "칸 차이로 선을 넘었습니다.");
        return false;
    }
}