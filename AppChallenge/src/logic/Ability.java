package logic;

import java.util.Collection;

/**
 * <p>An ability that a {@link Unit} can have. This interface must not be extended directly; rather, one of its subinterfaces
 * {@link PlayerAbility}, {@link EnemyAbility} or {@link AnyAbility} must be implemented. Each {@code Ability} object is associated
 * with a single {@link Unit}, which can be retrieved by the {@link #getUnit()} method.</p>
 * 
 * <p><i>The unit of an {@code Ability}</i> is the {@code Unit} returned by {@link #getUnit()}. If {@code getUnit()} returns {@code null},
 * then the {@code Ability} has no {@code Unit}.</p>
 * 
 * <p>All units are requires to support the {@link #getLegals()} method, which returns a {@link Collection} of ordered-pairs (given
 * as length-2 {@code int[]} objects) that represent the tiles on the board where this ability could be legally used by its unit. The returned
 * Collection should not contain any duplicates (ordered pairs with the same two {@code int}s in the same order), but the return type is not
 * {@link Set} so as to allow implementors as much freedom as possible.</p>
 * 
 * <p><b>The {@code equals} method of all classes implementing this interface must test for object identity using the == operator.</b></p>
 * @author Sam Hooper
 *
 */
public interface Ability {
	
	/**
	 * Returns the unit of this ability, or {@code null} if this {@code Ability} has no {@code Unit}.
	 * Every {@code Ability} is associated with 0 or 1 {@code Unit}(s) at any given time.
	 */
	Unit getUnit();
	
	/**
	 * Returns a {@link Collection} of (row, col) ordered pairs that indicate the destination tiles where this ability could be legally used by its
	 * unit on its unit's board. The returned {@code Collection} contains no duplicates. (That is, it contains no two elements {@code a} and
	 * {@code b} such that {@code Arrays.equals(a, b)}).
	 * 
	 * <p>Behavior of this method is undefined if {@code getUnit() == null} (that is, if this {@code Ability} does not currently have a {@link Unit}).</p>
	 */
	Collection<int[]> getLegals();
	
	/**
	 * Creates a {@link Move} that when executed (by invoking {@link Move#execute(Board)}) will use this {@code Ability} on the appropriate
	 * {@link Board}. The destination tile (that is, the tile the user selects in order to use this {@code Ability}) will be given by
	 * the first two parameters. The {@link GameObject} parameter is the "target" of this {@code Ability} use. It may be {@code null} and can be
	 * ignored if this ability needs no target (such as a movement-based {@code Ability}, for example). If it is not {@code null}, it must be a {@code GameObject}
	 * on the destination tile.  Behavior of this method is undefined if the destination row and column do not indicate a legal move, the target is not on the
	 * destination tile, or the target is {@code null} when it should not be.
	 */
	Move createMoveFor(int destRow, int destCol, GameObject target);
	
	default Move createMoveFor(int[] spot, GameObject target) {
		return createMoveFor(spot[0], spot[1], target);
	}
}
