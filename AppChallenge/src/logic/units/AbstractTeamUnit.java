package logic.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import logic.Ability;
import logic.Board;
import logic.TeamUnit;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractTeamUnit extends AbstractUnit implements TeamUnit {
	
	protected AbstractTeamUnit(int maxHealth) {
		this(maxHealth, maxHealth);
	}
	
	protected AbstractTeamUnit(int maxHealth, int currentHealth) {
		this(null, -1, -1, maxHealth, currentHealth, new ArrayList<>());
	}
	
	protected AbstractTeamUnit(Board board, int row, int col, int maxHealth, int currentHealth, List<Ability> abilities) {
		super(board, row, col, maxHealth, currentHealth, abilities);
	}
}
