package code.rule;

import code.model.Frame;
import code.model.Roll;
import code.model.RoundResult;
import code.model.Score;

import java.util.List;
import java.util.Optional;

import static code.constant.BowlingConstant.*;
import static code.model.Frame.STRIKE;

public class ScoreCalculator {
    public Optional<Score> updatedScore(final List<RoundResult> results, int roundIdx) {
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

    // 나머지 모두 스트라이크/스페어로 채워서 점수 계산하는 방식은
    // 도메인 모델이 아닌 calculator 가 results 를 핸들링해야 하기 때문에 불가
    // 따라서 각 라운드 결과가 최대 점수 대비 놓친 점수를 합하는 방식으로 구현함
    public Score updateMaxScore(final List<RoundResult> results) {
        int totalLostScore = 0;
        for (int round = 1; round <= results.size(); round++) {
            RoundResult result = results.get(round - 1);
            if (result.scoreCalculated()) {
                totalLostScore+= MAX_FRAME_SCORE - result.score();
                continue;
            }
            totalLostScore +=
                    switch (round) {
                        case 1 -> lostScoreInFirst(results);
                        case 10 -> lostScoreInFinal(results);
                        default -> lostScoreInOrdinary(results, round-1);
                    };
        }
        return new Score(MAX_SCORE - totalLostScore);
    }

    // 첫번째 라운드에 점수가 결정되지 않은 경우는 스트라이크나 스페어 처리한 경우이거나
    // 한번의 투구로 모든 핀을 쓰러트리지 못한 경우(오픈)임
    // 이 때, 진행중인 라운드는 모두 스페어 처리를 가정하여 최대 점수를 계산하므로
    // 스트라이크가 아니면 모두 스페어가 될 것이라 가정하고 계산해도 문제가 없음
    private int lostScoreInFirst(List<RoundResult> results) {
        RoundResult firstRound = results.getFirst();
        Frame firstFrame = firstRound.rolls().getFirst().frame();
        if (firstFrame == STRIKE) return 0;
        return 10;
    }

    // 1. 예상 최대 점수에 변동이 생기는 상황은 스트라이크를 하지 못했을 때임
    // 이번 라운드에서 스트라이크를 하지 못한 순간 기댓값이 10점 떨어짐
    //
    // 2. 현재 라운드에 대한 대처는 첫 라운드와 동일하지만 이전 두 라운드에 대한 점수 계산도 포함되어야함
    // 하지만 해당 라운드에서 투구를 최소 1회 이상 한 상태이기 때문에 두 라운드 전 상태는 고려할 필요가 없음
    // 전 라운드의 점수가 영향 받는 경우는 스트라이크를 던졌을 경우 뿐인데, (스페어면 이미 점수가 계산됨)
    // 전 라운드의 기대점수 역시 스트라이크를 하지 못한 순간 10점 떨어짐
    private int lostScoreInOrdinary(List<RoundResult> results, int roundIdx) {
        int total = 0;
        RoundResult currentRound = results.get(roundIdx);
        Frame currentFrame = currentRound.rolls().getLast().frame();

        RoundResult formerRound = results.get(roundIdx-1);
        Frame lastFrame = formerRound.rolls().getLast().frame();
        if (currentFrame != STRIKE) {
             if (lastFrame == STRIKE && !currentFrame.struckAllPins()) {
                total += 10;
             }
             total += 10;  // 스트라이크를 하지 못했으므로 기본 기대 점수 -10점
        }
        return total;
    }

    // 파이널 라운드에서 고려할 사항은 두가지, 이전 라운드와 현재 프레임들임
    // 점수가 나지 않은 상황에서, 가장 최근 프레임이 스트라이크인 경우는 놓친 점수가 없음
    // 2회의 투구로 스페어 처리된 경우는 놓친 점수가 10임
    // 1회만 투구하여 스트라이크 처리를 하지 못했을 경우엔 기본적으로 놓친 점수가 10이며,
    // 이전 라운드가 스트라이크라면 이에 대한 기댓값도 10점 놓쳐 총 20점 손실이 일어남
    private int lostScoreInFinal(List<RoundResult> results) {
        int total = 0;
        RoundResult finalRound = results.get(MAX_ROUNDS - 1);
        Roll currentRoll = finalRound.rolls().getLast();
        if (currentRoll.frame() == STRIKE) return total;

        if (finalRound.rolls().size() <= 1) {  // 첫 투구가 스트라이크가 아닌 경우
            RoundResult formerRound = results.get(8);
            Frame lastFrame = formerRound.rolls().getLast().frame();
            if (lastFrame == STRIKE) {
                total += 10;
            }
        }
        return total + 10;
    }
}
