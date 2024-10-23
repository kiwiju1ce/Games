package code.model;

import code.NextFrameDto;
import code.rule.ScoreCalculator;
import code.rule.FinalFrameRule;
import code.rule.OrdinaryFrameRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static code.constant.BowlingConstant.MAX_ROUNDS;

public class BowlingGame {
    private final Player player;
    private final OrdinaryFrameRule ordinaryRule = new OrdinaryFrameRule();
    private final FinalFrameRule finalRule = new FinalFrameRule();
    private final List<RoundResult> results = new ArrayList<>();
    private final ScoreCalculator calculator = new ScoreCalculator();
    private Score maxScore = Score.noValue();
    private int round;

    private BowlingGame(Player player, int round) {
        this.player = player;
        this.round = round;
    }

    public static BowlingGame of(Player player) {
        return new BowlingGame(player, 1);
    }

    public NextFrameDto getNextTurn() {
        if (canContinueLatestFrame()) {
            return new NextFrameDto(
                    round, getProcessableLatestResults().get().iter(), playerName()
            );
        } else {
            return new NextFrameDto(
                    round, 1, playerName()
            );
        }
    }

    public void updateRoll(int struck) {
        RoundResult latest;
        if (canContinueLatestFrame()) {
            latest = getProcessableLatestResults().get();
            latest.thrown(struck);
        } else {
            latest = round == MAX_ROUNDS ?
                    new RoundResult(finalRule) : new RoundResult(ordinaryRule);
            latest.thrown(struck);
            results.add(latest);
        }

        if (!latest.canProcess()) {
            round++;
        }

        updateScore();
        updateMaxScore();
    }

    public boolean completed() {
        return round == MAX_ROUNDS+1 && !canContinueLatestFrame();
    }


    private boolean canContinueLatestFrame() {
        Optional<RoundResult> latest = getProcessableLatestResults();
        return latest.isPresent() && latest.get().canProcess();
    }

    private Optional<RoundResult> getProcessableLatestResults() {
        return results.size() < round ? Optional.empty() : Optional.of(results.get(round - 1));
    }

    private void updateScore() {
        for (int idx = 0; idx < results.size(); idx++) {
            if (!results.get(idx).scoreCalculated()) {
                Optional<Score> score = calculator.updatedScore(results(), idx);
                if (score.isPresent()) {
                    results.get(idx).setScore(score.get());
                } else break;
            }
        }
    }

    private void updateMaxScore() {
        maxScore = calculator.updateMaxScore(results());
    }

    public String playerName() {
        return player.getName();
    }

    public List<RoundResult> results() {
        return Collections.unmodifiableList(results);
    }

    public Integer maxScore() {
        return maxScore.value();
    }

    public int round() {
        return round;
    }
}
