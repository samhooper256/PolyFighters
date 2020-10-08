package logic;

import java.util.Collection;
import java.util.Objects;

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
	
	/** Creates a new {@code Board} with {@code size} rows and {@code size} columns. All of the tiles on the board will be empty, solid tiles
	 * by default.*/
	public Board(int size) {
		this(size, size);
	}
	
	/** Creates a new {@code Board} with the given amount of rows and columns. All of the tiles on the board will be empty, solid tiles
	 * by default. */
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
				tiles[i][j] = new BoardTile(i, j, this, TileType.SOLID);
			}
		}
	}
	
	/**
	 * Returns the legal spots that the given {@link TeamUnit} currently has for the given {@link Ability} as a {@link Collection}
	 * (row, col) ordered pairs.
	 * @param unit
	 * @param ability
	 * @return
	 * @throws IllegalArgumentException if the given {@code TeamUnit} is not on this board.
	 */
	public Collection<int[]> getLegalSpotsFor(TeamUnit unit, Ability ability) {
		if(unit.getBoard() != this)
			throw new IllegalArgumentException("The unit " + unit + " is not on this board.");
		return unit.getLegalSpots(ability);
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public BoardTile getTileAt(int row, int col) {
		return tiles[row][col];
	}
	
	/**
	 * Returns the {@link Unit} at the indicated tile, or {@code null} if no {@code Unit} is on that tile.
	 */
	public Unit getUnitAt(int row, int col) {
		return tiles[row][col].getUnit();
	}
	
	/**
	 * Returns {@code true} if there is a {@link Unit} on the indicated tile, {@code false} otherwise.
	 */
	public boolean hasUnit(int row, int col) {
		return tiles[row][col].hasUnit();
	}
	
	/**
	 * Adds the given {@link Unit} to this {@code Board} at the given location, updating the {@code Unit}'s associated {@code Board} and
	 * row and column values as necessary.
	 * @throws IllegalStateException if there is already a {@code Unit} on the indicated tile.
	 */
	public void addUnit(Unit unit, int row, int col) {
		if(hasUnit(row, col))
			throw new IllegalStateException(String.format("There is already a unit on the tile at (%d, %d)", row, col));
		setUnit(unit, row, col);
	}
	
	/**
	 * Sets the {@link BoardTile} at the indicated location to have the given {@link Unit}, updating the {@code Unit}'s associated {@code Board} and
	 * row and column values as necessary. Replaces any existing unit on the given tile.
	 * @param unit
	 * @param row
	 * @param col
	 */
	public void setUnit(Unit unit, int row, int col) {
		unit.setBoard(this);
		unit.setRow(row);
		unit.setCol(col);
		tiles[row][col].removeUnitIfPresent();
		tiles[row][col].addUnitOrThrow(unit);
	}
	
	/**
	 * Adds the given {@link Obstacle} to this {@code Board}. Updates the row, col, and board
	 * pointers of the {@code Obstacle} as necessary.
	 */
	public void addObstacle(Obstacle obstacle, int row, int col) {
		obstacle.setBoard(this);
		obstacle.setRow(row);
		obstacle.setCol(col);
		tiles[row][col].addObstacleOrThrow(obstacle);
	}
	
	/**
	 * <p>Returns {@code true} if {@code unit} is on this {@code Board}, {@code false} otherwise. {@code unit} must not be {@code null}.</p>
	 */
	public boolean isOnBoard(Unit unit) {
		Objects.requireNonNull(unit);
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				if(unit.equals(tiles[i][j].getUnit()))
					return true;
			}
		}
		return false;
	}
}
