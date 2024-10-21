import code.GameManager;
import code.exception.ExitFlagException;

public class Application {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.setEnvironment();

        try {
            gameManager.start();
        } catch (ExitFlagException e) {
            // 처리 x
        } finally {
            System.out.println("게임을 종료합니다.");

        }
    }
}
