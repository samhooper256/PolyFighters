package logic;

/**
 * Represents a form of movement where the unit can 
 * @author Sam Hooper
 *
 */
public class StepMove extends AbstractAnyAbility {

	public static final int MAX_DISTANCE = 120;
	public static final int MIN_DISTANCE = 1;
	
	private int distance;
	
	public StepMove(Unit unit, int distance) {
		super(unit);
		verifyDistance(distance);
		this.distance = distance;
	}
	
	private static void verifyDistance(int distance) {
		if(distance < MIN_DISTANCE || distance > MAX_DISTANCE)
			throw new IllegalArgumentException(String.format("Distance must be between %d and %d", MIN_DISTANCE, MAX_DISTANCE));
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public final int getDistance() {
		return distance;
	}
}
