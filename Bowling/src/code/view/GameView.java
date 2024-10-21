package code.view;

import code.model.BowlingGame;
import code.model.Player;
import code.model.Roll;
import code.model.RoundResult;
import code.util.BufferedReaderWrapper;
import code.util.BufferedWriterWrapper;
import code.util.ReaderSingletonHelper;
import code.util.WriterSingletonHelper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static code.view.ViewComponent.*;

public class GameView {
    private final BufferedReaderWrapper reader;
    private final BufferedWriterWrapper writer;

    private GameView(BufferedReaderWrapper reader, BufferedWriterWrapper writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static GameView init() {
        return new GameView(
                ReaderSingletonHelper.getInstance(),
                WriterSingletonHelper.getInstance());
    }

    public Player askPlayer() {
        try {
            writer.printSingleLine("플레이어의 이름을 입력해주세요 : ");
            String name = reader.readLine();
            return Player.of(name);
        } catch (RuntimeException e) {
            System.out.println("이름 입력중 문제가 발생했습니다. 기본 이름으로 설정합니다 : Player1");
            return Player.of("Player1");
        }
    }

    public int askStruckPins(int round, int iter, String player) {
        try {
            String question =
                    round+"라운드 "+iter +"차 투구 "+ player+"님의 턴입니다.\n"+
                    "쓰러트린 핀의 수를 입력하세요. : ";
            writer.printSingleLine(question);
            String struck = reader.readLine();
            return Integer.parseInt(struck);
        } catch (RuntimeException e) {
            System.out.println("입력중 문제가 발생했습니다. 기본 설정 0으로 입력됩니다.");
            return 0;
        }
    }

    public void printScoreTemplate(BowlingGame game) {
        // TODO
        StringBuilder sb = new StringBuilder();
        sb.append(PLAYER + " : ").append(game.playerName()).append("\t \t \t")
                .append(MAX_SCORE).append(" : ").append("100").append("\n");

        sb.append(ROW_DIVIDER).append("\n");
        sb.append(ROUND).append(" | ");
        for (int round = 1; round <= 10; round++) {
            if (round < 10) {
                sb.append("  ").append(round).append("   | ");
            } else {
                sb.append(" ").append(round).append("   | ").append("\n");
            }
        }
        sb.append(ROW_DIVIDER).append("\n");
        sb.append(FRAME + " | ");
        List<RoundResult> results = game.results();
        for (int round = 0; round < 10; round++) {
            if (round+1 > results.size()) {
                sb.append("      | ");
            } else {
                RoundResult result = results.get(round);
                String frames = result.rolls().stream()
                        .map(Roll::toString)
                        .collect(Collectors.joining(" "));
                switch (frames.length()) {
                    case 1 -> sb.append("  ").append(frames).append("  ");
                    case 3 -> sb.append(" ").append(frames).append(" ");
                    case 5 -> sb.append(frames);
                }
                sb.append(" | ");
            }
        }
        sb.append("\n");
        sb.append(ROW_DIVIDER).append("\n");
        sb.append(SCORE + " | ");
        int sum = 0;
        for (int round = 0; round < 10; round++) {
            if (round + 1 > results.size()) {
                sb.append("      | ");
            } else {
                RoundResult result = results.get(round);
                String score;
                if (result.scoreCalculated()) {
                    sum += result.score();
                    score = String.valueOf(sum);
                } else score = " ";
                switch (score.length()) {
                    case 1 -> sb.append("  ").append(score).append("  ");
                    case 2 -> sb.append(" ").append(score).append("  ");
                    case 3 -> sb.append(" ").append(score).append(" ");
                }
                sb.append(" | ");
            }
        }
        sb.append("\n");
        sb.append(ROW_DIVIDER).append("\n").append("\n");
        writer.printSingleLine(sb.toString());
    }

    private List<String> getFrames(BowlingGame game) {
        return game.results().stream()
                .map(RoundResult::rolls)
                .flatMap(Collection::stream)
                .map(Roll::toString)
                .toList();
    }
}
