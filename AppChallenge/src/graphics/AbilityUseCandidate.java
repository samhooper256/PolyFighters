package graphics;

/**
 * Allows for an object to be marked as the "use candidate" for an {@link Ability}. A use candidate for an {@code Ability} is something that
 * the user can click on to use the currently selected {@code Ability} on that thing.
 * @author Sam Hooper
 *
 */
public interface AbilityUseCandidate {
	/**
	 * If the given value if {@code true}, makes this object a use candidate for an {@link Ability}. Otherwise,
	 * makes this object not a use candidate.
	 */
	void setUseCandidate(boolean value);
	
	/**
	 * Returns {@code true} if this object is currently a use candidate, {@code false} otherwise.
	 */
	boolean isUseCandidate();
}