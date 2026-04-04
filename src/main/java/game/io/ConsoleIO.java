package game.io;

import java.util.Scanner;

/**
 * 콘솔 입출력을 담당하는 클래스
 *
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>문자열 출력 (print, println)</li>
 *     <li>문자열 입력 처리</li>
 *     <li>정수 입력 및 유효성 검사</li>
 *     <li>범위 제한 입력 처리</li>
 * </ul>
 */
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

    /**
     * 한 줄 입력을 받아 반환하는 메서드
     *
     * <p>
     * 입력된 문자열의 앞뒤 공백을 제거한 후 반환
     *
     * @return 사용자 입력 문자열
     */
    public String nextLine() {
        return scanner.nextLine().trim();
    }

    /**
     * 정수 입력을 받아 반환하는 메서드
     *
     * <p>
     * 숫자가 아닌 값이 입력될 경우 예외를 처리하고
     * 올바른 값이 입력될 때까지 반복한다.
     *
     * @param message 입력 안내 메시지
     * @return 입력된 정수 값
     */
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

    /**
     * 지정된 범위 내의 정수를 입력받는 메서드
     *
     * <p>
     * 입력값이 최소값(min)과 최대값(max) 사이가 아닐 경우
     * 다시 입력을 요구
     *
     * @param message 입력 안내 메시지
     * @param min 최소값
     * @param max 최대값
     * @return 범위 내의 정수 값
     */
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