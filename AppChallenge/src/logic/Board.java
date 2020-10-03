package logic;

/**
 * @author Sam Hooper
 *
 */
public class Board {
	
	public static final int MAX_ROWS = 20;
	public static final int MAX_COLS = 20;
	public static final int MIN_ROWS = 3;
	public static final int MIN_COLS = 3;
	
	private final BoardTile[][] tiles;
	private final int rows, cols;
	
	public Board(int rows, int cols) {
		verifySize(rows, cols);
		this.rows = rows;
		this.cols = cols;
		tiles = new BoardTile[rows][cols];
		initBoardTiles();
	}
	
	/** @throws IllegalArgumentException if the given parameters do not specify a valid size for a board. */
	private static void verifySize(int rows, int cols) {
		if(rows < MIN_ROWS || rows > MAX_ROWS || cols < MIN_COLS || cols > MAX_COLS)
			throw new IllegalArgumentException(String.format("Board must have between %d and %d rows and between %d and %d cols.", 
					MIN_ROWS, MAX_ROWS, MIN_COLS, MAX_COLS));
	}
	
	/**
	 * @return {@code true} if the tile indicated by {@code row} and {@code col} is in this board, {@code false} otherwise.
	 */
	public boolean inBounds(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols; 
	}
	
	private void initBoardTiles() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				tiles[i][j] = new BoardTile(this, TileType.SOLID);
			}
		}
	}
}
