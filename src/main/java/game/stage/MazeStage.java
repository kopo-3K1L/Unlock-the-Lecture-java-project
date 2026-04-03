package game.stage;

import game.io.ConsoleIO;
import java.util.Random;

public class MazeStage extends AbstractStage {

    private static final int PATH_LENGTH = 5;
    private static final int CLEAR_LINES = 20;

    private final Random rand = new Random();

    public MazeStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "미로 탈출 게임";
    }

    @Override
    public StageResult play() {
        System.out.println("    ____  ____  __  ___   ______     ____ ");
        System.out.println("   / __ \\/ __ \\/ / / / | / / __ \\   / __ \\");
        System.out.println("  / /_/ / / / / / / /  |/ / / / /  / /_/ /");
        System.out.println(" / _, _/ /_/ / /_/ / /|  / /_/ /   \\__, / ");
        System.out.println("/_/ |_|\\____/\\____/_/ |_/_____/   /____/  ");
        System.out.println("                                          ");
        System.out.println("=========================================================");
        System.out.println("                🧩 미로 탈출 게임 🧩                ");
        System.out.println("=========================================================");
        System.out.println("제시되는 경로를 기억하고 순서대로 입력하세요!");
        System.out.println("5단계를 모두 통과하면 수업한다!");
        System.out.println("공통 명령어: skip(넘기기), retry(다시하기), exit(종료)\n");

        System.out.print("미로 생성 중");
        for (int i = 0; i < 3; i++) {
            sleep(600);
            System.out.print(".");
        }
        System.out.println("\n");

        int[] path = generatePath();

        io.println("제시되는 경로를 기억하세요!");
        sleep(3000);

        for (int i = 0; i < PATH_LENGTH; i++) {
            io.print(toDirection(path[i]) + " ");
            sleep(800);
        }

        io.println("");
        sleep(500);

        for (int i = 0; i < CLEAR_LINES; i++) io.println("");

        io.println("--- 이제 입력하세요 ---");

        for (int stage = 0; stage < PATH_LENGTH; stage++) {
            io.println("\n단계 " + (stage + 1));
            io.println("1. 왼쪽   2. 가운데   3. 오른쪽");

            StageResult result = readAnswer(path[stage]);
            if (result != null) return result;
        }

        io.println("\n🧩 탈출 성공! [수업한다] 경로를 찾았습니다!");
        io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
        sleep(800);
        return new StageResult(StageResultType.SUCCESS, "미로 탈출 클리어!");
    }

    private int[] generatePath() {
        int[] path = new int[PATH_LENGTH];
        for (int i = 0; i < PATH_LENGTH; i++) {
            path[i] = rand.nextInt(3) + 1;
        }
        return path;
    }

    private StageResult readAnswer(int correct) {
        while (true) {
            io.print("선택: ");
            String raw = io.nextLine();

            StageResult cmd = checkCommonCommand(raw);
            if (cmd != null) return cmd;

            int input;
            try {
                input = Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                io.println("숫자를 입력하세요.");
                continue;
            }

            if (input == correct) {
                io.println("통과!");
                return null;
            }

            io.println("으아아악 게임 오버");
            return new StageResult(StageResultType.FAIL, "미로 탈출 실패!");
        }
    }

    private String toDirection(int n) {
        return switch (n) {
            case 1 -> "왼쪽";
            case 2 -> "가운데";
            case 3 -> "오른쪽";
            default -> "";
        };
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
