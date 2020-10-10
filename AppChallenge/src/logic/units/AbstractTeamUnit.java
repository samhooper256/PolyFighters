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
	@Override
	public Collection<int[]> getLegalSpots(Ability ability) {
		if(!abilities.contains(ability))
			throw new IllegalArgumentException("This unit does not have the given ability");
		return ability.getLegals();
	}
	
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
