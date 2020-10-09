package logic;

import utils.IntRef;

/**
 * Interface for any {@link GameObject} that has health.
 * @author Sam Hooper
 *
 */
public interface HasHealth extends GameObject {
	/**
	 * Returns this {@code Unit}'s max health. The returned value will not be negative.
	 */
	int getMaxHealth();
	
	/**
	 * Returns this {@code Unit}'s current health. The returned value will be between 0 and {@link #getMaxHealth()} (inclusive on both ends).
	 */
	IntRef healthProperty();
	
	/**
	 * Returns {@code true} if this {@link GameObject} is dead, {@code false} otherwise.
	 */
	default boolean isDead() {
		return healthProperty().get() <= 0;
	}
}
