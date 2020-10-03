package logic;

import java.util.Collection;

/**
 * @author Sam Hooper
 *
 */
public interface TeamUnit extends Unit {
	String getName();
	/**
	 * Returns a {@link Collection} of (row, col) ordered pairs. Each ordered
	 * pair is a destination tile where this unit could legally use the given ability.
	 * @param ability
	 * @return
	 * @throws IllegalArgumentException if this unit does not have the given ability.
	 */
	Collection<int[]> getLegalSpots(Ability ability);
}
