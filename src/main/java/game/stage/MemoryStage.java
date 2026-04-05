package game.stage;

import game.io.ConsoleIO;
import game.stage.core.AbstractStage;
import game.stage.core.StageResult;
import game.stage.core.StageResultType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryStage extends AbstractStage {

    private static final int SIZE = 4;
    private Card[][] board;

    public MemoryStage(ConsoleIO io) {
        super(io);
    }

    @Override
    public String getTitle() {
        return "기억력 카드 뒤집기";
    }

    /**
     * <h2>
     * 기억력 카드 뒤집기
     * </h2>
     *
     * <p>
     * 4x4 카드 보드에서 같은 값의 카드 2장을 찾아 모두 맞추면 클리어하는 게임입니다.<br>
     * 사용자는 행과 열 좌표를 입력하여 카드를 뒤집고, 두 카드의 값이 같으면 짝이 맞은 것으로 처리합니다.
     *
     * <p>
     * 이미 뒤집힌 카드나 같은 위치를 다시 선택할 수 없으며,<br>
     * 모든 카드의 짝을 맞추면 게임 클리어
     *
     * <p>
     * 오래 걸려서 <strong>SKIP</strong>을 추천합니다.<br>
     * 공통 명령어(skip, retry, exit) 가능
     */
    @Override
    public StageResult play() {
        initBoard();

        printStageHeader(6, "              🧠 메모리 카드 게임 🧠");
        io.println("같은 카드 2장을 맞추면 됩니다.");
        io.println("좌표는 행, 열 순서로 입력하세요. 예: 1 3");
        printCommandGuide();
        io.println("");

        while (true) {
            printBoard();

            if (isAllMatched()) {
                io.println("🎉 모든 카드를 맞췄습니다!");
                io.println("🎉 다음 스테이지(강의)가 열렸습니다!");
                delay(800);
                return new StageResult(StageResultType.SUCCESS, "메모리 게임 클리어!");
            }

            String firstInput = promptInput("첫 번째 카드 선택");
            StageResult firstCommand = checkCommonCommand(firstInput);
            if (firstCommand != null) {
                return firstCommand;
            }

            int[] firstPick = parseCoordinate(firstInput);
            if (firstPick == null) {
                continue;
            }

            Card firstCard = board[firstPick[0]][firstPick[1]];
            if (firstCard.matched || firstCard.revealed) {
                io.println("이미 뒤집힌 카드입니다. 다른 카드를 선택하세요.");
                continue;
            }

            firstCard.revealed = true;
            printBoard();

            String secondInput = promptInput("두 번째 카드 선택");
            StageResult secondCommand = checkCommonCommand(secondInput);
            if (secondCommand != null) {
                firstCard.revealed = false;
                return secondCommand;
            }

            int[] secondPick = parseCoordinate(secondInput);
            if (secondPick == null) {
                firstCard.revealed = false;
                continue;
            }

            if (firstPick[0] == secondPick[0] && firstPick[1] == secondPick[1]) {
                io.println("같은 위치는 선택할 수 없습니다.");
                firstCard.revealed = false;
                continue;
            }

            Card secondCard = board[secondPick[0]][secondPick[1]];
            if (secondCard.matched || secondCard.revealed) {
                io.println("이미 뒤집힌 카드입니다. 다른 카드를 선택하세요.");
                firstCard.revealed = false;
                continue;
            }

            secondCard.revealed = true;
            printBoard();

            if (firstCard.value.equals(secondCard.value)) {
                firstCard.matched = true;
                secondCard.matched = true;
                io.println("✅ 짝을 맞췄습니다! [" + firstCard.value + "]");
            } else {
                io.println("❌ 틀렸습니다. 잠시 후 다시 뒤집힙니다.");
                io.println("");
                delay(1000);
                firstCard.revealed = false;
                secondCard.revealed = false;
            }
        }
    }

    /**
     * <h2>
     * 카드 보드를 초기화
     * </h2>
     *
     * <p>
     * A부터 H까지의 카드 값을 각각 2장씩 준비한 뒤 섞어서<br>
     * 4x4 크기의 보드에 차례대로 배치합니다.
     */
    private void initBoard() {
        List<String> values = new ArrayList<>();
        String[] symbols = {"A", "B", "C", "D", "E", "F", "G", "H"};

        for (String symbol : symbols) {
            values.add(symbol);
            values.add(symbol);
        }

        Collections.shuffle(values);

        board = new Card[SIZE][SIZE];
        int index = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = new Card(values.get(index++));
            }
        }
    }

    /**
     * <h2>
     * 현재 카드 보드 상태를 출력
     * </h2>
     *
     * <p>
     * 맞춘 카드나 현재 뒤집힌 카드는 실제 값을 보여주고,<br>
     * 아직 공개되지 않은 카드는 * 문자로 출력합니다.
     */
    private void printBoard() {
        io.println("");
        io.println("현재 카드 상태");
        io.println("    1   2   3   4");

        for (int row = 0; row < SIZE; row++) {
            System.out.print((row + 1) + " | ");
            for (int col = 0; col < SIZE; col++) {
                Card card = board[row][col];
                if (card.matched || card.revealed) {
                    System.out.print(card.value + " | ");
                } else {
                    System.out.print("* | ");
                }
            }
            System.out.println();
        }
        io.println("");
    }

    /**
     * <h2>
     * 모든 카드가 맞춰졌는지 확인
     * </h2>
     *
     * <p>
     * 보드 전체를 순회하며 아직 짝이 맞지 않은 카드가 하나라도 있으면 false를 반환하고,<br>
     * 모든 카드가 matched 상태이면 true를 반환합니다.
     *
     * @return 전체 카드 매칭 완료 여부
     */
    private boolean isAllMatched() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!board[row][col].matched) {
                    return false;
                }
            }
        }
        return true;
    }

    private String promptInput(String message) {
        io.print(message + " (예: 1 3): ");
        return io.nextLine();
    }

    /**
     * <h2>
     * 사용자 입력 좌표를 카드 위치로 변환
     * </h2>
     *
     * <p>
     * 입력 문자열을 공백 기준으로 나누어 행과 열 값을 해석하고,<br>
     * 유효한 범위인지 검사한 뒤 배열 인덱스 형태로 반환합니다.
     *
     * @param input 사용자 입력 문자열
     * @return 변환된 좌표 배열, 잘못된 입력이면 null
     */
    private int[] parseCoordinate(String input) {
        String[] parts = input.trim().split("\\s+");

        if (parts.length != 2) {
            io.println("행과 열을 공백으로 구분해서 입력하세요. 예: 1 3");
            return null;
        }

        Integer row = tryParseInt(parts[0]);
        Integer col = tryParseInt(parts[1]);

        if (row == null || col == null) {
            io.println("숫자만 입력하세요.");
            return null;
        }

        if (row < 1 || row > SIZE || col < 1 || col > SIZE) {
            io.println("입력 범위는 1 ~ 4 입니다.");
            return null;
        }

        return new int[]{row - 1, col - 1};
    }

    private static class Card {
        private final String value;
        private boolean revealed;
        private boolean matched;

        private Card(String value) {
            this.value = value;
            this.revealed = false;
            this.matched = false;
        }
    }
}