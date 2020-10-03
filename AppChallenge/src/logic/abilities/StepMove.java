package logic.abilities;

import java.util.*;
import java.util.stream.Collectors;

import logic.Board;
import logic.TileType;
import logic.Unit;

/**
 * Represents a form of movement where the unit can move a set number of steps in any direction.
 * @author Sam Hooper
 *
 */
public class StepMove extends AbstractAnyAbility {

	public static final int MAX_DISTANCE = 120;
	public static final int MIN_DISTANCE = 1;
	
	private static final int[][] STEPS = {{-1,0}, {0,1}, {1,0}, {0,-1}};
	
	private int distance;
	/** If {@code null}, the {@link TileType}s traversable by this ability are the same as the unit's. */
	private EnumSet<TileType> traversableTileTypes;
	
	public StepMove(Unit unit, int distance) {
		this(unit, distance, null);
	}
	
	/**
	 * If {@code traversableTileTypes} is {@code null}, the {@link TileType}s traversable by this ability are the same as the unit's.
	 * @param unit
	 * @param distance
	 * @param traversableTileTypes
	 */
	public StepMove(Unit unit, int distance, EnumSet<TileType> traversableTileTypes) {
		super(unit);
		verifyDistance(distance);
		this.distance = distance;
		this.traversableTileTypes = traversableTileTypes;
	}
	
	private static void verifyDistance(int distance) {
		if(distance < MIN_DISTANCE || distance > MAX_DISTANCE)
			throw new IllegalArgumentException(String.format("Distance must be between %d and %d", MIN_DISTANCE, MAX_DISTANCE));
	}
	
	public boolean canTraverse(TileType type) {
		if(traversableTileTypes == null)
			return unit.canTraversable(type);
		return traversableTileTypes.contains(type);
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public final int getDistance() {
		return distance;
	}

	@Override
	public Collection<int[]> getLegals() {
		throw new UnsupportedOperationException("Unfinished");
	}
}
