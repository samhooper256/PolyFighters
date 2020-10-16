package logic.units;

import java.util.*;

import logic.*;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractPlayerUnit extends AbstractUnit implements PlayerUnit {
	
	protected AbstractPlayerUnit(int maxHealth) {
		this(maxHealth, maxHealth);
	}
	
	protected AbstractPlayerUnit(int maxHealth, int currentHealth) {
		this(null, -1, -1, maxHealth, currentHealth, new ArrayList<>());
	}
	
	protected AbstractPlayerUnit(Board board, int row, int col, int maxHealth, int currentHealth, List<Ability> abilities) {
		super(board, row, col, maxHealth, currentHealth, abilities);
	}
	
	@Override
	public String toString() {
		return String.format("TeamUnit[health=%d, maxHealth=%d]", health.get(), maxHealth.get());
	}
}
