package code.rule;

import code.model.Frame;
import code.model.Roll;
import code.model.RoundResult;
import code.model.Score;

import java.util.List;
import java.util.Optional;

import static code.constant.BowlingConstant.MAX_PINS;
import static code.constant.BowlingConstant.MAX_ROUNDS;

public class ScoreCalculator {
    public Optional<Score> updatedScore(List<RoundResult> results, int roundIdx) {
        if (results.get(roundIdx).canProcess()) return Optional.empty();

        Score score;
        if (isFinal(roundIdx)) {
            RoundResult result = results.get(roundIdx);
            score = applyFinalRule(result);
        } else {
            score = applyOrdinaryRule(results, roundIdx);
        }

        return Optional.ofNullable(score);
    }

    private boolean isFinal(int round) {
        return round == MAX_ROUNDS -1;
    }

    private Score applyFinalRule(RoundResult result) {
        if (!result.canProcess()) {
            int struckPins = getStruckPins(result.rolls());
            return new Score(struckPins);
        } else return null;
    }

    private Score applyOrdinaryRule(List<RoundResult> results, int roundIdx) {
        RoundResult result = results.get(roundIdx);
        Frame lastFrame = result.rolls().getLast().frame();
        if (lastFrame.needMoreFrames()) {
            return switch (lastFrame) {
                case STRIKE -> checkTwoRolls(results, roundIdx);
                case SPARE -> checkOneRoll(results, roundIdx);
                default -> null;
            };
        } else {
            int struckPins = getStruckPins(result.rolls());
            return new Score(struckPins);
        }
    }

    private Score checkOneRoll(List<RoundResult> results, int roundIdx) {
        if (results.size() > roundIdx + 1) {
            Roll first = results.get(roundIdx + 1).rolls().getFirst();
            return new Score(MAX_PINS + first.struck());
        } else return null;
    }

    private Score checkTwoRolls(List<RoundResult> results, int roundIdx) {
        if (results.size() > roundIdx + 1) {  // 이후 던진 투구가 있다면
            List<Roll> nextRolls = results.get(roundIdx + 1).rolls();
            if (nextRolls.size() < 2) {     // 다음 프레임 투구 수가 2회 미만이라면
                if (results.size() > roundIdx + 2) {  // 다다음 프레임에서 투구를 했다면
                    Roll thirdRoll = results.get(roundIdx + 2).rolls().getFirst();
                    return new Score(2 * MAX_PINS + thirdRoll.struck());
                } else return null;
            } else {                        // 다음 프레임 투구 수가 2회라면
                int struckPins = getStruckPins(nextRolls);
                return new Score(MAX_PINS + struckPins);
            }
        } else return null;
    }

    private int getStruckPins(List<Roll> rolls) {
        return rolls.stream()
                .map(Roll::struck)
                .reduce(0, Integer::sum);
    }

    public Score updateMaxScore(List<RoundResult> results) {
        return null;
    }
}
