package code.model;

import code.exception.InvalidPinsException;
import code.rule.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static code.constant.BowlingConstant.MAX_PINS;

public class RoundResult {
    private int iter = 1;
    private int chance = 2;
    private final Rule rule;
    private int sparePins = MAX_PINS;
    private Score score = Score.noValue();
    private final List<Roll> rolls = new ArrayList<>();

    public RoundResult(Rule rule) {
        this.rule = rule;
    }

    public void thrown(final int struck) {
        updateSparePins(struck);
        if (sparePins < 0) {
            updateSparePins(struck * -1);
            throw new InvalidPinsException("한 프레임에서 10개 이상의 핀을 쓰러트릴 순 없습니다!");
        }

        Frame frame = rule.determineFrame(struck, rolls);
        int grantedChance = rule.grantChance(frame, iter);
        updateChance(grantedChance);
        if (sparePins == 0 && canProcess()) {
            refillPins();
        }

        updateIter();
        assert iter <= rule.getMaxIter();

        Roll result = new Roll(struck, frame);
        rolls.add(result);
    }

    private void refillPins() {
        this.sparePins = MAX_PINS;
    }

    public boolean canProcess() {
        return chance > 0;
    }

    void setScore(Score score) {
        if (scoreCalculated()) {
            throw new IllegalStateException("이미 점수가 산정되었습니다 : " + score.value());
        }
        this.score = score;
    }

    public boolean scoreCalculated() {
        return !Objects.equals(score, Score.noValue());
    }

    private void updateSparePins(int struck) {
        sparePins -= struck;
    }

    private void updateChance(int grantedChance) {
        chance += grantedChance - 1;
    }

    private void updateIter() {
        iter++;
    }

    public int iter() {
        return iter;
    }

    public int chance() {
        return chance;
    }

    public int sparePins() {
        return sparePins;
    }

    public Integer score() {
        return score.value();
    }

    public List<Roll> rolls() {
        return Collections.unmodifiableList(rolls);
    }
}
