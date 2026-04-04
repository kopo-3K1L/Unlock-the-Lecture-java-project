package game;

import game.io.ConsoleIO;
import game.manager.GameManager;
import game.model.PlayerProgress;
import game.repository.FileProgressRepository;
import game.repository.ProgressRepository;
import game.stage.*;
import game.stage.core.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>🎮 Unlock the Lecture - Console Game</h2>
 *
 * <p>
 * 학교 프론트엔드 기반 프로젝트를 순수 Java 콘솔 환경으로 재구현한 미니게임 프로젝트
 * 여러 개의 스테이지를 순차적으로 클리어해야 최종적으로 수업을 진행할 수 있습니다.
 *
 * <p>
 * 각 스테이지는 독립적인 게임으로 구성되어 있으며,
 * 공통 명령어(skip, retry, exit)를 통해 넘어가거나 다시하기가 가능합니다. <br>
 * 힘드시면 게임 <strong>SKIP</strong> 으로 넘어가주세요.
 *
 * <p>
 * <b>주요 역할:</b>
 * <ul>
 *     <li>입출력(ConsoleIO) 객체 생성</li>
 *     <li>진행 상태 저장소(Repository) 초기화 및 로드</li>
 *     <li>스테이지 목록 생성</li>
 *     <li>GameManager를 통해 전체 게임 흐름 실행</li>
 * </ul>
 *
 * <p>
 * <b>실행 흐름:</b>
 * <ol>
 *     <li>저장된 진행 상태 로드</li>
 *     <li>스테이지 리스트 생성</li>
 *     <li>GameManager 실행</li>
 * </ol>
 *
 * <p>
 * <b>설계 특징:</b>
 * <ul>
 *     <li>Stage 인터페이스 기반의 확장 가능한 구조</li>
 *     <li>AbstractStage를 통한 공통 기능(입력, 명령어, 출력) 통합</li>
 *     <li>Repository 패턴을 활용한 진행 상태 관리</li>
 * </ul>
 *
 * <p>
 * <b>게임 목표:</b><br>
 * 모든 스테이지를 클리어하여 "수업한다" 상태에 도달하는 것 <br>
 * ※ 복잡한 코드는 도큐먼트 주석 썼습니다. 감사합니다.
 */
public class Main {

    private static final String SAVE_FILE_PATH = "save.txt";

    public static void main(String[] args) {
        ConsoleIO io = new ConsoleIO();
        ProgressRepository repository = new FileProgressRepository(SAVE_FILE_PATH);
        PlayerProgress progress = repository.load();

        List<Stage> stages = createStages(io);

        GameManager gameManager = new GameManager(io, stages, progress, repository);
        gameManager.start();
    }

    /**
     * 게임에 사용되는 스테이지 목록을 생성
     *
     * <p>
     * 각 스테이지는 List에 추가된 순서대로 실행
     * 여기에 스테이지 추가해주세요. 순서대로 입니다.
     *
     * @param io 콘솔 입출력 객체
     * @return 스테이지 리스트
     */
    private static List<Stage> createStages(ConsoleIO io) {
        List<Stage> stages = new ArrayList<>();

        stages.add(new IntroStage(io)); // 1번 인트로
        stages.add(new UpDownStage(io)); // 2번 업다운 게임
        stages.add(new TimerStage(io)); // 3번 타이머 게임
        stages.add(new RGBStage(io)); // 4번 RGB 게임
        stages.add(new DragStage(io)); // 5번 드래그 게임
        stages.add(new MemoryStage(io)); // 6번 메모리 게임
        stages.add(new LineStage(io)); // 7번 선넘지마 게임
        stages.add(new RouletteStage(io)); // 8번 룰렛 게임
        stages.add(new MazeStage(io)); // 9번 미로 찾기
        stages.add(new DarkStage(io)); // 10번 다크 다크 게임
        stages.add(new PhotoStage(io)); // 11번 사진 찍기 게임
        stages.add(new StairsStage(io)); // 12번 무한의 계단
        stages.add(new EndStage(io)); // 13번 엔딩

        return stages;
    }
}