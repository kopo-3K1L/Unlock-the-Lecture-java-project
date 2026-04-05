package game.manager;

import game.io.ConsoleIO;
import game.model.PlayerProgress;
import game.repository.ProgressRepository;
import game.stage.core.Stage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.List;

/**
 * 게임 전체 흐름을 제어하는 클래스
 *
 * <p>
 * 주요 역할:
 * <ul>
 *     <li>메인 메뉴 출력 및 사용자 입력 처리</li>
 *     <li>게임 시작, 이어하기, 초기화 기능 제공</li>
 *     <li>각 스테이지 실행 및 결과 처리</li>
 *     <li>진행 상태를 저장소에 저장</li>
 * </ul>
 *
 * <p>
 * 동작 흐름:
 * <ol>
 *     <li>메뉴 출력</li>
 *     <li>사용자 선택 입력</li>
 *     <li>게임 실행 또는 초기화</li>
 * </ol>
 */
public class GameManager {
    private final ConsoleIO io;
    private final List<Stage> stages;
    private final PlayerProgress progress;
    private final ProgressRepository repository;

    public GameManager(ConsoleIO io, List<Stage> stages, PlayerProgress progress, ProgressRepository repository) {
        this.io = io;
        this.stages = stages;
        this.progress = progress;
        this.repository = repository;
    }

    /**
     * <h2>
     * 메인 메뉴를 반복 실행
     * </h2>
     *
     * <p>
     * 현재 진행 상태에 따라 메뉴 구성을 다르게 보여주고,<br>
     * 사용자의 선택에 따라 게임 시작, 초기화, 종료를 처리합니다.
     */
    public void start() {
        while (true) {
            printMenu();

            // 첫 단계라면 "처음부터, 종료"만 표시
            if (progress.getCurrentStageIndex() == 0) {
                int menu = io.nextIntInRange("선택: ", 1, 2);

                switch (menu) {
                    case 1:
                        playGame();
                        break;
                    case 2:
                        io.println("게임을 종료합니다.");
                        return;
                }
            } else {
                int menu = io.nextIntInRange("선택: ", 1, 3);

                switch (menu) {
                    case 1:
                        playGame();
                        break;
                    case 2:
                        resetProgress();
                        break;
                    case 3:
                        io.println("게임을 종료합니다.");
                        return;
                }
            }
        }
    }

    /**
     * <h2>
     * 현재 진행 상태에 맞는 메뉴를 출력
     * </h2>
     *
     * <p>
     * 첫 단계에서는 이어하기가 필요 없으므로<br>
     * 처음부터와 종료 메뉴만 출력합니다.
     *
     * <p>
     * 그 외에는 이어하기, 처음부터, 종료 메뉴를 모두 출력합니다.
     */
    private void printMenu() {
        io.println("\n==============================");
        io.println(" Unlock the Lecture - Console ");
        io.println("==============================");

        if (progress.getCurrentStageIndex() >= stages.size()) {
            io.println("현재 진행 스테이지: 클리어");
        } else {
            io.println("현재 진행 스테이지: " + (progress.getCurrentStageIndex() + 1));
        }

        if (progress.getCurrentStageIndex() == 0) {
            io.println("1. 시작하기");
            io.println("2. 종료");
        } else {
            io.println("1. 이어하기");
            io.println("2. 처음부터");
            io.println("3. 종료");
        }
    }

    /**
     * 실제 게임 진행을 담당하는 메서드
     *
     * <p>
     * 현재 스테이지부터 시작하여 순차적으로 실행한다.
     * 스테이지 결과에 따라 다음과 같이 처리된다:
     *
     * <ul>
     *     <li>SUCCESS / SKIP : 다음 스테이지로 이동 후 저장</li>
     *     <li>RETRY : 현재 스테이지 재시작</li>
     *     <li>EXIT : 진행 상태 저장 후 종료</li>
     *     <li>FAIL : 실패 메시지 출력 후 재도전</li>
     * </ul>
     */
    private void playGame() {
        while (progress.getCurrentStageIndex() < stages.size()) {
            Stage currentStage = stages.get(progress.getCurrentStageIndex());
            StageResult result = currentStage.play();

            io.println(result.getMessage());

            if (result.getType() == StageResultType.SUCCESS || result.getType() == StageResultType.SKIP) {
                progress.advance();
                repository.save(progress);

                io.println("");
                io.println("");
                io.println("");
                io.println("");
                io.println("");

            } else if (result.getType() == StageResultType.RETRY) {
                io.println("");
                io.println("현재 스테이지를 다시 시작합니다.");
                io.println("");
            } else if (result.getType() == StageResultType.EXIT) {
                repository.save(progress);
                return;
            } else if (result.getType() == StageResultType.FAIL) {
                io.println("");
                io.println("실패했습니다. 다시 도전하세요.");
                io.println("");
            }
        }
        io.println("");
        io.println("모든 스테이지를 클리어했습니다!");
        repository.save(progress);
    }

    /**
     * 진행 상태를 초기화하는 메서드
     *
     * <p>
     * 현재 스테이지 정보를 초기 상태로 되돌리고
     * 저장소에 반영한다.
     */
    private void resetProgress() {
        progress.reset();
        repository.save(progress);
        io.println("저장된 진행 상태가 초기화되었습니다.");
    }
}