package logic;

import java.util.Collection;

/**
 * <p>An ability that a {@link Unit} can have. This interface must not be extended directly; rather, one of its subinterfaces
 * {@link TeamAbility}, {@link EnemyAbility} or {@link AnyAbility} must be implemented. Each {@code Ability} object is associated
 * with a single {@link Unit}, which can be retrieved by the {@link #getUnit()} method.</p>
 * 
 * <p><i>The unit of an {@code Ability}</i> is the {@code Unit} returned by {@link #getUnit()}. If {@code getUnit()} returns {@code null},
 * then the {@code Ability} has no {@code Unit}.</p>
 * 
 * <p>All units are requires to support the {@link #getLegals()} method, which returns a {@link Collection} of ordered-pairs (given
 * as length-2 {@code int[]} objects) that represent the tiles on the board where this ability could be legally used by its unit. The returned
 * Collection should not contain any duplicates (ordered pairs with the same two {@code int}s in the same order), but the return type is not
 * {@link Set} so as to allow implementors as much freedom as possible.</p>
 * @author Sam Hooper
 *
 */
public interface Ability {
	
	/**
	 * Returns the unit of this ability, or {@code null} if this {@code Ability} has no {@code Unit}.
	 * Every {@code Ability} is associated with 0 or 1 {@code Unit}(s) at any given time.
	 * @return
	 */
	Unit getUnit();
	
	/**
	 * Returns a {@link Collection} of (row, col) ordered pairs that indicate the destination tiles where this ability could be legally used by its
	 * unit on its unit's board. The returned {@code Collection} contains no duplicates. (That is, it contains no two elements {@code a} and
	 * {@code b} such that {@code Arrays.equals(a, b)}).
	 * 
	 * <p>Behavior of this method is undefined if {@code getUnit() == null} (that is, if this {@code Ability} does not currently have a {@link Unit}).</p>
	 * @return
	 */
	Collection<int[]> getLegals();
	
}
