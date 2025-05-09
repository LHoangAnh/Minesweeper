package Minesweeper;

//Giao dien chinh

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GameUI {
	private JFrame frame;
	private JLabel textLabel;
	private JPanel boardPanel;
	private final MineBoard board;
	private boolean gameOver = false;
	private int tilesClicked = 0;
	private JButton resetButton;
	private JLabel timerLabel;
	private Timer timer;
	private int elapsedSeconds = 0;

	public GameUI(MineBoard board) {
		this.board = board;
		setupFrame();
	}

	private void setupFrame() {
		frame = new JFrame("Minesweeper");
		frame.setSize(board.getNumCols() * 70, board.getNumRows() * 70);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		textLabel = new JLabel("Minesweeper: " + board.getMineCount(), SwingConstants.CENTER);
		textLabel.setFont(new Font("Arial", Font.BOLD, 25));
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(textLabel, BorderLayout.CENTER);
		frame.add(textPanel, BorderLayout.NORTH);

		boardPanel = new JPanel(new GridLayout(board.getNumRows(), board.getNumCols()));
		boardPanel.setBackground(Color.gray);
		frame.add(boardPanel, BorderLayout.CENTER);

		textLabel = new JLabel("Minesweeper: " + board.getMineCount(), SwingConstants.CENTER);
		textLabel.setFont(new Font("Arial", Font.BOLD, 25));

		timerLabel = new JLabel("Time: 0s", SwingConstants.RIGHT);
		timerLabel.setFont(new Font("Arial", Font.PLAIN, 18));

		resetButton = new JButton("Reset");
		resetButton.setFont(new Font("Arial", Font.PLAIN, 25));
		resetButton.addActionListener(e -> resetGame());

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(resetButton, BorderLayout.WEST);
		topPanel.add(textLabel, BorderLayout.CENTER);
		topPanel.add(timerLabel, BorderLayout.EAST);
		frame.add(topPanel, BorderLayout.NORTH);
		resetButton.setFocusPainted(false);
		resetButton.setBorderPainted(false);
		resetButton.setBackground(Color.LIGHT_GRAY);

	}

	public void setupBoard() {
		MineTile[][] tiles = board.getBoard();
		for (int r = 0; r < board.getNumRows(); r++) {
			for (int c = 0; c < board.getNumCols(); c++) {
				MineTile tile = tiles[r][c];
				tile.setFocusable(false);
				tile.setMargin(new Insets(0, 0, 0, 0));
				tile.setFont(new Font("Arial Unicode Ms", Font.PLAIN, 45));
				boardPanel.add(tile);

				tile.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (gameOver)
							return;

						if (e.getButton() == MouseEvent.BUTTON1) {
							if (tile.getText().equals("")) {
								if (board.isMine(tile)) {
									revealMines();
								} else {
									checkMine(tile.getRow(), tile.getCol());
								}
							}
						} else if (e.getButton() == MouseEvent.BUTTON3) {
							if (tile.getText().equals("") && tile.isEnabled()) {
								tile.setText("🚩");
							} else if (tile.getText().equals("🚩")) {
								tile.setText("");
							}
						}
					}
				});
			}
		}
		frame.setVisible(true);
	}

	private void revealMines() {
		for (MineTile tile : board.getMineList()) {
			tile.setText("💣");
		}
		gameOver = true;
		textLabel.setText("Game Over!");
		timer.stop();
	}

	private void checkMine(int r, int c) {
		if (r < 0 || r >= board.getNumRows() || c < 0 || c >= board.getNumCols())
			return;

		MineTile tile = board.getTile(r, c);
		if (!tile.isEnabled())
			return;

		tile.setEnabled(false);
		tilesClicked++;

		int minesFound = 0;
		// top 3
		minesFound += countMine(r - 1, c - 1); // top left
		minesFound += countMine(r - 1, c); // top
		minesFound += countMine(r - 1, c + 1); // top right
		// left and right
		minesFound += countMine(r, c - 1); // left
		minesFound += countMine(r, c + 1); // right
		// bottom
		minesFound += countMine(r + 1, c - 1); // bottom left
		minesFound += countMine(r + 1, c); // bottom
		minesFound += countMine(r + 1, c + 1); // bottom right

		if (minesFound > 0) {
			tile.setText(Integer.toString(minesFound));
		} else {
			tile.setText("");

			// top 3
			checkMine(r - 1, c - 1); // top left
			checkMine(r - 1, c); // top
			checkMine(r - 1, c + 1); // top right
			// left and right
			checkMine(r, c - 1); // left
			checkMine(r, c + 1); // right
			// bottom 3
			checkMine(r + 1, c - 1); // bottom left
			checkMine(r + 1, c); // bottom
			checkMine(r + 1, c + 1); // bottom right
		}
		if (tilesClicked == board.getNumRows() * board.getNumCols() - board.getMineList().size()) {
			gameOver = true;
			textLabel.setText("Mines Cleared!");
			timer.stop();
		}
	}

	private int countMine(int r, int c) {
		if (r < 0 || r >= board.getNumRows() || c < 0 || c >= board.getNumCols())
			return 0;
		return board.isMine(board.getTile(r, c)) ? 1 : 0;
	}

	private void resetGame() {
		board.getMineList().clear();
		board.setMines();
		tilesClicked = 0;
		gameOver = false;
		textLabel.setText("Minesweeper: " + board.getMineCount());

		MineTile[][] tiles = board.getBoard();
		boardPanel.removeAll();

		for (MineTile[] row : tiles) {
			for (MineTile tile : row) {
				tile.setEnabled(true);
				tile.setText("");
				boardPanel.add(tile);
			}
		}

		elapsedSeconds = 0;
		timerLabel.setText("Time: 0s");
		if (timer != null) {
			timer.stop();
		}
		startTimer();

		frame.revalidate();
		frame.repaint();
	}

	public void startTimer() {
		timer = new Timer(1000, e -> {
			elapsedSeconds++;
			timerLabel.setText("Time: " + elapsedSeconds + "s");
		});
		timer.start();
	}

}
