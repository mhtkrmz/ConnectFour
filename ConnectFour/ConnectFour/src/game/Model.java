package game;

import java.util.ArrayList;


import java.util.List;

import java.util.*;

import interfaces.IModel;
import util.GameSettings;

/**
 * This class represents the state of the game.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <YOUR UUN>
 */
public class Model implements IModel
{
	public static final int ROWS = 6; // Adjust this according to your game
	public static final int COLS = 7; // Adjust this according to your game

	int flag;
	public static List<String> board = new ArrayList<>();
	static int playertoken;
	private GameSettings settings;
	public Model cloneModel() {
		Model clone = new Model();
		clone.board = new ArrayList<>(this.board);
		clone.playertoken = this.getActivePlayer();
		clone.settings = this.settings;
		return clone;
	}


	public Model()
	{
		// You probably won't need this.
	}

	// A constructor that takes another instance of the same type as its parameter.
	// This is called a copy constructor.
	public Model(IModel model)
	{
		// You may (or may not) find this useful for advanced tasks.
	}

	// Called when a new game is started on an empty board.
	public void initNewGame(GameSettings settings)
	{
		this.settings = settings;
		board = new ArrayList<>();
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 6; i++) {

				board.add("_");

			}
		}
		playertoken = 1;
		// This method still needs to be extended.
	}

	// Called when a game state should be loaded from the given file.
	public void initSavedGame(String fileName)
	{
		// This is an advanced feature. If not attempting it, you can ignore this method.
	}

	// Returns whether or not the passed in move is valid at this time.
	public boolean isMoveValid(int move)
	{
		// Assuming all moves are valid.
		if (move >= 0 && move <= 6) {
			if(board.get(move) == "_") {
				return true;
			}
		}
		if(move == -1) {
			return true;
		}
		return false;
	}

	// Actions the given move if it is valid. Otherwise, does nothing.
	public void makeMove(int move)
	{
		flag = 0;
		if (move == -1) {
			flag = 1;
		}
		if(flag == 0) {
			int mover = getActivePlayer();
			if (mover == 1) {
				if(isMoveValid(move)) {
					for (int counter = (board.size() - 1) - (6 - move); counter >= 0; counter = counter - 7) {
						if (board.get(counter).equals("_")) {
							board.set(counter, "X");
							break;
						}
					}
				}
			}
			else {
				if(isMoveValid(move)) {
					for (int counter = (board.size() - 1) - (6 - move); counter >= 0; counter = counter - 7) {
						if (board.get(counter).equals("_")) {
							board.set(counter, "O");
							break;
						}
					}
				}
			}
			if (playertoken == 1) {
				playertoken = 2;
			}
			else {
				playertoken = 1;
			}
		}
		else {

		}
	}

	// Returns one of the following codes to indicate the game's current status.
	// IModel.java in the "interfaces" package defines constants you can use for this.
	// 0 = Game in progress
	// 1 = Player 1 has won
	// 2 = Player 2 has won
	// 3 = Tie (board is full and there is no winner)
	public byte getGameStatus()
	{

		// Assuming the game is never ending.
		int nonemptycounter = 0;
		int winner = 0;
		for(int counter = 0; counter < board.size(); counter++) {
			if(board.get(counter) != "_") {
				nonemptycounter++;
			}
		}
		for(int counter = 0; counter < board.size() - 3; counter++) {
			if(board.get(counter) == "X" && board.get(counter+1) == "X" && board.get(counter+2) == "X" && board.get(counter+3) == "X") {
				winner = 1;
				break;
			}
			else if(board.get(counter) == "O" && board.get(counter+1) == "O" && board.get(counter+2) == "O" && board.get(counter+3) == "O") {
				winner = 2;
				break;
			}
		}

		for(int counter = 0; counter < (board.size() - 3*7); counter++) {
			if(board.get(counter) == "X" && board.get(counter+7) == "X" && board.get(counter+14) == "X" && board.get(counter+21) == "X") {
				winner = 1;
				break;
			}
			else if(board.get(counter) == "O" && board.get(counter+7) == "O" && board.get(counter+14) == "O" && board.get(counter+21) == "O") {
				winner = 2;
				break;
			}
		}

		for(int counter = 0; counter < board.size(); counter++) {
			if( counter + 24 < board.size()) {
				if(board.get(counter) == "X" && board.get(counter+8) == "X" && board.get(counter+16) == "X" && board.get(counter+24) == "X") {
					winner = 1;
					break;
				}
			}
			if(counter - 18 >= 0 ) {
				if(board.get(counter) == "X" && board.get(counter-6) == "X" && board.get(counter-12) == "X" && board.get(counter-18) == "X") {
					winner = 1;
					break;
				}
			}
			if( counter + 24 < board.size()) {
				if(board.get(counter) == "O" && board.get(counter+8) == "O" && board.get(counter+16) == "O" && board.get(counter+24) == "O") {
					winner = 2;
					break;
				}
			}
			if(counter - 18 >= 0 ) {
				if(board.get(counter) == "O" && board.get(counter-6) == "O" && board.get(counter-12) == "O" && board.get(counter-18) == "O") {
					winner = 2;
					break;
				}
			}
		}
		if(flag == 1) {
			if(playertoken == 2) {
				winner = 1;
				flag = 0;
			}
			else {
				winner = 2;
				flag = 0;
			}
		}
		if(winner != 0) {

			if(winner == 1) {
				board = new ArrayList<>();
				playertoken = 0;
				return IModel.GAME_STATUS_WIN_1;
			}
			else {
				board = new ArrayList<>();
				playertoken = 0;
				return IModel.GAME_STATUS_WIN_2;
			}
		}
		else {
			if(nonemptycounter == board.size()){
				board = new ArrayList<>();
				playertoken= 0;
				return  IModel.GAME_STATUS_TIE;
			}
			else {
				return IModel.GAME_STATUS_ONGOING;
			}

		}
	}

	// Returns the number of the player whose turn it is.
	public byte getActivePlayer()
	{
		// Assuming it is always the turn of player 1.
		if (playertoken == 1) {
			return 1;
		}
		else {
			return 2;
		}
	}

	// Returns the owner of the piece in the given row and column on the board.
	// Return 1 or 2 for players 1 and 2 respectively or 0 for empty cells.
	public byte getPieceIn(int row, int column)
	{
		int index = row * 7;
		index = index + column;
		String location = board.get(index);
		if (location.equals("X")) {
			return 1;
		}
		else if (location.equals("O")) {
			return 2;
		}
		else {
			return 0;
		}
	}

	// Returns a reference to the game settings, from which you can retrieve the
	// number of rows and columns the board has and how long the win streak is.
	public GameSettings getGameSettings()
	{
		return settings;
	}

	// =========================================================================
	// ================================ HELPERS ================================
	// =========================================================================

	// You may find it useful to define some helper methods here.

}