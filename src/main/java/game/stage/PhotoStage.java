package game.stage;

import game.io.ConsoleIO;

import java.util.Random;

public class PhotoStage extends AbstractStage {

    private static final int TRACK_LENGTH = 31;
    private static final int FOCUS_START = 14;
    private static final int FOCUS_END = 16;
    private static final int MAX_ATTEMPTS = 3;

    public PhotoStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "찰나 포착! 사진 한 방";
    }

    @Override
    public StageResult play() {
        showIntro();

        System.out.println("    ____  ____  __  ___   ______     ____  __  ______  ________");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\   / __ \\/ / / / __ \\/_  __/ /");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /  / /_/ / /_/ / / / / / / / / ");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /  / ____/ __  / /_/ / / / /_/  ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/  /_/   /_/ /_/\\____/ /_/ (_)   ");
        System.out.println("                                                               ");
        System.out.println("===============================================================");
        System.out.println("                  📸 찰나 포착! 사진 한 방 📸");
        System.out.println("===============================================================");
        System.out.println("움직이는 피사체가 중앙 포커스 구역에 들어왔을 때");
        System.out.println("엔터를 눌러 사진을 찍으세요.");
        System.out.println("총 3번의 촬영 기회 중 2번 이상 성공하면 클리어입니다.");
        System.out.println("시작 전 공통 명령어: skip / retry / exit");
        System.out.println();

        String startInput = io.nextLine();
        StageResult commandResult = checkCommonCommand(startInput);
        if (commandResult != null) {
            return commandResult;
        }

        int successCount = 0;
        Random random = new Random();

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.println();
            System.out.println("---------------------------------------------------------------");
            System.out.println("촬영 기회 " + attempt + " / " + MAX_ATTEMPTS);
            System.out.println("포커스 구역 [###] 안에 피사체가 들어오면 엔터를 누르세요.");
            System.out.println("---------------------------------------------------------------");

            final boolean[] shot = {false};

            Thread inputThread = new Thread(() -> {
                io.nextLine();
                shot[0] = true;
            });
            inputThread.setDaemon(true);
            inputThread.start();

            int position = 0;
            boolean attemptFinished = false;

            while (position < TRACK_LENGTH) {
                printTrack(position);

                if (shot[0]) {
                    System.out.println();

                    if (position >= FOCUS_START && position <= FOCUS_END) {
                        System.out.println("📸 찰칵! 완벽하게 포착했습니다!");
                        successCount++;
                    } else {
                        System.out.println("❌ 찰칵! 찍긴 했지만 초점이 빗나갔습니다.");
                    }

                    attemptFinished = true;
                    break;
                }

                try {
                    Thread.sleep(120 + random.nextInt(120));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new StageResult(StageResultType.FAIL, "사진 촬영 중 오류가 발생했습니다.");
                }

                position++;
            }

            if (!attemptFinished) {
                System.out.println();
                System.out.println("❌ 피사체가 화면 밖으로 사라졌습니다. 촬영 실패!");
            }

            System.out.println("현재 성공 횟수: " + successCount + " / 2");
            delay(700);
        }

        if (successCount >= 2) {
            System.out.println();
            System.out.println("===============================================================");
            System.out.println("🎉 훌륭합니다! 결정적인 순간을 포착했습니다!");
            System.out.println("🎉 다음 스테이지(강의)가 열렸습니다!");
            System.out.println("===============================================================");
            delay(800);
            return new StageResult(StageResultType.SUCCESS, "사진찍기 게임 클리어!");
        }

        System.out.println();
        System.out.println("===============================================================");
        System.out.println("❌ 좋은 장면을 충분히 건지지 못했습니다.");
        System.out.println("다시 도전하세요.");
        System.out.println("===============================================================");
        return new StageResult(StageResultType.FAIL, "사진찍기 게임 실패!");
    }

    private void printTrack(int position) {
        StringBuilder sb = new StringBuilder();
        sb.append("\r[");

        for (int i = 0; i < TRACK_LENGTH; i++) {
            if (i == position) {
                sb.append("●");
            } else if (i >= FOCUS_START && i <= FOCUS_END) {
                sb.append("#");
            } else {
                sb.append(".");
            }
        }

        sb.append("]  ");
        sb.append("포커스: 중앙 [###]");

        System.out.print(sb);
    }

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}