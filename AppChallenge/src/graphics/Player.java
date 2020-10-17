package graphics;

import java.util.*;

import logic.*;
import utils.IntChangeListener;

/**
 * @author Sam Hooper
 *
 */
public class Player {
	
	private List<PlayerUnit> units;
	
	public Player() {
		units = new ArrayList<>();
	}
	
	public void addUnit(PlayerUnit unit) {
		units.add(unit);
		
	}
	
	public void addUnits(PlayerUnit... units) {
		for(PlayerUnit unit : units)
			addUnit(unit);
	}
	
	public List<PlayerUnit> getUnitsUnmodifiable() {
		return Collections.unmodifiableList(units);
	}
	
	/**
	 * Returns {@code true} if the {@link PlayerUnit} was present and has been removed, {@code false} otherwise.
	 */
	public boolean removeUnit(PlayerUnit unit) {
		return units.remove(unit);
	}
}
