package logic.abilities;

import logic.AnyAbility;
import logic.Unit;

/**
 * <p> An abstract class that implements {@link AnyAbility} that provides some of the implementation for that interface.</p>
 * @author Sam Hooper
 *
 */
public abstract class AbstractAnyAbility implements AnyAbility {
	
	protected final Unit unit;
	
	AbstractAnyAbility(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public Unit getUnit() {
		return unit;
	}
}
