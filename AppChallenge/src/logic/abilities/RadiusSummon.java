package logic.abilities;

import java.util.*;
import java.util.function.Supplier;

import logic.*;
import logic.actions.PlaceObject;
import utils.*;

/**
 * Allows for a {@link Unit} to summon another in a radius around that {@link Unit}.
 * @author Sam Hooper
 *
 */
public class RadiusSummon extends AbstractAnyAbility {
	
	private final Supplier<Unit> unitSupplier;
	private final Class<? extends Unit> unitClass;
	private final IntRef radius;
	
	/**
	 * @param unit
	 */
	public RadiusSummon(Unit unit, Class<? extends Unit> unitClass, Supplier<Unit> unitSupplier, int radius) {
		super(unit);
		this.unitClass = unitClass;
		this.unitSupplier = unitSupplier;
		this.radius = new IntRef(radius);
	}
	
	public IntRef radiusProperty() {
		return radius;
	}
	
	public Class<? extends Unit> getUnitClass() {
		return unitClass;
	}

	@Override
	public Collection<int[]> getLegals() {
		final int myRow = unit.getRow(), myCol = unit.getCol(), radius = this.radius.get();
		final Board board = unit.getBoard();
		final List<int[]> legals = new ArrayList<>();
		for(int r = myRow - radius; r <= myRow + radius; r++) {
			for(int c = myCol - radius; c <= myCol + radius; c++) {
				if(r == myRow && c == myCol || !board.inBounds(r, c) || board.isOccupied(r, c))
					continue;
				legals.add(new int[] {r, c});
			}
		}
		return legals;
	}

	/**
	 * The {@link GameObject} parameter will be ignored and may be {@code null}.
	 */
	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		return new Move(this, new PlaceObject(unitSupplier.get(), destRow, destCol));
	}
	

}
