package code;

public enum Frame {
    STRIKE("X"),
    OPEN(null),
    SPARE("/"),
    SPLIT(null),
    MISS("-"),
    GUTTER("G"),
    FOUL("F");

    private final String code;

    Frame(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}