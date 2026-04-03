package game.stage.core;

/**
 * 스테이지 실행 결과를 나타내는 열거형(enum)
 *
 * <p>
 * 각 스테이지 수행 후 결과 상태를 구분하기 위해 사용
 *
 * <ul>
 *     <li>SUCCESS : 스테이지 성공</li>
 *     <li>FAIL : 스테이지 실패</li>
 *     <li>EXIT : 게임 종료 요청</li>
 *     <li>SKIP : 스테이지 건너뛰기</li>
 *     <li>RETRY : 스테이지 재시도</li>
 * </ul>
 */
public enum StageResultType {
    SUCCESS,
    FAIL,
    EXIT,
    SKIP,
    RETRY
}