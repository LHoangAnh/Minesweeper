package Minesweeper;

public class Main {
	public static void main(String[] args) {
		MineBoard board = new MineBoard(8, 8, 10);
		GameUI ui = new GameUI(board);
		GameController controller = new GameController(board, ui);
		controller.startGame();
	}
}
