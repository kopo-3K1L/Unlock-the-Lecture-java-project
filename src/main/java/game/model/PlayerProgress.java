package game.model;

/**
 * 플레이어의 현재 진행 상태를 저장하는 클래스
 *
 * <p>
 * 현재 스테이지 인덱스를 관리하며,
 * 진행, 초기화 기능을 제공
 */
public class PlayerProgress {

    private int currentStageIndex;
    /**
     * 초기 상태(0번 스테이지)로 생성
     */
    public PlayerProgress() {
        this.currentStageIndex = 0;
    }

    /**
     * 지정된 스테이지 인덱스로 생성
     *
     * @param currentStageIndex 현재 스테이지 번호 (0 이상)
     */
    public PlayerProgress(int currentStageIndex) {
        this.currentStageIndex = Math.max(0, currentStageIndex);
    }

    /**
     * 현재 스테이지 인덱스를 반환
     *
     * @return 현재 스테이지 인덱스
     */
    public int getCurrentStageIndex() {
        return currentStageIndex;
    }

    /**
     * 다음 스테이지로 진행
     */
    public void advance() {
        currentStageIndex++;
    }

    /**
     * 진행 상태를 초기화
     */
    public void reset() {
        currentStageIndex = 0;
    }
}