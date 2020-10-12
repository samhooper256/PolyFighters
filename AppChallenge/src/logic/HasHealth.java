package logic;

import utils.BooleanRef;
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
	IntRef maxHealthProperty();
	
	default int getMaxHealth() {
		return maxHealthProperty().get();
	}
	
	/**
	 * Returns this {@code Unit}'s current health. The returned value will be between 0 and {@link #getMaxHealth()} (inclusive on both ends).
	 */
	IntRef healthProperty();
	
	default int getHealth() {
		return healthProperty().get();
	}
	
	/**
	 * Returns {@code true} if this {@link GameObject} is dead, {@code false} otherwise.
	 */
	default boolean isAlive() {
		return aliveProperty().get();
	}
	
	default boolean isFullHealth() {
		return getHealth() == getMaxHealth();
	}
	
	/**
	 * The {@link BooleanRef} stores {@code true} if this {@link GameObject} is alive, {@code false} otherwise.
	 * @return
	 */
	BooleanRef aliveProperty();
}
