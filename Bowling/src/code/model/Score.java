package code.model;

import java.util.Objects;

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
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return Objects.equals(value, score.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
