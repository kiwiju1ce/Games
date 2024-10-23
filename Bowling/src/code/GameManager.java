package code;

import code.exception.InvalidPinsException;
import code.model.BowlingGame;
import code.model.Player;
import code.view.GameView;

import static code.constant.BowlingConstant.MAX_ROUNDS;

public class GameManager {
    private BowlingGame game;
    private GameView view;

    public void setEnvironment() {
        this.view = GameView.init();
        Player player = view.askPlayer();
        this.game = BowlingGame.of(player);
    }

    public void start() {
        while (!game.completed()) {
            NextFrameDto next = game.getNextTurn();
            askResultAndRecord(next);
            view.printScoreTemplate(game);
            if (next.round() == MAX_ROUNDS+1) {
                break;
            }
        }
    }

    private void askResultAndRecord(NextFrameDto next) {
        int struck = view.askStruckPins(
                next.round(), next.iter(), next.playerName());
        try {
            game.updateRoll(struck);
        } catch (InvalidPinsException e) {
            view.noteInvalidInput();
            askResultAndRecord(next);
        }
    }


}
