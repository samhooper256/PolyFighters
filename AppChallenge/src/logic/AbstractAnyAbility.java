package logic;

/**
 * @author Sam Hooper
 *
 */
abstract class AbstractAnyAbility implements Ability{
	
	protected final Unit unit;
	
	AbstractAnyAbility(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public Unit getUnit() {
		return unit;
	}
}
