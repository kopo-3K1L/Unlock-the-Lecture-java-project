package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.Random;

public class RouletteStage extends AbstractStage {
    private static final String[] ITEMS = {
            "수업한다", "수염한다", "수압한다", "수엽한다", "수얌한다", "수엄한다"
    };

    private static final int COUNTDOWN_SECONDS = 5;
    private static final int RESULT_DELAY = 1500;

    private final Random random = new Random();

    public RouletteStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "룰렛 돌리기: 멈춰! 억까 멈춰!";
    }

    /**
     * <h2>
     * 룰렛 돌리기: 멈춰! 억까 멈춰!
     * </h2>
     *
     * <p>
     * 사용자가 엔터를 입력하면 룰렛이 돌아가고,<br>
     * 여러 결과 중 하나가 무작위로 선택됩니다.
     *
     * <p>
     * 결과가 [수업한다]이면 스테이지 클리어이며,<br>
     * 그 외의 결과가 나오면 다시 도전해야 합니다.
     *
     * <p>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        clearConsole();

        printStageHeader(8, "           🎰 룰렛 돌리기: 멈춰! 억까 멈춰! 🎰");
        printCommandGuide();

        while (true) {
            io.println("\n[Enter]를 누르면 운명의 룰렛을 돌립니다...");

            String input = io.nextLine();
            StageResult commandResult = checkCommonCommand(input);
            if (commandResult != null) {
                return commandResult;
            }

            io.println("💡 룰렛이 돌아가는 중... 5초 뒤 결과가 나옵니다!");
            String result = getGameResult();

            io.println("\n-----------------------------------------");
            io.println("결과: [" + result + "]");
            io.println("-----------------------------------------");

            if (isSuccess(result)) {
                io.println("💀 억까 당했습니다... 수업 들을게요... (Stage Clear)");
                delay(RESULT_DELAY);
                return new StageResult(StageResultType.SUCCESS, "억까를 뚫고 수업에 참여했습니다!");
            }

            io.println("🎉 축하합니다! 교수님 가방 싸세요! 저흰 먼저 집에 갑니다! 🎉");
            io.println("(다시 도전하세요!)");
            delay(RESULT_DELAY);
        }
    }

    /**
     * <h2>
     * 콘솔 화면을 초기화
     * </h2>
     *
     * <p>
     * ANSI 이스케이프 코드를 사용하여 콘솔 화면을 지우고<br>
     * 커서를 좌측 상단으로 이동시킵니다.
     */
    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * <h2>
     * 룰렛 결과를 생성
     * </h2>
     *
     * <p>
     * 5초 카운트다운을 출력한 뒤,<br>
     * 결과 문자열 배열에서 하나를 무작위로 선택하여 반환합니다.
     *
     * @return 룰렛 결과 문자열
     */
    private String getGameResult() {
        try {
            for (int i = COUNTDOWN_SECONDS; i > 0; i--) {
                System.out.print(i + "... ");
                System.out.flush();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int resultIndex = random.nextInt(ITEMS.length);
        return ITEMS[resultIndex];
    }

    /**
     * <h2>
     * 룰렛 결과가 성공인지 판정
     * </h2>
     *
     * <p>
     * 룰렛 결과가 [수업한다]인 경우에만<br>
     * 성공으로 판정합니다.
     *
     * @param result 룰렛 결과 문자열
     * @return 성공 여부
     */
    private boolean isSuccess(String result) {
        return "수업한다".equals(result);
    }
}