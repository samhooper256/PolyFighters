package logic;

import java.util.Collection;

/**
 * <p>An ability that a {@link Unit} can have. This interface should not be extended directly; rather, one of its subinterfaces
 * {@link TeamAbility}, {@link EnemyAbility} or {@link AnyAbility} should be implemented. Each {@code Ability} object is associated
 * with a single {@link Unit}, which can be retrieved by the {@link #getUnit()} method.</p>
 * 
 * <p><i>The unit of an {@code Ability}</i> is the {@code Unit} returned by {@link #getUnit()}.</p>
 * @author Sam Hooper
 *
 */
public interface Ability {
	Unit getUnit();
	/**
	 * Returns an array of (row, col) ordered pairs that indicate the destination tiles where this ability could be legally used.
	 * @return
	 */
	Collection<int[]> getLegals();
}
