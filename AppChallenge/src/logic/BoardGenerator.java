package logic;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import logic.EnemyGenerator.EnemyFactory;
import logic.obstacles.ObstacleBase;

/**
 * @author Sam Hooper
 *
 */
public class BoardGenerator {
	public static final int DEFAULT_ROW_COUNT = 8;
	public static final int DEFAULT_COL_COUNT = 8;
	public static final double DEFAULT_LIQUID_PERCENT = 0.2;
	public static final double DEFAULT_POOL_STRENGTH = 0.4;
	public static final double DEFAULT_TURN_DIFFICULTY = 0;
	public static final int DIFFICULTY_MULTIPLY_THRESHOLD = 2; //the sum of the difficulties of the EnemyUnits added must be no more than turnDifficulty * DIFFICULTY_MULTIPLY_THRESHOLD
	public static final double DEFAULT_OBSTACLE_PERCENT = 0.08;
	public static final double DEFAULT_LARGE_OBSTACLE_PERCENT = 0.4;
	public static final int DEFAULT_LARGE_OBSTACLE_HEALTH = 3;
	public static final int DEFAULT_SMALL_OBSTACLE_HEALTH = 1;
	private static final int[][] ADJACENTS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	
	private int rowCount, colCount, largeObstacleHealth, smallObstacleHealth, movesPerPlayer, movesPerEnemy;
	private double liquidPercent, poolStrength, obstaclePercent, largeObstaclePercent, turnDifficulty;
	private List<PlayerUnit> teamUnits;
	
	public BoardGenerator() {
		this.rowCount = DEFAULT_ROW_COUNT;
		this.colCount = DEFAULT_COL_COUNT;
		this.liquidPercent = DEFAULT_LIQUID_PERCENT;
		this.poolStrength = DEFAULT_POOL_STRENGTH;
		this.teamUnits = Collections.emptyList();
		this.obstaclePercent = DEFAULT_OBSTACLE_PERCENT;
		this.largeObstaclePercent = DEFAULT_LARGE_OBSTACLE_PERCENT;
		this.largeObstacleHealth = DEFAULT_LARGE_OBSTACLE_HEALTH;
		this.smallObstacleHealth = DEFAULT_SMALL_OBSTACLE_HEALTH;
		this.movesPerPlayer = Board.DEFAULT_MOVES_PER_PLAYER;
		this.movesPerEnemy = Board.DEFAULT_MOVES_PER_ENEMY;
		this.turnDifficulty = 0;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setRowCount(int rowCount) {
		this.rowCount = rowCount;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setColumnCount(int colCount) {
		this.colCount = colCount;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setLiquidPercent(double liquidPercent) {
		this.liquidPercent = liquidPercent;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setPoolStrength(double poolStrength) {
		this.poolStrength = poolStrength;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setTeamUnits(List<PlayerUnit> teamUnits) {
		this.teamUnits = teamUnits;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setTurnDifficulty(double turnDifficulty) {
		this.turnDifficulty = turnDifficulty;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setMovesPerPlayer(int movesPerPlayer) {
		this.movesPerPlayer = movesPerPlayer;
		return this;
	}
	
	public int getMovesPerPlayer() {
		return movesPerPlayer;
	}
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setMovesPerEnemy(int movesPerEnemy) {
		this.movesPerEnemy = movesPerEnemy;
		return this;
	}
	
	public int getMovesPerEnemy() {
		return movesPerEnemy;
	}
	
	private int liquidRemaining;
	
	/**
	 * Returns a {@link Board} using all of the settings of this {@link BoardGenerator}. The number of {@link PlayerUnit#getMovesRemaining() moves remaining} for
	 * each {@link PlayerUnit} will be set to the number of {@link #getMovesPerPlayer() moves per player}  configured for this {@link BoardGenerator}.
	 * @return
	 */
	public Board build() {
		final Board board = new Board(rowCount, colCount, movesPerPlayer, movesPerEnemy);
		placeLiquid(board);
		placePlayerUnits(board);
		placeEnemyUnits(board);
		placeObstacles(board);
		return board;
	}

	private void placeLiquid(final Board board) {
		liquidRemaining = (int) Math.round(rowCount * colCount * liquidPercent);
		liquidRemaining = Math.min(liquidRemaining, rowCount * colCount - teamUnits.size() - (int) turnDifficulty); //don't have so many liquid tiles that we can't place the units.
		int[] liquifiedSpots = IntStream.range(0, rowCount * colCount).toArray();
		int liquifiedSpotsMaxUsableIndex = liquifiedSpots.length - 1;
		liquid_gen:
		while(liquidRemaining > 0) {
			int startRow, startCol;
			do {
				if(liquifiedSpotsMaxUsableIndex < 0)
					break liquid_gen;
				int liquifiedIndex = (int) (Math.random() * (liquifiedSpotsMaxUsableIndex + 1));
				int liquifiedValue = liquifiedSpots[liquifiedIndex];
				startRow = liquifiedValue / colCount;
				startCol = liquifiedValue % colCount;
				liquifiedSpots[liquifiedIndex] = liquifiedSpots[liquifiedSpotsMaxUsableIndex];
				liquifiedSpots[liquifiedSpotsMaxUsableIndex] = liquifiedValue;
				liquifiedSpotsMaxUsableIndex--;
			} while(board.getTileAt(startRow, startCol).getType() == TileType.LIQUID);
			board.getTileAt(startRow, startCol).setType(TileType.LIQUID);
			liquidRemaining--;
			if(liquidRemaining <= 0)
				break liquid_gen;
			Queue<int[]> toVisit = new LinkedList<>();
			for(int[] adj : ADJACENTS) {
				int r = startRow + adj[0], c = startCol + adj[1];
				if(r >= 0 && r < rowCount && c >= 0 && c < colCount && board.getTileAt(r, c).getType() != TileType.LIQUID)
					toVisit.add(new int[] {r, c});
			}
			while(!toVisit.isEmpty()) {
				int[] spot = toVisit.remove();
				BoardTile tile = board.getTileAt(spot[0], spot[1]);
				if(Math.random() < poolStrength) {
					tile.setType(TileType.LIQUID);
					liquidRemaining--;
					if(liquidRemaining <= 0)
						break liquid_gen;
				}
				else {
					continue;
				}
				
				for(int[] adj : ADJACENTS) {
					int nr = spot[0] + adj[0], nc = spot[1] + adj[1];
					if(nr < 0 || nr >= rowCount || nc < 0 || nc >= colCount)
						continue;
					BoardTile newTile = board.getTileAt(nr, nc);
					if(newTile.getType() == TileType.LIQUID)
						continue;
					toVisit.add(new int[] {nr, nc});
				}
			}
		}
	}
	
	/**
	 * Sets the {@link PlayerUnit#getMovesRemaining() moves remaining} values for the {@link PlayerUnit PlayerUnits}.
	 */
	private void placePlayerUnits(final Board board) {
		int[] placeSpots = IntStream.range(0, rowCount * colCount).toArray();
		int placeSpotsMaxIndex = placeSpots.length - 1;
		for(final PlayerUnit playerUnit : teamUnits) {
			int row, col;
			do {
				if(placeSpotsMaxIndex < 0)
					throw new IllegalStateException("Not enough space to place the team units");
				int placeIndex = (int) (Math.random() * (placeSpotsMaxIndex + 1));
				int placeValue = placeSpots[placeIndex];
				row = placeValue / colCount;
				col = placeValue % colCount;
				placeSpots[placeIndex] = placeSpots[placeSpotsMaxIndex];
				placeSpots[placeSpotsMaxIndex] = placeValue;
				placeSpotsMaxIndex--;
				
			} while(board.getTileAt(row, col).getType() == TileType.LIQUID);
			board.addUnitOrThrow(playerUnit, row, col);
			playerUnit.setMovesRemaining(movesPerPlayer);
		}
	}
	
	
	private void placeEnemyUnits(final Board board) {
		EnemyFactory[] factories = EnemyGenerator.factories().toArray(EnemyFactory[]::new);
		int[] factoryIndices = IntStream.range(0, factories.length).toArray();
		int factoryMaxIndex = factories.length - 1;
		int[] boardIndices = IntStream.range(0, rowCount * colCount).toArray();
		int boardMaxIndex = boardIndices.length - 1;
		double currentDifficulty = 0.0;
		outer:
		while(currentDifficulty < turnDifficulty) {
			if(boardMaxIndex < 0)
				break;
			factoryMaxIndex = factories.length - 1;
			int factoryIndex;
			do {
				if(factoryMaxIndex < 0)
					break outer;
				factoryIndex = (int) (Math.random() * (factoryMaxIndex + 1));
				int temp = factoryIndices[factoryIndex];
				factoryIndices[factoryIndex] = factoryIndices[factoryMaxIndex];
				factoryIndices[factoryMaxIndex] = temp;
				factoryMaxIndex--;
			} while(currentDifficulty + factories[factoryIndex].getDifficulty() > turnDifficulty * DIFFICULTY_MULTIPLY_THRESHOLD);
			EnemyFactory factory = factories[factoryIndex];
			int row, col;
			do {
				if(boardMaxIndex < 0)
					break outer;
				int boardIndex = (int) (Math.random() * (boardMaxIndex + 1));
				row = boardIndices[boardIndex] / colCount;
				col = boardIndices[boardIndex] % colCount;
				int temp = boardIndices[boardIndex];
				boardIndices[boardIndex] = boardIndices[boardMaxIndex];
				boardIndices[boardMaxIndex] = temp;
				boardMaxIndex--;
			} while(board.getTileAt(row, col).getType() == TileType.LIQUID || board.getTileAt(row, col).hasUnit());
			board.addUnitOrThrow(factory.make(), row, col);
			currentDifficulty += factories[factoryIndex].getDifficulty();
		}
		//TODO Finish up this methodical
	}

	private void placeObstacles(final Board board) {
		int obstacleCount = (int) Math.round(rowCount * colCount * obstaclePercent);
		int[] emptySpots = IntStream.range(0, rowCount * colCount).toArray();
		int emptyMax = emptySpots.length - 1;
		obstacle_gen:
		while(obstacleCount > 0) {
			obstacleCount--;
			int row, col;
			do {
				if(emptyMax < 0)
					break obstacle_gen;
				int index = (int) (Math.random() * (emptyMax + 1));
				int spotValue = emptySpots[index];
				row = spotValue / colCount;
				col = spotValue % colCount;
				emptySpots[index] = emptySpots[emptyMax];
				emptySpots[emptyMax] = spotValue;
				emptyMax--;
			}
			while(board.getTileAt(row, col).isOccupied() || board.getTileAt(row, col).getType() == TileType.LIQUID);
			Obstacle obstacle = Math.random() < largeObstaclePercent ?
					new ObstacleBase(ObstacleSize.LARGE, largeObstacleHealth) :
					new ObstacleBase(ObstacleSize.SMALL, smallObstacleHealth);
			board.addObstacleOrThrow(obstacle, row, col);
		}
	}
}
