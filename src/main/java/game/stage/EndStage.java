package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

public class EndStage extends AbstractStage {
    public EndStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "최종화면";
    }

    /**
     * <h2>
     * 엔딩
     * </h2>
     *
     * <p>
     * 모든 스테이지를 클리어한 뒤 출력되는 엔딩 화면입니다.<br>
     * 최종 메시지와 ASCII 아트를 보여준 후 게임 성공 결과를 반환합니다.
     */
    @Override
    public StageResult play() {
        printStageHeader(13, "             🤖  최종화면 🤖");
        io.println("\n      💣💣💣 미션 클리어: 과제 폭탄 투하! 💣💣💣      ");
        io.println("   /\\____/\\                                       ");
        io.println("  (  o.o  )  <-- (교수님: 아, 하나 더 있었네?)      ");
        io.println("   (  > < )                                        ");
        io.println("    V    V                                         ");
        delay(800);
        return new StageResult(StageResultType.SUCCESS, "억까를 뚫고 수업에 참여했습니다!");
    }
}