package Minesweeper;

//Dien dien cho moi o vuong

import javax.swing.JButton;

public class MineTile extends JButton implements IMineTile {
	private int row;
	private int col;

	public MineTile(int row, int col) {
		this.setRow(row);
		this.setCol(col);
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getCol() {
		return col;
	}

	@Override
	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public void setCol(int col) {
		this.col = col;
	}
}
