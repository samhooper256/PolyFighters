package logic;

import java.util.*;
/**
 * @author Sam Hooper
 *
 */
public class Board {
	
	/**
	 * <b>CONTENTS MUST NOT BE MODIFIED.</b>
	 */
	public static final int[][] ADJACENT_8 = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
	
	public static final int MAX_ROWS = 20;
	public static final int MAX_COLS = 20;
	public static final int MIN_ROWS = 3;
	public static final int MIN_COLS = 3;
	public static final int MOVES_PER_ENEMY = 2;
	
	private final BoardTile[][] tiles;
	private final int rows, cols;
	/**
	 * The amount of enemy {@link Move}s that have yet to be played on the Enemy's turn. This value is not used during the Player's turn.
	 */
	private int enemyMovesRemaining;
	/**
	 * The enemies on this {@link Board}. <b>Must ONLY be used during the Enemy's turn and cannot be shared with the outside world.</b>
	 * Should be random access for efficiency.
	 */
	private List<EnemyUnit> enemies;
	private Turn turn;
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
		turn = Turn.PLAYER;
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
	 * Returns the legal spots that the given {@link Unit} currently has for the given {@link Ability} as a {@link Collection}
	 * (row, col) ordered pairs.
	 * @throws IllegalArgumentException if the given {@link Unit} is not on this board.
	 */
	public Collection<int[]> getLegalSpotsFor(Unit unit, Ability ability) {
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
	
	public BoardTile getTileAt(int[] spot) {
		return getTileAt(spot[0], spot[1]);
	}
	
	public BoardTile getTileAt(int row, int col) {
		return tiles[row][col];
	}
	
	/**
	 * Returns the {@link Unit} at the indicated tile, or {@code null} if no {@code Unit} is on that tile.
	 */
	public Unit getUnitAtOrNull(int row, int col) {
		return tiles[row][col].getUnitOrNull();
	}
	
	/**
	 * Returns the {@link Unit} at the indicated tile, or {@code null} if no {@code Unit} is on that tile.
	 */
	public Obstacle getObstacleAtOrNull(int row, int col) {
		return tiles[row][col].getObstacleOrNull();
	}
	
	/**
	 * Returns {@code true} if there is a {@link Unit} on the indicated tile, {@code false} otherwise.
	 */
	public boolean hasUnit(int row, int col) {
		return tiles[row][col].hasUnit();
	}
	
	/**
	 * Returns {@code true} if there is a {@link Obstacle} on the indicated tile, {@code false} otherwise.
	 */
	public boolean hasObstacle(int row, int col) {
		return tiles[row][col].hasObstacle();
	}
	
	
	/**
	 * Adds the given {@link GameObject} to the indicated {@link TerrainTile}, throwing an {@link IllegalStateException} if there is already a {@code GameObject}
	 * of that type on the {@code TerrainTile}. The given {@code GameObject} must be a {@link Unit} or a {@link Obstacle}.
	 */
	public void addOrThrow(GameObject object, int row, int col) {
		if(object instanceof Unit)
			addUnitOrThrow((Unit) object, row, col);
		else if(object instanceof Obstacle)
			addObstacleOrThrow((Obstacle) object, row, col);
		else
			throw new IllegalArgumentException("Must be Unit or Obstacle");
	}
	
	/**
	 * @throws IllegalStateException if it is the {@link Turn#PLAYER player's turn}.
	 */
	public boolean hasNextEnemyMove() {
		if(turn != Turn.ENEMY)
			throw new IllegalStateException("Must be the enemy's turn");
		return enemyMovesRemaining > 0;
	}
	
	public Move nextEnemyMove() {
		System.out.printf("entered nextEnemyMove, enemies = %s%n", enemies);
		EnemyUnit actingEnemy = enemies.get((enemies.size() * MOVES_PER_ENEMY - enemyMovesRemaining) / MOVES_PER_ENEMY);
		int enemyMoves = enemyMovesRemaining % MOVES_PER_ENEMY;
		if(enemyMoves == 0)
			enemyMoves = MOVES_PER_ENEMY;
		enemyMovesRemaining--;
		Move chosenMove = actingEnemy.chooseMove(this, enemyMoves);
		System.out.printf("nextEnemyMove chose: %s%n", chosenMove);
		return chosenMove;
	}
	
	/**
	 * Adds the given {@link Unit} to this {@code Board} at the given location, updating the {@code Unit}'s associated {@code Board} and
	 * row and column values as necessary.
	 * @throws IllegalStateException if there is already a {@code Unit} on the indicated tile.
	 */
	public void addUnitOrThrow(Unit unit, int row, int col) {
		if(hasUnit(row, col))
			throw new IllegalStateException(String.format("There is already a unit on the tile at (%d, %d)", row, col));
		setUnit(unit, row, col);
	}
	
	/**
	 * Adds the given {@link Obstacle} to this {@code Board}. Updates the row, col, and board
	 * pointers of the {@code Obstacle} as necessary.
	 * @throws IllegalStateException if there is already an {@link Obstacle} on the given {@link BoardTile}.
	 */
	public void addObstacleOrThrow(Obstacle obstacle, int row, int col) {
		obstacle.setBoard(this);
		obstacle.setRow(row);
		obstacle.setCol(col);
		tiles[row][col].addObstacleOrThrow(obstacle);
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
	 * Removes the given {@link GameObject} on the given tile from this {@link Board}. Throws an {@link IllegalArgumentException}
	 * if it is not present. Throws {@link NullPointerException} if the object is {@code null}. This method updates the row, column, and board
	 * values of the {@code GameObject}. The row and column values are set to {@code -1}.
	 * @throws NullPointerException if the given {@link GameObject} is {@code null}.
	 * @throws IllegalArgumentException if the given {@link GameObject} is not present on this {@link Board} at the given tile.
	 */
	public void removeGameObject(GameObject object, int row, int col) {
		Objects.requireNonNull(object);
		tiles[row][col].removeObjectOrThrow(object);
		object.setBoard(null);
		object.setRow(-1);
		object.setCol(-1);
	}
	
	public Turn getTurn() {
		return turn;
	}
	
	/**
	 * Sets the {@link #getTurn() turn} of this {@link Board} to the {@link Turn#ENEMY enemy's} and prepares to play the turn. </b>Does not play the turn.</b>
	 */
	public void setToEnemyTurn() {
		System.out.printf("Entered set to enemy Turn%n");
		this.turn = Turn.ENEMY;
		this.enemies = findEnemies();
		this.enemyMovesRemaining = this.enemies.size() * MOVES_PER_ENEMY;
	}
	
	/**
	 * Does an O(rows*cols) search to find all the enemies on this board. Returns an {@link ArrayList} of all the {@link EnemyUnit EnemyUnits}.
	 */
	private ArrayList<EnemyUnit> findEnemies() {
		ArrayList<EnemyUnit> list = new ArrayList<>();
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				Unit unit = tiles[i][j].getUnitOrNull();
				if(unit instanceof EnemyUnit)
					list.add((EnemyUnit) unit);
			}
		}
		return list;
	}
	
	public Collection<TeamUnit> getCurrentTeamUnits() {
		ArrayList<TeamUnit> list = new ArrayList<>();
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				Unit unit = tiles[i][j].getUnitOrNull();
				if(unit instanceof TeamUnit)
					list.add((TeamUnit) unit);
			}
		}
		return list;
	}
	
	/**
	 * <p>Returns {@code true} if {@code unit} is on this {@code Board}, {@code false} otherwise. {@code unit} must not be {@code null}.</p>
	 */
	public boolean isOnBoard(Unit unit) {
		Objects.requireNonNull(unit);
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				if(unit.equals(tiles[i][j].getUnitOrNull()))
					return true;
			}
		}
		return false;
	}
	
	public Collection<GameObject> get8AdjacentGameObject(final int row, final int col) {
		Collection<GameObject> list = new ArrayList<>();
		for(int[] adj : ADJACENT_8) {
			int nr = row + adj[0], nc = col + adj[1];
			if(inBounds(nr, nc))
				list.addAll(tiles[nr][nc].getObjectsUnmodifiable());
		}
		return list;
	}

	@Override
	public String toString() {
		StringJoiner j = new StringJoiner("\n", "\n", "\n");
		j.add("BOARD:");
		for(BoardTile[] row : tiles)
			j.add(Arrays.toString(row));
		return j.toString();
	}
}
