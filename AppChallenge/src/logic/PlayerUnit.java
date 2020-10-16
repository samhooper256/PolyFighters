package logic;

import java.util.Collection;

import utils.CollectionRef;
import utils.IntRef;
import logic.units.AbstractUnit;

/**
 * <p>A {@link Unit} belonging to the player. {@link PlayerUnit PlayerUnits} are encouraged to maintain their
 * list of abilities as a {@code ListRef<Ability>} (see {@link CollectionRef}).</p>
 * 
 * <p>Implementing classes may consider extending the {@link AbstractUnit} to make implementation
 * easier.</p>
 * 
 * @author Sam Hooper
 *
 */
public interface PlayerUnit extends Unit {
	
	/**
	 * @return the name of this {@link Unit}, as it would be displayed to the user.
	 */
	String getName();
	
	@Override
	default Turn playingTurn() {
		return Turn.PLAYER;
	}
	
	/**
	 * The number of moves this {@link Unit} can still make in the current {@link Turn}.
	 */
	IntRef movesRemainingProperty();
	
	default int getMovesRemaining() {
		return movesRemainingProperty().get();
	}
	/**
	 * Sets the number of {@link #getMovesRemaining() moves remaining}.
	 */
	default void setMovesRemaining(int movesRemaining) {
		movesRemainingProperty().set(movesRemaining);
	}
}
