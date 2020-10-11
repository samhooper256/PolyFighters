package logic.abilities;

import java.util.*;

import logic.Board;
import logic.GameObject;
import logic.Move;
import logic.TileType;
import logic.Unit;
import logic.actions.Relocate;
import utils.IntRef;

/**<p>Every {@code TeleportAbility} has a set of traversable {@link TileType}s. In order for a {@link Unit} <b>U</b> who possesses this {@code Ability}
 * to use it on a given tile of {@code TileType} <b>T</b>, <i>all</i> of the following must be true:
 * <ul>
 * <li><b>T</b> must be a member of this {@code Ability}'s set of traversable {@code TileType}s.</li>
 * <li><b>U</b> can traverse <b>T</b>, where "can traverse" is defined in the documentation of {@link Unit}.
 * </ul>
 * </p>
 * @author Sam Hooper
 *
 */
public class DiamondTeleport extends AbstractAnyAbility implements MoveAbility {
	
	public static final int MAX_DISTANCE = 120;
	public static final int MIN_DISTANCE = 1;
	
	private IntRef distance;
	/** If {@code null}, the {@link TileType}s traversable by this ability are the same as the unit's. */
	private EnumSet<TileType> traversableTileTypes;
	
	public DiamondTeleport(Unit unit, int distance) {
		this(unit, distance, null);
	}
	
	public DiamondTeleport(Unit unit, int distance, EnumSet<TileType> traversableTileTypes) {
		super(unit);
		verifyDistance(distance);
		this.distance = new IntRef(distance);
		this.traversableTileTypes = traversableTileTypes;
	}
	
	private static void verifyDistance(int distance) {
		if(distance < MIN_DISTANCE || distance > MAX_DISTANCE)
			throw new IllegalArgumentException(String.format("Distance must be between %d and %d", MIN_DISTANCE, MAX_DISTANCE));
	}
	
	@Override
	public Collection<int[]> getLegals() {
		ArrayList<int[]> legals = new ArrayList<>();
		final Board board = unit.getBoard();
		final int distance = this.distance.get();
		final int unitRow = unit.getRow(), unitCol = unit.getCol();
		for(int i = -distance; i <= distance; i++) {
			int j = Math.abs(Math.abs(i) - distance);
			for(int k = -j; k <= j; k++) {
				final int r = unitRow + i;
				final int c = unitCol + k;
				if(board.inBounds(r, c) && canTraverse(board.getTileAt(r, c).getType()) && !board.hasUnit(r, c) && !board.hasObstacle(r, c))
					legals.add(new int[] {r, c});
			}
		}
		return legals;
	}
	
	public int getDistance() {
		return distance.get();
	}
	
	public void setDistance(int distance) {
		this.distance.set(distance);
	}
	
	public IntRef distanceProperty() {
		return distance;
	}
	
	/**
	 * Creates the {@link Move} object for the given destination tile even if it is illegal. The {@link GameObject} parameter
	 * is ignored.
	 */
	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		return new Move(this, new Relocate(unit.getRow(), unit.getCol(), destRow, destCol));
	}

	@Override
	public boolean canTraverse(TileType type) {
		return unit.canTraverse(type) && (traversableTileTypes == null || traversableTileTypes.contains(type));
	}

}
