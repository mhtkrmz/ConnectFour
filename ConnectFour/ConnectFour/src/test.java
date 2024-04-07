import game.Model;
import util.GameSettings;

public class test {
    public static void main(String[] args) {
        Model model = new Model();
        model.initNewGame(new GameSettings());
        model.makeMove(3);
        model.initNewGame(new GameSettings());
        System.out.println("model.getPieceIn(5, 3) = " + model.getPieceIn(5, 3));
    }
}
