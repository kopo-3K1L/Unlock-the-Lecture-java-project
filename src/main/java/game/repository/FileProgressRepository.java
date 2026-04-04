package game.repository;

import game.model.PlayerProgress;

import java.io.*;

/**
 * 파일을 이용하여 게임 진행 상태를 저장/불러오는 클래스
 *
 * <p>
 * 텍스트 파일에 현재 스테이지 인덱스를 저장하며,
 * 프로그램 재시작 시 해당 파일을 읽어 이어하기 기능을 제공
 *
 * <p>
 * 예외 처리:
 * <ul>
 *     <li>파일이 존재하지 않으면 새로 시작</li>
 *     <li>파일 내용이 비어있거나 잘못된 경우 초기 상태 반환</li>
 * </ul>
 */
public class FileProgressRepository implements ProgressRepository {
    private final String filePath;

    public FileProgressRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 현재 진행 상태를 파일에 저장하는 메서드
     *
     * @param progress 현재 플레이어 진행 상태
     */
    @Override
    public void save(PlayerProgress progress) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(progress.getCurrentStageIndex()));
        } catch (IOException e) {
            System.out.println("저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 파일로부터 진행 상태를 불러오는 메서드
     *
     * <p>
     * 파일이 없거나 데이터가 올바르지 않은 경우
     * 새로운 진행 상태를 생성하여 반환
     *
     * @return 저장된 진행 상태 또는 초기 상태
     */
    @Override
    public PlayerProgress load() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new PlayerProgress();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();

            if (line == null || line.isBlank()) {
                return new PlayerProgress();
            }

            int stageIndex = Integer.parseInt(line.trim());

            if (stageIndex < 0) {
                return new PlayerProgress();
            }

            return new PlayerProgress(stageIndex);

        } catch (IOException | NumberFormatException e) {
            System.out.println("저장 파일을 읽는 중 오류가 발생했습니다. 처음부터 시작합니다.");
            return new PlayerProgress();
        }
    }
}