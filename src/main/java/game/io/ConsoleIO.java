package game.io;

import java.util.Scanner;

public class ConsoleIO {
    private final Scanner scanner;

    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void print(String message) {
        System.out.print(message);
    }

    public String nextLine() {
        return scanner.nextLine().trim();
    }

    public int nextInt(String message) {
        while (true) {
            try {
                print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                println("숫자만 입력하세요.");
            }
        }
    }

    public int nextIntInRange(String message, int min, int max) {
        while (true) {
            int value = nextInt(message);
            if (value >= min && value <= max) {
                return value;
            }
            println("입력 범위는 " + min + " ~ " + max + " 입니다.");
        }
    }
}