package game.repository;

import game.model.PlayerProgress;

import java.io.*;

public class FileProgressRepository implements ProgressRepository {
    private final String filePath;

    public FileProgressRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(PlayerProgress progress) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(progress.getCurrentStageIndex()));
        } catch (IOException e) {
            System.out.println("저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

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