package game.stage;

import game.io.ConsoleIO;

public class EndStage extends AbstractStage{
    public EndStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "최종화면";
    }

    @Override
    public StageResult play() {
        System.out.println("    ____                      __  ____ ____");
        System.out.println("   / __ \\____  __  ______  __/ / /_  /|__ /");
        System.out.println("  / /_/ / __ \\/ / / / __ \\/ / /   / /  |_ \\");
        System.out.println(" / _, _/ /_/ / /_/ / / / / /_/ /  / /  ___/ /");
        System.out.println("/_/ |_|\\____/\\__,_/_/ /_/\\__,_/  /_/  /____/ ");
        System.out.println("                                           ");

        System.out.println("=========================================================");
        System.out.println("             \uD83E\uDD15  최종화면 \uD83E\uDD15             ");
        System.out.println("=========================================================");
        System.out.println("\n      💣💣💣 미션 클리어: 과제 폭탄 투하! 💣💣💣      ");
        System.out.println("   /\\____/\\                                       ");
        System.out.println("  (  o.o  )  <-- (교수님: 아, 하나 더 있었네?)      ");
        System.out.println("   (  > < )                                        ");
        System.out.println("    V    V                                         ");
        delay(800);
        return new StageResult(StageResultType.SUCCESS, "억까를 뚫고 수업에 참여했습니다!");
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
