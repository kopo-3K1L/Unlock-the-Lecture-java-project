package game.stage.ui;

/**
 * 콘솔 출력 색상을 위한 ANSI 코드 상수 클래스
 */
public final class AnsiColor {
    private AnsiColor() {
    }

    public static final String RESET = "\u001b[0m";

    public static final String BLACK_TEXT = "\u001b[30m";
    public static final String RED_TEXT = "\u001b[31m";
    public static final String GREEN_TEXT = "\u001b[32m";
    public static final String YELLOW_TEXT = "\u001b[33m";
    public static final String BLUE_TEXT = "\u001b[34m";
    public static final String PURPLE_TEXT = "\u001b[35m";
    public static final String CYAN_TEXT = "\u001b[36m";
    public static final String WHITE_TEXT = "\u001b[37m";

    public static final String BLACK_BG = "\u001b[40m";
    public static final String RED_BG = "\u001b[41m";
    public static final String GREEN_BG = "\u001b[42m";
    public static final String YELLOW_BG = "\u001b[43m";
    public static final String BLUE_BG = "\u001b[44m";
    public static final String WHITE_BG = "\u001b[47m";
}