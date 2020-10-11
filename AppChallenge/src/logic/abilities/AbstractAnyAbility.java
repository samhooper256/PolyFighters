package logic.abilities;

import logic.*;

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
	
	/**
	 * Returns {@code true} if this {@link Ability}'s {@link Unit} is on the opposing team as the given {@code Unit}.
	 * Returns {@code false} if the given {@code unit} is {@code null}.
	 */
	public boolean isAgainstOrFalse(Unit unitArg) {
		if(unit == null)
			return false;
		if(unit instanceof TeamUnit)
			return unitArg instanceof EnemyUnit;
		if(unit instanceof EnemyUnit)
			return unitArg instanceof TeamUnit;
		throw new UnsupportedOperationException("Unrecognized unit subtype: " + unitArg.getClass());
	}
}
