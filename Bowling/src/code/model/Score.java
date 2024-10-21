package code;

public class Score {
    private final Integer value;

    public Score(Integer value) {
        assert value >= 0;
        this.value = value;
    }

    private Score() {
        this.value = -1;
    }

    public static Score noValue() {
        return new Score();
    }

    public Integer value() {
        ass
        return value;
    }
}
