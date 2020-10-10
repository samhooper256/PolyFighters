package logic.units;

import java.util.List;

import logic.Ability;
import logic.Board;
import logic.EnemyUnit;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractEnemyUnit extends AbstractUnit implements EnemyUnit {
	
	protected AbstractEnemyUnit(int maxHealth, int currentHealth) {
		super(maxHealth, currentHealth);
	}
	
	protected AbstractEnemyUnit(int maxHealth) {
		super(maxHealth);
	}

	protected AbstractEnemyUnit(Board board, int row, int col, int maxHealth, int currentHealth,
			List<Ability> abilities) {
		super(board, row, col, maxHealth, currentHealth, abilities);
	}

}
