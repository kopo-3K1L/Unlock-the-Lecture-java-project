package game.stage;

import game.io.ConsoleIO;
import java.util.Random;

public class LineStage extends AbstractStage {
    public volatile boolean running;
    private int blockPos = 20;
    private int targetLine;
    private final int MAX_HEIGHT = 20;
    

    private final int COLUMN_POS = 9;
    private final int ROW_OFFSET = 12;

    public LineStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "선 넘지 마세요 제발";
    }

    @Override
    public StageResult play() {
        running = true;
        blockPos = MAX_HEIGHT;
        System.out.print("\033[H\033[2J"); 
        System.out.flush();

        System.out.println("    ____                    __  _____");
        System.out.println("   / __ \\____  __  ______  __/ / /__  /");
        System.out.println("  / /_/ / __ \\/ / / / __ \\/ / / /  / / ");
        System.out.println(" / _, _/ /_/ / /_/ / / / / /_/ /  / /  ");
        System.out.println("/_/ |_|\\____/\\__,_/_/ /_/\\__,_/  /_/   ");
        System.out.println("                                        ");
        System.out.println("=========================================================");
        System.out.println("                🚨 선 넘지 마세요 제발! 🚨                ");
        System.out.println("=========================================================");
        System.out.println("움직이는 블럭이 골든 라인에 닿는 순간 [Enter]를 누르세요!");
        System.out.println("(명령어 입력: skip / retry / exit)");

        Random random = new Random();
        targetLine = random.nextInt(15) + 2;

        drawStaticFrame(); 
        display();

        String input = io.nextLine();
        running = false; 

        StageResult commandResult = checkCommonCommand(input);
        if (commandResult != null) {
            return commandResult;
        }

        moveCursor(ROW_OFFSET + MAX_HEIGHT + 3, 1);
        int result = checkResult(blockPos);

        if (result == 1) {
            System.out.println("\n🎉 축하합니다! 다음 강의(스테이지)가 열렸습니다!");
            delay(1200);
            return new StageResult(StageResultType.SUCCESS, "선 넘지 마세요 제발 클리어!");
        } else {
            System.out.println("\n💀 다시 도전하세요.");
            delay(1200);
            return new StageResult(StageResultType.FAIL, "타이밍 맞추기 실패");
        }
    }

    private void drawStaticFrame() {
        moveCursor(ROW_OFFSET - 1, 1);
        System.out.println("--------------------------");

        for (int i = 0; i <= MAX_HEIGHT; i++) {
            moveCursor(ROW_OFFSET + i, 1);
            if (i == targetLine) {
                System.out.print("  [ GOLDEN LINE ] <--- 타겟");
            } else {
                System.out.print("        |");
            }
        }

        moveCursor(ROW_OFFSET + MAX_HEIGHT + 1, 1);
        System.out.println("--------------------------");
        System.out.print("지금이다! [입력/Enter]!!");
    }

    private void display() {
        Thread thread = new Thread(() -> {
            int lastPos = -1;
            try {
                while (running) {
                    if (lastPos != -1) {
                        moveCursor(ROW_OFFSET + lastPos, COLUMN_POS);
                        if (lastPos == targetLine) System.out.print("]");
                        else System.out.print("|");
                    }

                    moveCursor(ROW_OFFSET + blockPos, COLUMN_POS);
                    System.out.print("■");

                    lastPos = blockPos;
                    blockPos--;

                    if (blockPos < 0) blockPos = MAX_HEIGHT;

                    Thread.sleep(80); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }

    private int checkResult(int pos) {
        int diff = Math.abs(pos - targetLine);
        if (diff <= 1) { 
            System.out.println("★ SUCCESS! 완벽한 타이밍입니다!          ");
            return 1;
        } else {
            System.out.println("FAIL... " + diff + "칸 차이로 선을 넘었습니다.      ");
            return 0;
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