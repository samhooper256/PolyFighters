package logic;

/**
 * <p>An obstacle on the {@link Board}. Every {@code Obstacle} has a health (retrieved by {@link #getHealth()}) and a size (retrieved by {@link #getSize()}).</p>
 * 
 * @author Sam Hooper
 *
 */
public interface Obstacle extends HasHealth {
	
	ObstacleSize getSize();

}
