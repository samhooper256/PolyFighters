package logic;

/**
 * <p>An obstacle on the {@link Board}. Every {@code Obstacle} has a health (retrieved by {@link #getHealth()}) and a size (retrieved by {@link #getSize()}).</p>
 * 
 * @author Sam Hooper
 *
 */
public interface Obstacle extends GameObject {
	
	/**
	 * Returns the maximum health of this {@code Obstacle}.
	 */
	int getMaxHealth();
	
	/**
	 * Returns the current health of this {@code Obstacle}.
	 */
	int getHealth();
	
	ObstacleSize getSize();

}
