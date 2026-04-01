package game.repository;

import game.model.PlayerProgress;

public interface ProgressRepository {
    void save(PlayerProgress progress);
    PlayerProgress load();
}