package code.rule;

import code.model.Frame;
import code.model.Roll;

import java.util.List;

import static code.constant.BowlingConstant.MAX_PINS;

public class FinalFrameRule implements Rule {

    @Override
    public int getMaxIter() {
        return 3;
    }

    @Override
    public Frame determineFrame(int struck, List<Roll> results) {
        return switch (results.size()) {
            case 0 -> firstRoll(struck);
            case 1 -> secondRoll(struck, results);
            case 2 -> thirdRoll(struck, results);
            default -> throw new IllegalStateException("Unexpected value: " + results.size());
        };
    }

    private Frame firstRoll(int struck) {
        return switch (struck) {
            case MAX -> Frame.STRIKE;
            case NONE -> Frame.GUTTER;
            default -> Frame.OPEN;
        };
    }

    private Frame secondRoll(int struck, List<Roll> results) {
        Roll former = results.getFirst();
        return switch (former.frame()) {
            case GUTTER -> withGutter(struck);
            case STRIKE -> withStrike(struck);
            case OPEN -> withOpen(struck, former.struck());
            default -> throw new IllegalStateException("Unexpected value: " + former.frame());
        };
    }

    private Frame withGutter(int struck) {
        return switch (struck) {
            case MAX -> Frame.SPARE;
            case NONE -> Frame.MISS;
            default -> Frame.OPEN;
        };
    }

    private Frame withStrike(int struck) {
        return switch (struck) {
            case MAX -> Frame.STRIKE;
            case NONE -> Frame.GUTTER;
            default -> Frame.OPEN;
        };
    }

    private Frame withOpen(int struck, int formerStruck) {
        return switch (struck) {
            case MAX -> throw new IllegalStateException("Unexpected value: " + struck);
            case NONE -> Frame.MISS;
            default -> formerStruck + struck == MAX_PINS ?
                    Frame.SPARE : Frame.OPEN;
        };
    }

    private Frame thirdRoll(int struck, List<Roll> results) {
        Roll former = results.get(1);
        return switch (former.frame()) {
            case STRIKE -> withStrike(struck);
            case OPEN -> withOpen(struck, former.struck());
            case SPARE -> withSpare(struck);
            case GUTTER -> withGutter(struck);
            default -> throw new IllegalStateException("Unexpected value: " + struck);
        };
    }

    private Frame withSpare(int struck) {
        return firstRoll(struck);
    }

    @Override
    public int grantChance(Frame frame, int iter) {
        return switch (iter) {
            case 1 -> frame == Frame.STRIKE ? 1 : 0;
            case 2 -> frame == Frame.SPARE ? 1 : 0;
            case 3 -> 0;
            default -> throw new IllegalStateException("Unexpected iter: " + iter);
        };
    }
}
