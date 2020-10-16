package logic.abilities;

import java.util.*;

import logic.*;
import utils.*;

/**
 * A {@link SingleHeal} that can heal any single {@link HasHealth} in the a square radius
 * around the {@link Unit} who possesses the {@link Ability}.
 * @author Sam Hooper
 *
 */
public class SquareSingleHeal extends SingleHeal implements TargetingAbility{

	private final IntRef radius;
	
	public SquareSingleHeal(Unit unit, int heal, int radius) {
		super(unit, heal);
		this.radius = new IntRef(radius);
	}

	@Override
	public Collection<int[]> getLegals() {
		Board board = unit.getBoard();
		Collection<int[]> legals = new ArrayList<>();
		board.forTilesInSquare(unit.getRow(), unit.getCol(), radius.get(), tile -> {
			if(tile.hasHasHealth())
				legals.add(new int[] {tile.getRow(), tile.getCol()});
		});
		return legals;
	}

	@Override
	public boolean canTarget(GameObject object) {
		return object instanceof HasHealth;
	}
	
	public IntRef radiusProperty() {
		return radius;
	}
}
