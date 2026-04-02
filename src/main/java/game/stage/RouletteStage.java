package game.stage;

import game.io.ConsoleIO;
import java.util.Random;

public class RouletteStage extends AbstractStage {

    private Random random;

    public RouletteStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "룰렛 돌리기: 멈춰! 억까 멈춰!";
    }

    @Override
    public StageResult play() {
        String[] items = {"수업한다", "수염한다", "수압한다", "수엽한다", "수얌한다", "수엄한다"};

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("    ____                    __  ____ ");
        System.out.println("   / __ \\____  __  ______  __/ / / __ \\");
        System.out.println("  / /_/ / __ \\/ / / / __ \\/ / / / /_/ /");
        System.out.println(" / _, _/ /_/ / /_/ / / / / /_/ / / __ / ");
        System.out.println("/_/ |_|\\____/\\__,_/_/ /_/\\__,_/ /____/  ");
        System.out.println("                                        ");
        System.out.println("=========================================================");
        System.out.println("           🎰 룰렛 돌리기: 멈춰! 억까 멈춰! 🎰            ");
        System.out.println("=========================================================");
        System.out.println("공통 명령어: skip / retry / exit");

        while (true) {
            System.out.println("\n[Enter]를 누르면 운명의 룰렛을 돌립니다...");
            
            String input = io.nextLine();

            StageResult commandResult = checkCommonCommand(input);
            if (commandResult != null) {
                return commandResult;
            }

            System.out.println("💡 룰렛이 돌아가는 중... 5초 뒤 결과가 나옵니다!");
            String result = getGameResult(items);

            System.out.println("\n-----------------------------------------");
            System.out.println("결과: [" + result + "]");
            System.out.println("-----------------------------------------");

            if (result.equals("수업한다")) {
                System.out.println("💀 억까 당했습니다... 수업 들을게요... (Stage Clear)");
                delay(1500);
                return new StageResult(StageResultType.SUCCESS, "억까를 뚫고 수업에 참여했습니다!");
            } else {
                System.out.println("🎉 축하합니다! 교수님 가방 싸세요! 저흰 먼저 집에 갑니다! 🎉");
                System.out.println("(다시 도전하세요!)");
                delay(1500);
            }
        }
    }

    private String getGameResult(String[] items) {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.print(i + "... ");
                System.out.flush(); 
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        random = new Random();
        int resultIdx = random.nextInt(items.length);
        return items[resultIdx];
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}