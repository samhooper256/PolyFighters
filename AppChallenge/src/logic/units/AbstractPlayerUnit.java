package logic.units;

import java.util.*;

import logic.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractPlayerUnit extends AbstractUnit implements PlayerUnit {
	
	protected final IntRef movesRemaining;
	
	protected AbstractPlayerUnit(int maxHealth) {
		this(maxHealth, maxHealth);
	}
	
	protected AbstractPlayerUnit(int maxHealth, int currentHealth) {
		this(null, -1, -1, maxHealth, currentHealth, new ArrayList<>());
	}
	
	/**
	 * Creates a new {@link AbstractPlayerUnit} with no {@link #getMovesRemaining() moves remaining}.
	 */
	protected AbstractPlayerUnit(Board board, int row, int col, int maxHealth, int currentHealth, List<Ability> abilities) {
		super(board, row, col, maxHealth, currentHealth, abilities);
		this.movesRemaining = new IntRef(0);
	}
	
	@Override
	public String toString() {
		return String.format("TeamUnit[health=%d, maxHealth=%d]", health.get(), maxHealth.get());
	}

	@Override
	public IntRef movesRemainingProperty() {
		return movesRemaining;
	}
}
