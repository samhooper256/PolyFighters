package graphics;

import java.util.*;

import logic.*;

/**
 * @author Sam Hooper
 *
 */
public class Player {
	
	private List<TeamUnit> units;
	
	public Player() {
		units = new ArrayList<>();
	}
	
	public void addUnit(TeamUnit unit) {
		units.add(unit);
	}
	
	public void addUnits(TeamUnit... units) {
		for(TeamUnit unit : units)
			addUnit(unit);
	}
	
	public List<TeamUnit> getUnitsUnmodifiable() {
		return Collections.unmodifiableList(units);
	}
	
	/**
	 * Returns {@code true} if the {@link TeamUnit} was present and has been removed, {@code false} otherwise.
	 */
	public boolean removeUnit(TeamUnit unit) {
		return units.remove(unit);
	}
}
