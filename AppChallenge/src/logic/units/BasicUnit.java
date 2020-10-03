package logic.units;

import java.util.*;

import logic.Ability;
import logic.Board;
import logic.TileType;
import logic.abilities.StepMove;

/**
 * @author Sam Hooper
 *
 */
public class BasicUnit extends AbstractTeamUnit {
	
	public static final String NAME = "BasicUnit";
	
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID);
	
	private StepMove stepMoveAbility;
	
	public BasicUnit(Board board, int row, int col) {
		super(board, row, col);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.abilities.add(stepMoveAbility);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean canTraversable(TileType type) {
		return traversableTileTypes.contains(type);
	}

	@Override
	public Collection<int[]> getLegalSpots(Ability ability) {
		if(!abilities.contains(ability))
			throw new IllegalArgumentException("This unit does not have the given ability");
		return ability.getLegals();
	}
	
	public StepMove getStepMoveAbility() {
		return stepMoveAbility;
	}
	
}
