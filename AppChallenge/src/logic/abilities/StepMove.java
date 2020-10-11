package logic.abilities;

import java.util.*;

import logic.Board;
import logic.BoardTile;
import logic.GameObject;
import logic.Move;
import logic.TileType;
import logic.Unit;
import logic.actions.Relocate;
import utils.IntRef;

/**
 * <p>Represents a form of movement where the unit can move a set number of steps in any direction. The <i>distance</i> of a {@code StepMove}
 * is that set number of steps.</p>
 * 
 * <p>Every {@code StepMove} has a set of traversable {@link TileType}s. In order for a {@link Unit} <b>U</b> who possesses this {@code Ability}
 * to use it on a given tile of {@code TileType} <b>T</b>, <i>all</i> of the following must be true:
 * <ul>
 * <li><b>T</b> must be a member of this {@code Ability}'s set of traversable {@code TileType}s.</li>
 * <li><b>U</b> can traverse <b>T</b>, where "can traverse" is defined in the documentation of {@link Unit}.
 * </ul>
 * </p>
 * @author Sam Hooper
 *
 */
public class StepMove extends AbstractAnyAbility implements MoveAbility {

	public static final int MAX_DISTANCE = 120;
	public static final int MIN_DISTANCE = 1;
	
	private static final int[][] STEPS = {{-1,0}, {0,1}, {1,0}, {0,-1}};
	
	private IntRef distance;
	/** If {@code null}, the {@link TileType}s traversable by this ability are the same as the unit's. */
	private EnumSet<TileType> traversableTileTypes;
	
	public StepMove(Unit unit, int distance) {
		this(unit, distance, null);
	}
	
	/**
	 * Creates a new {@code StepMove} whose unit is {@code unit} and whose distance is {@code distance}.
	 * If {@code traversableTileTypes} is {@code null}, the set of {@link TileType}s traversable by this {@code Ability} are the same as the unit's.
	 * Otherwise, {@code traversableTileTypes} is the set of {@link TileType}s traversable by this {@code Ability}.
	 * @param unit
	 * @param distance
	 * @param traversableTileTypes
	 */
	public StepMove(Unit unit, int distance, EnumSet<TileType> traversableTileTypes) {
		super(unit);
		verifyDistance(distance);
		this.distance = new IntRef(distance);
		this.traversableTileTypes = traversableTileTypes;
	}
	
	private static void verifyDistance(int distance) {
		if(distance < MIN_DISTANCE || distance > MAX_DISTANCE)
			throw new IllegalArgumentException(String.format("Distance must be between %d and %d", MIN_DISTANCE, MAX_DISTANCE));
	}
	
	/** Returns {@code true} if this ability can traverse in the given {@link TileType} (that is, if the unit that possesses this {@code Ability}
	 * can use it on a tile with the given {@code TileType}), {@code false} otherwise.
	 * @param type the type of the tile
	 * @return {@code true} if the given {@code TileType} can be traversed using this {@code Ability}, {@code false} otherwise.
	 */
	@Override
	public boolean canTraverse(TileType type) {
		return unit.canTraverse(type) && (traversableTileTypes == null || traversableTileTypes.contains(type));
	}
	
	public void setDistance(int distance) {
		this.distance.set(distance);
	}
	
	public IntRef distanceProperty() {
		return distance;
	}

	@Override
	public Collection<int[]> getLegals() {
		final Board board = unit.getBoard();
		final int distance = this.distance.get();
		final int boardSize = Math.max(board.getRows(), board.getCols());
		final int boxSize = Math.min(distance * 2 + 1, boardSize * 2 + 1);
		final boolean[][] beenInList = new boolean[boxSize][boxSize]; //coordintes are relative the location of the unit, with the unit at the center.
		final int unitRow = unit.getRow(), unitCol = unit.getCol();
		//Stores length-3 arrays of (row, col, stepsRemaining). (row, col) coordinates are for locations in visited, not on the board.
		ArrayList<int[]> tilesToVisit = new ArrayList<>(); //all tiles in this have been added to allLegals (with the exception of the starting tile).
		ArrayList<int[]> allLegals = new ArrayList<>();
		tilesToVisit.add(new int[] {distance, distance, distance});
		beenInList[distance][distance] = true;
		while(!tilesToVisit.isEmpty()) {
			int[] tile = tilesToVisit.remove(tilesToVisit.size() - 1);
			assert beenInList[tile[0]][tile[1]];
			if(tile[2] == 0) //if no more steps can be taken from this spot...
				continue;
			for(final int[] step : STEPS) {
				int nr = tile[0] + step[0], nc = tile[1] + step[1];
				int boardRow = nr + unitRow - distance, boardCol = nc + unitCol - distance;
				if(!board.inBounds(boardRow, boardCol) || beenInList[nr][nc]) continue;
				BoardTile boardTile = board.getTileAt(boardRow, boardCol);
				if(boardTile.hasObstacle() || boardTile.hasUnit())
					continue;
				if(!canTraverse(boardTile.getType()))
					continue;
				tilesToVisit.add(new int[] {nr, nc, tile[2] - 1});
				beenInList[nr][nc] = true;
				allLegals.add(new int[] {boardRow, boardCol});
			}			
		}
		return allLegals;
	}
	
	/**
	 * Creates the {@link Move} object for the given destination tile even if it is illegal. The {@link GameObject} parameter
	 * is ignored.
	 */
	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		return new Move(this, new Relocate(unit.getRow(), unit.getCol(), destRow, destCol));
	}
}
