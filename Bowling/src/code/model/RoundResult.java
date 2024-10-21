package code;

import code.constant.BowlingConstant;
import code.rule.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundResult {
    private int iter = 1;
    private int chance = 2;
    private final Rule rule;
    private int sparePins = BowlingConstant.MAX_PINS;
    private Score score;
    private final List<Roll> results = new ArrayList<>();

    public RoundResult(Rule rule) {
        this.rule = rule;
    }

    public void thrown(final int struck) {
        updateIter();
        assert iter <= rule.getMaxIter();

        Frame frame = rule.determineFrame(struck, results);
        int grantedChance = rule.grantChance(frame, iter);

        updateChance(grantedChance);
        assert 0 <= chance && chance <= 2;

        updateSparePins(struck);
        assert sparePins >= 0;

        Roll result = new Roll(struck, frame);
        results.add(result);
    }

    public boolean canProcess() {
        return chance > 0;
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

    public List<Roll> results() {
        return Collections.unmodifiableList(results);
    }
}
