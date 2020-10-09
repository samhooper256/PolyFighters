package graphics;

import logic.GameObject;

/**
 * An interface for a {@link Highlightable} providing a representation of a {@link GameObject}.
 * @author Sam Hooper
 *
 */
public interface GameObjectRepresentation extends Highlightable, AbilityUseCandidate {
	
	/**
	 * Returns the {@link GameObject} that this {@link GameObjectRepresentation} is currently representing, or {@code null} if it
	 * is not currently representing any {@code GameObject}.
	 * @return
	 */
	public GameObject getGameObject();

}
