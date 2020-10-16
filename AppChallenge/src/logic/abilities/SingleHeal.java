package logic.abilities;

import logic.*;
import logic.actions.*;
import utils.IntRef;

/**
 * An {@link Ability} that allows for a single {@link HasHealth} to be healed (that is, for its health to be increased by a non-negative value).
 * @author Sam Hooper
 *
 */
public abstract class SingleHeal extends AbstractAnyAbility {
	
	protected final class HealProperty extends IntRef {
		public HealProperty(int value) {
			super(value);
		}

		@Override
		public boolean set(int newValue) {
			if(newValue < 0)
				throw new IllegalArgumentException("Heal amount must not be negative. Was: " + newValue);
			return super.set(newValue);
		}
	}
	
	protected final HealProperty heal;
	
	public SingleHeal(Unit unit, final int heal) {
		super(unit);
		if(heal < 0)
			throw new IllegalArgumentException("Heal amount must not be negative. Was: " + heal);
		this.heal = new HealProperty(heal);
	}

	public IntRef healProperty() {
		return heal;
	}
	
	public int getHeal() {
		return heal.get();
	}

	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		if(target instanceof HasHealth)
			return new Move(this, new ChangeHealth((HasHealth) target, heal.get()));
		throw new IllegalArgumentException("The given GameObject must implement HasHealth in order to be healed.");
	}

}