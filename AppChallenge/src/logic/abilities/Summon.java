package logic.abilities;

import java.util.Collection;

import logic.GameObject;
import logic.Move;
import logic.Unit;

/**
 * @author Sam Hooper
 *
 */
public class Summon extends AbstractAnyAbility {

	/**
	 * @param unit
	 */
	Summon(Unit unit) {
		super(unit);
		throw new UnsupportedOperationException("Unfinished class");
	}

	@Override
	public Collection<int[]> getLegals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		// TODO Auto-generated method stub
		return null;
	}

}
