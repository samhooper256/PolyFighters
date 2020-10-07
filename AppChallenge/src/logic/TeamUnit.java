package logic;

import java.util.Collection;

import utils.CollectionRef;

import logic.units.AbstractTeamUnit;

/**
 * <p>A {@link Unit} belonging to the player. {@code TeamUnit}s are encouraged to maintain their
 * list of abilities as a {@code ListRef<Ability>} (see {@link CollectionRef}).</p>
 * 
 * <p>Implementing classes may consider extending the {@link AbstractTeamUnit} to make implementation
 * easier.</p>
 * 
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
