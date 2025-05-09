package Minesweeper;

//Dieu khien game

public class GameController {
	private final MineBoard board;
	private final GameUI ui;

	public GameController(MineBoard board, GameUI ui) {
		this.board = board;
		this.ui = ui;
	}

	public void startGame() {
		board.initializeBoard();
		board.setMines();
		ui.setupBoard();
		ui.startTimer();
	}

}
