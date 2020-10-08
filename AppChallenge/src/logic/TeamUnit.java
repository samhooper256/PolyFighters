package logic;

import java.util.Collection;

import utils.CollectionRef;

import logic.units.AbstractUnit;

/**
 * <p>A {@link Unit} belonging to the player. {@code TeamUnit}s are encouraged to maintain their
 * list of abilities as a {@code ListRef<Ability>} (see {@link CollectionRef}).</p>
 * 
 * <p>Implementing classes may consider extending the {@link AbstractUnit} to make implementation
 * easier.</p>
 * 
 * @author Sam Hooper
 *
 */
public interface TeamUnit extends Unit {
	String getName();
	
}
