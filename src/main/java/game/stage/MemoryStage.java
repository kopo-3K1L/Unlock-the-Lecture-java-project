package game.stage;

import game.io.ConsoleIO;

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

    @Override
    public StageResult play() {
        showIntro();
        initBoard();

        System.out.println(" __  __ ______ __  __  ____  _____  __     __");
        System.out.println("|  \\/  |  ____|  \\/  |/ __ \\|  __ \\ \\ \\   / /");
        System.out.println("| \\  / | |__  | \\  / | |  | | |__) | \\ \\_/ / ");
        System.out.println("| |\\/| |  __| | |\\/| | |  | |  _  /   \\   /  ");
        System.out.println("| |  | | |____| |  | | |__| | | \\ \\    | |   ");
        System.out.println("|_|  |_|______|_|  |_|\\____/|_|  \\_\\   |_|   ");
        System.out.println("==============================================");
        System.out.println("              🧠 메모리 카드 게임 🧠");
        System.out.println("==============================================");
        System.out.println("같은 카드 2장을 맞추면 됩니다.");
        System.out.println("좌표는 행, 열 순서로 입력하세요. 예: 1 3");
        System.out.println("공통 명령어: skip / retry / exit");
        System.out.println();

        while (true) {
            printBoard();

            if (isAllMatched()) {
                System.out.println("🎉 모든 카드를 맞췄습니다!");
                System.out.println("🎉 다음 스테이지(강의)가 열렸습니다!");
                delay(800);
                return new StageResult(StageResultType.SUCCESS, "메모리 게임 클리어!");
            }

            int[] firstPick = promptCoordinate("첫 번째 카드 선택");
            if (firstPick == null) {
                continue;
            }
            StageResult common1 = checkSpecialCommand(firstPick);
            if (common1 != null) {
                return common1;
            }

            Card firstCard = board[firstPick[0]][firstPick[1]];
            if (firstCard.matched || firstCard.revealed) {
                System.out.println("이미 뒤집힌 카드입니다. 다른 카드를 선택하세요.");
                continue;
            }

            firstCard.revealed = true;
            printBoard();

            int[] secondPick = promptCoordinate("두 번째 카드 선택");
            if (secondPick == null) {
                firstCard.revealed = false;
                continue;
            }
            StageResult common2 = checkSpecialCommand(secondPick);
            if (common2 != null) {
                firstCard.revealed = false;
                return common2;
            }

            if (firstPick[0] == secondPick[0] && firstPick[1] == secondPick[1]) {
                System.out.println("같은 위치는 선택할 수 없습니다.");
                firstCard.revealed = false;
                continue;
            }

            Card secondCard = board[secondPick[0]][secondPick[1]];
            if (secondCard.matched || secondCard.revealed) {
                System.out.println("이미 뒤집힌 카드입니다. 다른 카드를 선택하세요.");
                firstCard.revealed = false;
                continue;
            }

            secondCard.revealed = true;
            printBoard();

            if (firstCard.value.equals(secondCard.value)) {
                firstCard.matched = true;
                secondCard.matched = true;
                System.out.println("✅ 짝을 맞췄습니다! [" + firstCard.value + "]");
            } else {
                System.out.println("❌ 틀렸습니다. 잠시 후 다시 뒤집힙니다.");
                delay(1000);
                firstCard.revealed = false;
                secondCard.revealed = false;
            }

            System.out.println();
        }
    }

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

    private void printBoard() {
        System.out.println();
        System.out.println("현재 카드 상태");
        System.out.println("    1   2   3   4");
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
        System.out.println();
    }

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

    private int[] promptCoordinate(String message) {
        System.out.print(message + " (예: 1 3): ");
        String input = io.nextLine();

        StageResult commandResult = checkCommonCommand(input);
        if (commandResult != null) {
            if (commandResult.getType() == StageResultType.SKIP
                    || commandResult.getType() == StageResultType.RETRY
                    || commandResult.getType() == StageResultType.EXIT) {
                return encodeCommand(commandResult.getType());
            }
        }

        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            System.out.println("행과 열을 공백으로 구분해서 입력하세요. 예: 1 3");
            return null;
        }

        try {
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if (row < 1 || row > SIZE || col < 1 || col > SIZE) {
                System.out.println("입력 범위는 1 ~ 4 입니다.");
                return null;
            }

            return new int[]{row - 1, col - 1};
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력하세요.");
            return null;
        }
    }

    private int[] encodeCommand(StageResultType type) {
        if (type == StageResultType.SKIP) {
            return new int[]{-1, -1};
        }
        if (type == StageResultType.RETRY) {
            return new int[]{-2, -2};
        }
        return new int[]{-3, -3};
    }

    private StageResult checkSpecialCommand(int[] value) {
        if (value[0] == -1) {
            return new StageResult(StageResultType.SKIP, "현재 스테이지를 넘깁니다.");
        }
        if (value[0] == -2) {
            return new StageResult(StageResultType.RETRY, "현재 스테이지를 다시 시작합니다.");
        }
        if (value[0] == -3) {
            return new StageResult(StageResultType.EXIT, "게임을 종료합니다.");
        }
        return null;
    }

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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