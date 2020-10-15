package logic;

import java.util.*;
import java.util.function.*;
/**
 * @author Sam Hooper
 *
 */
public class Board {
	
	@FunctionalInterface
	public interface TileConsumer extends Consumer<BoardTile> {
		@Override
		void accept(BoardTile t);
	}
	
	@FunctionalInterface
	public interface TilePredicate extends Predicate<BoardTile> {
		@Override
		boolean test(BoardTile tile);
	}
	
	/**
	 * <b>CONTENTS MUST NOT BE MODIFIED.</b>
	 */
	public static final int[][] ADJACENT_8 = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
	
	/**
	 * <b>CONTENTS MUST NOT BE MODIFIED.</b>
	 */
	public static final int[][] ADJACENT_4 = {{-1,0}, {0,1}, {1,0}, {0,-1}};
	
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
	
	private void initBoardTiles() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				tiles[i][j] = new BoardTile(i, j, this, TileType.SOLID);
			}
		}
	}
	
	/**
	 * @return {@code true} if the tile indicated by {@code row} and {@code col} is in this board, {@code false} otherwise.
	 */
	public boolean inBounds(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols; 
	}
	
	public boolean isOccupied(int row, int col) {
		return tiles[row][col].isOccupied();
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
	
	public Unit getUnitAtOrNull(final int[] spot) {
		return getUnitAtOrNull(spot[0], spot[1]);
	}
	/**
	 * Returns the {@link Unit} at the indicated tile, or {@code null} if no {@code Unit} is on that tile.
	 */
	public Unit getUnitAtOrNull(int row, int col) {
		return tiles[row][col].getUnitOrNull();
	}
	
	public Unit getUnitAtOrThrow(int[] spot) {
		return getUnitAtOrThrow(spot[0], spot[1]);
	}
	/**
	 * Returns the {@link Unit} on the indicated tile, or throws an {@link IllegalStateException} if there is none.
	 */
	public Unit getUnitAtOrThrow(int row, int col) {
		Unit u = getUnitAtOrNull(row, col);
		if(u == null)
			throw new IllegalStateException("No unit on the indicated tile.");
		return u;
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
		EnemyUnit actingEnemy = enemies.get((enemies.size() * MOVES_PER_ENEMY - enemyMovesRemaining) / MOVES_PER_ENEMY);
		System.out.printf("entered nextEnemyMove, enemies = %s%n\tactingEnemy=%s%n", enemies, actingEnemy);
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
		this.turn = Turn.ENEMY;
		this.enemies = findEnemies();
		this.enemyMovesRemaining = this.enemies.size() * MOVES_PER_ENEMY;
	}
	
	/**
	 * Sets the {@link #getTurn() turn} of this {@link Board} to the {@link Turn#ENEMY player's} and prepares for the turn to be played.
	 * */
	public void setToPlayerTurn() {
		this.turn = Turn.PLAYER;
		this.enemies = null;
		this.enemyMovesRemaining = -1;
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
	
	public Collection<PlayerUnit> getCurrentTeamUnits() {
		ArrayList<PlayerUnit> list = new ArrayList<>();
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				Unit unit = tiles[i][j].getUnitOrNull();
				if(unit instanceof PlayerUnit)
					list.add((PlayerUnit) unit);
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
	
	/**
	 * Executes the given {@link TileConsumer} on every {@link BoardTile} that is 8-Adjacent to the indicated tile. Only tiles whose row/col
	 * values would be {@link #inBounds(int, int) in bounds} are passed to the consumer. The given row and column values may be out of range for this
	 * {@link Board}. Any 8-Adjacent tiles to the given coordinates will be passed to the {@link TileConsumer}, regardless of whether the starting
	 * location is in bounds.
	 * @throws NullPointerException if the given {@link TileConsumer} is {@code null}.
	 */
	public void for8AdjacentTiles(final int row, final int col, final TileConsumer consumer) {
		forTilesInSquare(row, col, 1, consumer);
	}
	
	public Collection<GameObject> get8AdjacentGameObjects(final int row, final int col) {
		return getGameObjectsInSquare(row, col, 1);
	}
	
	public Collection<Unit> get8AdjacentUnits(final int row, final int col) {
		return getUnitsInSquare(row, col, 1);
	}
	
	public Collection<EnemyUnit> get8AdjacentEnemyUnits(final int row, final int col) {
		return getEnemyUnitsInSquare(row, col, 1);
	}
	
	/**
	 * <pre><code>forTilesInSquare(a, b, c, d)</code></pre>
	 * is equivalent to:
	 * <pre><code>forTilesInSquare(a, b, c, d, false)</code></pre>
	 * (In other words, this method does <b>not</b> consume the start tile that is in the center of the square).
	 */
	public void forTilesInSquare(final int row, final int col, final int radius, final TileConsumer consumer) {
		forTilesInSquare(row, col, radius, consumer, false);
	}
	/**
	 * Executes the given {@link TileConsumer} on every tile surrounding the indicated one in a square with the given radius.
	 * If {@code radius} is 0, this method has no effect. The given starting location need not be {@link #inBounds(int, int) in the bounds}
	 * of this {@link Board}. If {@code includeCenter} is {@code true} the center tile will be consumed, otherwise it will not.
	 * @throws NullPointerException if the given {@link TileConsumer} is {@code null} <b>and</b> the given radius is not {@code 0}.
	 * @throws IllegalArgumentException if the given radius is less than {@code 0}.
	 */
	public void forTilesInSquare(final int row, final int col, final int radius, final TileConsumer consumer, boolean includeCenter) {
		if(radius == 0)
			return;
		Objects.requireNonNull(consumer);
		if(radius < 0)
			throw new IllegalArgumentException("Radius must be >= 0");
		for(int i = row - radius; i <= row + radius; i++) {
			for(int j = col - radius; j <= col + radius; j++) {
				if(!inBounds(i, j) || !includeCenter && i == row && j == col)
					continue;
				consumer.accept(tiles[i][j]);
			}
		}
	}
	
	/**
	 * Does not include the starting tile.
	 */
	public Collection<GameObject> getGameObjectsInSquare(final int row, final int col, final int radius) {
		Collection<GameObject> list = new ArrayList<>();
		forTilesInSquare(row, col, radius, tile -> list.addAll(tile.getObjectsUnmodifiable()));
		return list;
	}
	
	/**
	 * Does not include the starting tile.
	 */
	public Collection<Unit> getUnitsInSquare(final int row, final int col, final int radius) {
		Collection<Unit> list = new ArrayList<>();
		forTilesInSquare(row, col, radius, tile -> list.addAll(tile.getUnitsUnmodifiable()));
		return list;
	}
	
	/**
	 * Does not include the starting tile.
	 */
	public Collection<PlayerUnit> getPlayerUnitsInSquare(final int row, final int col, final int radius) {
		Collection<PlayerUnit> list = new ArrayList<>();
		forTilesInSquare(row, col, radius, tile -> {
			Unit u = tile.getUnitOrNull();
			if(u instanceof PlayerUnit)
				list.add((PlayerUnit) u);
		});
		return list;
	}
	
	/**
	 * Does not include the starting tile.
	 */
	public Collection<EnemyUnit> getEnemyUnitsInSquare(final int row, final int col, final int radius) {
		Collection<EnemyUnit> list = new ArrayList<>();
		forTilesInSquare(row, col, radius, tile -> {
			Unit u = tile.getUnitOrNull();
			if(u instanceof EnemyUnit)
				list.add((EnemyUnit) u);
		});
		return list;
	}
	
	/**
	 * <pre><code>anyInSquare(a, b, c)</code></pre>
	 * is equivalent to:
	 * <pre><code>anyInSquare(a[0], a[1], b, c)</code></pre>
	 */
	public boolean anyInSquare(final int[] spot, final int radius, final TilePredicate consumer) {
		return anyInSquare(spot[0], spot[1], radius, consumer);
	}
	
	/**
	 * <pre><code>anyInSquare(a, b, c, d)</code></pre>
	 * is equivalent to:
	 * <pre><code>anyInSquare(a, b, c, d, false)</code></pre>
	 * (In other words, this method does <b>not</b> test the start tile that is in the center of the square).
	 */
	public boolean anyInSquare(final int row, final int col, final int radius, final TilePredicate consumer) {
		return anyInSquare(row, col, radius, consumer, false);
	}
	
	/**
	 * Returns {@code true} if any of the {@link BoardTile BoardTiles} in a square of the given radius around the given center
	 * satisfies the given {@link TilePredicate}, {@code false} otherwise. Returns {@code false} if the given radius is {@code 0}.
	 * The center need not be {@link #inBounds(int, int) in the bounds} of this {@link Board}.
	 * @throws NullPointerException if the given {@link TilePredicate} if {@code null} <b>and</b> the given radius is not zero.
	 * @throws IllegalArgumentException if the given radius is less than {@code 0}.
	 */
	public boolean anyInSquare(final int row, final int col, final int radius, TilePredicate predicate, boolean includeCenter) {
		if(radius == 0)
			return false;
		Objects.requireNonNull(predicate);
		if(radius < 0)
			throw new IllegalArgumentException("Radius must be >= 0");
		for(int i = row - radius; i <= row + radius; i++) {
			for(int j = col - radius; j <= col + radius; j++) {
				if(!inBounds(i, j) || !includeCenter && i == row && j == col)
					continue;
				if(predicate.test(tiles[i][j]))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * <pre><code>countSatisfyingInSquare(a, b, c)</code></pre>
	 * is equivalent to:
	 * <pre><code>countSatisfyingInSquare(a[0], b[1], c, d)</code></pre>
	 * (In other words, this method does <b>not</b> count the start tile that is in the center of the square).
	 */
	public int countSatisfyingInSquare(final int[] spot, final int radius, TilePredicate predicate) {
		return countSatisfyingInSquare(spot[0], spot[1], radius, predicate);
	}
	
	/**
	 * <pre><code>countSatisfyingInSquare(a, b, c, d)</code></pre>
	 * is equivalent to:
	 * <pre><code>countSatisfyingInSquare(a, b, c, d, false)</code></pre>
	 * (In other words, this method does <b>not</b> count the start tile that is in the center of the square).
	 */
	public int countSatisfyingInSquare(final int row, final int col, final int radius, TilePredicate predicate) {
		return countSatisfyingInSquare(row, col, radius, predicate, false);
	}
	
	/**
	 * Returns the number of {@link BoardTile BoardTiles} in a square of the given radius around the given center that satisfy the given
	 * {@link TilePredicate}.
	 */
	public int countSatisfyingInSquare(final int row, final int col, final int radius, TilePredicate predicate, boolean includeCenter) {
		if(radius == 0)
			return 0;
		Objects.requireNonNull(predicate);
		if(radius < 0)
			throw new IllegalArgumentException("Radius must be >= 0");
		int count = 0;
		for(int i = row - radius; i <= row + radius; i++) {
			for(int j = col - radius; j <= col + radius; j++) {
				if(!inBounds(i, j) || !includeCenter && i == row && j == col)
					continue;
				if(predicate.test(tiles[i][j]))
					count++;
			}
		}
		return count;
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
