package code.model;

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

    /**
     * 점수 계산을 위한 추가 프레임 조사 필요 여부
     * @return boolean
     */
    public boolean needMoreFrames() {
        return this == STRIKE || this == SPARE;
    }

    public boolean hasCode() {
        return this != OPEN && this != SPLIT;
    }
}