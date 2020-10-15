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
public class SelfHeal extends SingleHeal implements TargetingAbility {

	public SelfHeal(Unit unit, int heal) {
		super(unit, heal);
	}

	/**
	 * Returns an empty {@link Collection} if this {@link Ability Ability's} {@link Unit} is at {@link HasHealth#isFullHealth() full health}. 
	 */
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

	/**
	 * The {@link GameObject} parameter is ignored and can be {@code null}.
	 */
	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		return super.createMoveFor(destRow, destCol, unit);
	}
	
	
}
