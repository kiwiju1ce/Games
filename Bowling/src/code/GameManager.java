package code;

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
            int struck = view.askStruckPins(
                    next.round(), next.iter(), next.playerName());
            game.updateRoll(struck);
            view.printScoreTemplate(game);
            if (next.round() == MAX_ROUNDS+1) {

                break;
            }
        }
    }


}
