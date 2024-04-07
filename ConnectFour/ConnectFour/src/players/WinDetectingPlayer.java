package players;

import interfaces.IModel;
import game.Model;
import interfaces.IPlayer;
import util.GameSettings;

/**
 * Implementing this player is an advanced task.
 * See assignment instructions for what to do.
 * If not attempting it, just upload the file as it is.
 *
 * @author <YOUR UUN>
 */
public class WinDetectingPlayer implements IPlayer {
	// A reference to the model, which you can use to get information about
	// the state of the game. Do not use this model to make any moves!
	private IModel model;
	private byte playerId;

	// The constructor is called when the player is selected from the game menu.
	public WinDetectingPlayer() {
		// You may (or may not) need to perform some initialization here.
	}

	// This method is called when a new game is started or loaded.
	// You can use it to perform any setup that may be required before
	// the player is asked to make a move. The second argument tells
	// you if you are playing as player 1 or player 2.
	public void prepareForGameStart(IModel model, byte playerId) {
		this.model = model;
		this.playerId = playerId;
	}

	public int chooseMove() {
		int boardSize = Model.board.size();
		char playerSymbol = (playerId == 1) ? 'X' : 'O';
		char opponentSymbol = (playerSymbol == 'X') ? 'O' : 'X';

		// Check for a winning move for the current player
		for (int move = 0; move < boardSize; move++) {
			if (Model.board.get(move).equals("_")) {
				Model.board.set(move, Character.toString(playerSymbol));
				if (isWinningMove(move, playerSymbol)) {
					Model.board.set(move, "_");
					return move;
				}
				Model.board.set(move, "_");
			}
		}

		// Check for a move that prevents the opponent from winning
		for (int move = 0; move < boardSize; move++) {
			if (Model.board.get(move).equals("_")) {
				Model.board.set(move, Character.toString(opponentSymbol));
				if (isWinningMove(move, opponentSymbol)) {
					Model.board.set(move, "_");
					return move;
				}
				Model.board.set(move, "_");
			}
		}

		// If no winning or preventing move is found, concede.
		return IModel.CONCEDE_MOVE;
	}

	private boolean isWinningMove(int move, char symbol) {
		int row = move / Model.COLS;
		int col = move % Model.COLS;

		// Check row
		for (int i = 0; i < Model.COLS; i++) {
			if (Model.board.get(row * Model.COLS + i).charAt(0) != symbol) {
				return false;
			}
		}

		// Check column
		for (int i = 0; i < Model.ROWS; i++) {
			if (Model.board.get(i * Model.COLS + col).charAt(0) != symbol) {
				return false;
			}
		}

		// Check diagonal
		if (row == col || row + col == Model.COLS - 1) {
			boolean diagonal1 = true;
			boolean diagonal2 = true;
			for (int i = 0; i < Model.COLS; i++) {
				if (Model.board.get(i * Model.COLS + i).charAt(0) != symbol) {
					diagonal1 = false;
				}
				if (Model.board.get(i * Model.COLS + (Model.COLS - 1 - i)).charAt(0) != symbol) {
					diagonal2 = false;
				}
			}
			if (diagonal1 || diagonal2) {
				return true;
			}
		}

		return false;
	}
}
