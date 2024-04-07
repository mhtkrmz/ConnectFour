package players;

import interfaces.IModel;

import interfaces.IPlayer;
import game.Model;

/**
 * Implementing this player is an intermediate task.
 * See assignment instructions for what to do.
 * If not attempting it, just upload the file as it is.
 *
 * @author <YOUR UUN>
 */
public class RoundRobinPlayer implements IPlayer {
	int counter = -1;
	// A reference to the model, which you can use to get information about
	// the state of the game. Do not use this model to make any moves!
	private IModel model;

	// The constructor is called when the player is selected from the game menu.
	public RoundRobinPlayer() {
		// You can leave this empty.
	}

	public void prepareForGameStart(IModel model, byte playerId) {
		this.model = model;
		// Extend this method if required.
	}

	public int chooseMove() {
		return chooseMoveRecursive(0);
	}

	private int chooseMoveRecursive(int counter) {
		if (counter >= 0 && counter < Model.board.size() && Model.board.get(counter).equals("_")) {
			return counter;
		} else if (counter == 6) {
			return 6;
		} else {
			counter++;
			return chooseMoveRecursive(counter);
		}
	}
}