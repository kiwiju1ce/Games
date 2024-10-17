package code;

import code.constant.BowlingConstant;
import code.rule.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundResult {
    private int iter = 1;
    private int chance = 2;
    private final int MAX_TRIAL;
    private int spareBalls = BowlingConstant.MAX_PINS;
    private final List<Roll> results = new ArrayList<>();

    public RoundResult(Rule rule) {
        this.MAX_TRIAL = rule.getMaxIter();
    }

    public void thrown(final int struck, Rule rule) {
        updateIter();
        assert iter <= MAX_TRIAL;

        Frame frame = rule.determineFrame(struck, results);
        int grantedChance = rule.grantChance(frame);

        updateChance(grantedChance);
        assert 0 <= chance && chance <= 2;

        updateSpareBalls(struck);
        assert spareBalls >= 0;

        Roll result = new Roll(struck, frame);
        results.add(result);
    }

    private void updateSpareBalls(int struck) {
        spareBalls -= struck;
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

    public int spareBalls() {
        return spareBalls;
    }

    public List<Roll> results() {
        return Collections.unmodifiableList(results);
    }
}
