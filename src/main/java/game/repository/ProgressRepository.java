package game.repository;

import game.model.PlayerProgress;

/**
 * 게임 진행 상태를 저장하고 불러오는 기능을 정의한 인터페이스
 *
 */
public interface ProgressRepository {
    /**
     * 현재 진행 상태를 저장하는 메서드
     *
     * @param progress 플레이어 진행 상태 객체
     */
    void save(PlayerProgress progress);

    /**
     * 저장된 진행 상태를 불러오는 메서드
     *
     * @return 저장된 진행 상태 또는 초기 상태
     */
    PlayerProgress load();
}