package logic.abilities;

import java.util.Collection;

import logic.GameObject;
import logic.Move;
import logic.Unit;

/**
 * @author Sam Hooper
 *
 */
public class SelfHeal extends AbstractAnyAbility implements TargetingAbility {

	/**
	 * @param unit
	 */
	SelfHeal(Unit unit) {
		super(unit);
		throw new UnsupportedOperationException("Unfinished class");
	}

	@Override
	public Collection<int[]> getLegals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canTarget(GameObject object) {
		return object == unit;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		// TODO Auto-generated method stub
		return null;
	}

}
