package Minesweeper;

//Xu ly logic tro choi

import java.util.ArrayList;
import java.util.Random;

public class MineBoard {
	private MineTile[][] board;
	private ArrayList<MineTile> mineList;
	private int numRows;
	private int numCols;
	private int mineCount;
	private Random random;

	public MineBoard(int rows, int cols, int mines) {
		this.numRows = rows;
		this.numCols = cols;
		this.mineCount = mines;
		board = new MineTile[rows][cols];
		mineList = new ArrayList<>();
		random = new Random();
	}

	public MineTile[][] getBoard() {
		return board;
	}

	public ArrayList<MineTile> getMineList() {
		return mineList;
	}

	public void initializeBoard() {
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				board[r][c] = new MineTile(r, c);
			}
		}
	}

	public void setMines() {
		int minesLeft = mineCount;
		while (minesLeft > 0) {
			int r = random.nextInt(numRows);
			int c = random.nextInt(numCols);
			MineTile tile = board[r][c];
			if (!mineList.contains(tile)) {
				mineList.add(tile);
				minesLeft--;
			}
		}
	}

	public boolean isMine(MineTile tile) {
		return mineList.contains(tile);
	}

	public MineTile getTile(int r, int c) {
		return board[r][c];
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getMineCount() {
		return mineCount;
	}
}
