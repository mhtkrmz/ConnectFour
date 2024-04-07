package players;

import game.Model;
import interfaces.IModel;
import interfaces.IPlayer;

import java.util.ArrayList;

/**
 * Move Decision: The chooseMove() method decides the next move.
 * It uses the Minimax algorithm with Alpha-Beta pruning to evaluate moves.
 * This algorithm simulates future moves (up to a certain depth) to choose the most advantageous move.
 * The method createHypotheticalModel() is used to clone the current game model for simulation purposes without affecting the actual game state.
 *
 * Minimax Algorithm: The minimax method is a recursive function that evaluates all possible moves up to a specified depth.
 * It uses the Alpha-Beta pruning technique to reduce the number of nodes evaluated in the search tree.
 * The algorithm alternates between maximizing and minimizing players to simulate the opponent's moves and choose the best strategic move.
 */
public class CompetitivePlayer implements IPlayer {
	private IModel model;
	private byte playerId;

	public CompetitivePlayer() {
	}

	public void prepareForGameStart(IModel model, byte playerId) {
		this.model = model;
		this.playerId = playerId;
	}

	private IModel createHypotheticalModel() {
		if (!(model instanceof Model)) {
			throw new IllegalStateException("Model instance is not of expected type");
		}
		return ((Model) model).cloneModel();
	}
	public int chooseMove() {
		int depth = 6;
		IModel hypotheticalModel = createHypotheticalModel();
		int[] result = minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, hypotheticalModel);
		return result[1];
	}
	private int[] minimax(int depth, int alpha, int beta, boolean maximizingPlayer, IModel model) {
		// Handle terminal case
		int bestMove = -1;
		int bestValue = evaluateBoard();

		// Base case: check for terminal condition or depth limit
		if (depth == 0 || isTerminal(bestValue)) {
			return new int[]{bestValue, bestMove};
		}

		bestValue = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (int move = 0; move < model.getGameSettings().nrCols; move++) {
			if (model.isMoveValid(move)) {
				// Simulate the move in a hypothetical model
				IModel hypotheticalModel = simulateMove(model, move, playerId);
				int[] result = minimax(depth - 1, alpha, beta, !maximizingPlayer, hypotheticalModel);

				if (maximizingPlayer) {
					if (result[0] > bestValue) {
						bestValue = result[0];
						bestMove = move;
					}
					alpha = Math.max(alpha, bestValue);
				} else {
					if (result[0] < bestValue) {
						bestValue = result[0];
						bestMove = move;
					}
					beta = Math.min(beta, bestValue);
				}

				if (beta <= alpha) {
					break;
				}
			}
		}

		return new int[]{bestValue, bestMove};
	}

	// Simulates a move on a hypothetical model, returning the new model state
	private IModel simulateMove(IModel currentModel, int move, byte playerId) {
		// Create a copy of the current Model
		Model simulatedModel = new Model(currentModel);

		// Translate playerId to the corresponding symbol
		String playerSymbol = (playerId == 1) ? "X" : "O";

		// Simulate the move
		if (currentModel.isMoveValid(move)) {
			for (int counter = (Model.COLS - 1) - (Model.COLS - move - 1); counter >= 0; counter -= Model.COLS) {
				if (simulatedModel.board.get(counter).equals("_")) {
					simulatedModel.board.set(counter, playerSymbol);
					break;
				}
			}
		}


		return simulatedModel;
	}




	// Determine if a board state is terminal (win, lose, draw)
	private boolean isTerminal(int boardValue) {
		return Math.abs(boardValue) >= 1000; // Adjust based on your scoring system
	}



	private int evaluateBoard() {
		int playerSymbol = (playerId == 1) ? 'X' : 'O';
		int opponentSymbol = (playerSymbol == 'X') ? 'O' : 'X';

		int score = 0;

		// Evaluate horizontal, vertical, and diagonal lines
		for (int row = 0; row < model.getGameSettings().nrRows; row++) {
			for (int col = 0; col < model.getGameSettings().nrCols; col++) {
				score += evaluateLine(row, col, 0, 1, playerSymbol, opponentSymbol); // Horizontal
				score += evaluateLine(row, col, 1, 0, playerSymbol, opponentSymbol); // Vertical
				score += evaluateLine(row, col, 1, 1, playerSymbol, opponentSymbol); // Diagonal Down-Right
				score += evaluateLine(row, col, 1, -1, playerSymbol, opponentSymbol); // Diagonal Down-Left
			}
		}

		return score;
	}

	private int evaluateLine(int row, int col, int rowDirection, int colDirection, int playerSymbol, int opponentSymbol) {
		int playerCount = 0;
		int opponentCount = 0;
		int emptyCount = 0;

		for (int i = 0; i < 4; i++) {
			int newRow = row + i * rowDirection;
			int newCol = col + i * colDirection;

			if (newRow >= 0 && newRow < model.getGameSettings().nrRows &&
					newCol >= 0 && newCol < model.getGameSettings().nrCols) {

				int piece = model.getPieceIn(newRow, newCol);
				if (piece == playerSymbol) {
					playerCount++;
				} else if (piece == opponentSymbol) {
					opponentCount++;
				} else {
					emptyCount++;
				}
			}
		}

		// Evaluation logic
		if (playerCount == 4) {
			return 1000; // Winning line for player
		} else if (opponentCount == 4) {
			return -1000; // Winning line for opponent
		} else if (playerCount == 3 && emptyCount == 1) {
			return 100; // Potential winning move for player
		} else if (opponentCount == 3 && emptyCount == 1) {
			return -100; // Potential winning move for opponent
		} else if (playerCount == 2 && emptyCount == 2) {
			return 10; // Two in a row for player
		} else if (opponentCount == 2 && emptyCount == 2) {
			return -10; // Two in a row for opponent
		} else {
			return 0; // Neutral or no significant advantage
		}
	}
}
