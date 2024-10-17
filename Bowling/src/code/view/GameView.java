package code.view;

import code.BowlingGame;
import code.Player;
import code.util.BufferedReaderWrapper;
import code.util.BufferedWriterWrapper;
import code.util.ReaderSingletonHelper;
import code.util.WriterSingletonHelper;

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

    public void printScoreTemplate(BowlingGame game) {
        // TODO
    }
}
