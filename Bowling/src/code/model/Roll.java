package code;

import code.exception.InvalidInputException;

import static code.constant.BowlingConstant.MAX_PINS;

public class Roll {
    private final Integer struck;
    private final Frame frame;

    public Roll(int struck, Frame frame) {
        validate(struck);
        this.struck = struck;
        this.frame = frame;
    }

    private void validate(int value) {
        if (value > MAX_PINS) {
            throw new InvalidInputException("쓰러트린 핀의 수는 10개 초과일 수 없습니다!");
        }
    }

    public Integer struck() {
        return struck;
    }

    public Frame frame() {
        return frame;
    }
}
