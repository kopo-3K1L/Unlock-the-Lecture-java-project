package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.Random;

public class PhotoStage extends AbstractStage {

    private static final int TRACK_LENGTH = 31;
    private static final int FOCUS_START = 14;
    private static final int FOCUS_END = 16;
    private static final int MAX_ATTEMPTS = 3;
    private static final int REQUIRED_SUCCESS = 2;

    private final Random random = new Random();

    public PhotoStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "찰나 포착! 사진 한 방";
    }

    /**
     * <h2>
     * 찰나 포착! 사진 한 방
     * </h2>
     *
     * <p>
     * 움직이는 피사체가 중앙 포커스 구역에 들어왔을 때<br>
     * 엔터를 눌러 사진을 찍는 타이밍 게임입니다.
     *
     * <p>
     * 총 3번의 촬영 기회 중 2번 이상 성공하면 게임 클리어 <br>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        printStageHeader(11, "                  📸 찰나 포착! 사진 한 방 📸");
        io.println("움직이는 피사체가 중앙 포커스 구역에 들어왔을 때");
        io.println("엔터를 눌러 사진을 찍으세요.");
        io.println("총 3번의 촬영 기회 중 2번 이상 성공하면 클리어입니다.");
        printCommandGuide();

        String startInput = io.nextLine();
        StageResult commandResult = checkCommonCommand(startInput);
        if (commandResult != null) {
            return commandResult;
        }

        int successCount = 0;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            boolean success = runAttempt(attempt);

            if (success) {
                successCount++;
            }

            io.println("현재 성공 횟수: " + successCount + " / " + REQUIRED_SUCCESS);
            delay(700);
        }

        return buildResult(successCount);
    }

    /**
     * <h2>
     * 한 번의 촬영 시도를 진행
     * </h2>
     *
     * <p>
     * 피사체를 왼쪽에서 오른쪽으로 이동시키면서 출력하고,<br>
     * 사용자가 엔터를 누른 순간의 위치를 기준으로 촬영 성공 여부를 판정합니다.
     *
     * @param attempt 현재 촬영 시도 번호
     * @return 촬영 성공 여부
     */
    private boolean runAttempt(int attempt) {
        io.println("---------------------------------------------------------------");
        io.println("촬영 기회 " + attempt + " / " + MAX_ATTEMPTS);
        io.println("포커스 구역 [###] 안에 피사체가 들어오면 엔터를 누르세요.");
        io.println("---------------------------------------------------------------");

        final boolean[] shot = {false};

        Thread inputThread = new Thread(() -> {
            io.nextLine();
            shot[0] = true;
        });
        inputThread.setDaemon(true);
        inputThread.start();

        int position = 0;

        while (position < TRACK_LENGTH) {
            printTrack(position);

            if (shot[0]) {
                System.out.println();
                return handleShot(position);
            }

            try {
                Thread.sleep(120 + random.nextInt(120));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            position++;
        }

        System.out.println();
        io.println("❌ 피사체가 화면 밖으로 사라졌습니다. 촬영 실패!");
        return false;
    }

    /**
     * <h2>
     * 촬영 시점의 위치를 기준으로 성공 여부를 판정
     * </h2>
     *
     * <p>
     * 사용자가 촬영한 순간의 피사체 위치가 중앙 포커스 구역 안에 있으면 성공,<br>
     * 포커스 범위를 벗어나 있으면 실패로 처리합니다.
     *
     * @param position 촬영 순간의 피사체 위치
     * @return 성공 여부
     */
    private boolean handleShot(int position) {
        if (position >= FOCUS_START && position <= FOCUS_END) {
            io.println("📸 찰칵! 완벽하게 포착했습니다!");
            return true;
        } else {
            io.println("❌ 찰칵! 찍긴 했지만 초점이 빗나갔습니다.");
            return false;
        }
    }

    /**
     * <h2>
     * 전체 촬영 결과를 바탕으로 최종 결과를 생성
     * </h2>
     *
     * <p>
     * 전체 성공 횟수가 기준 이상이면 스테이지 성공,<br>
     * 기준에 미달하면 실패 결과를 반환합니다.
     *
     * @param successCount 총 성공 횟수
     * @return 스테이지 최종 결과
     */
    private StageResult buildResult(int successCount) {
        if (successCount >= REQUIRED_SUCCESS) {
            io.println("===============================================================");
            io.println("🎉 훌륭합니다! 결정적인 순간을 포착했습니다!");
            io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
            io.println("===============================================================");
            delay(800);
            return new StageResult(StageResultType.SUCCESS, "사진찍기 게임 클리어!");
        }

        io.println("===============================================================");
        io.println("❌ 좋은 장면을 충분히 건지지 못했습니다.");
        io.println("다시 도전하세요.");
        io.println("===============================================================");
        return new StageResult(StageResultType.FAIL, "사진찍기 게임 실패!");
    }

    /**
     * <h2>
     * 현재 피사체 위치와 포커스 구역을 화면에 출력
     * </h2>
     *
     * <p>
     * 이동 중인 피사체는 ● 문자로 표시하고,<br>
     * 중앙 포커스 구역은 # 문자로 구분하여 한 줄 트랙 형태로 출력합니다.
     *
     * @param position 현재 피사체 위치
     */
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

        sb.append("]  포커스: 중앙 [###]");
        System.out.print(sb);
    }
}