package logic.abilities;

import java.util.*;

import logic.*;
import logic.actions.*;
import utils.IntRef;

/**
 * An {@link Ability} allowing a {@link Unit} to heal itself. Can be used anywhere. Cannot be used when the {@code Unit} is at full health.
 * @author Sam Hooper
 *
 */
public class SelfHeal extends AbstractAnyAbility implements TargetingAbility {

	private final IntRef heal;
	
	public SelfHeal(Unit unit, int heal) {
		super(unit);
		this.heal = new IntRef(heal);
	}

	@Override
	public Collection<int[]> getLegals() {
		if(unit.isFullHealth())
			return Collections.emptySet();
		return Set.of(new int[] {unit.getRow(), unit.getCol()});
	}

	@Override
	public boolean canTarget(GameObject object) {
		return object == unit;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		if(destRow != unit.getRow() || destCol != unit.getCol())
			throw new IllegalArgumentException("Not a legal move.");
		return new Move(this, new ChangeHealth(unit, heal.get()));
	}

	public IntRef healProperty() {
		return heal;
	}

}
