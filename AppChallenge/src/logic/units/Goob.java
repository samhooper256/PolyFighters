package logic.units;

import java.util.*;
import java.util.stream.Collectors;

import logic.*;
import logic.abilities.Shoot;
import logic.abilities.StepMove;
import utils.BooleanRef;
import utils.CollectionRef;
import utils.IntRef;

/**
 * @author Sam Hooper
 *
 */
public class Goob extends AbstractEnemyUnit {
	
	public static final String NAME = "Goob";
	
	private static final int DEFAULT_MAX_HEALTH = 2;
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final int DEFAULT_SHOOT_DAMAGE = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final StepMove stepMoveAbility;
	private final Shoot shootAbility;
	
	public Goob() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.shootAbility = new Shoot(this, DEFAULT_SHOOT_DAMAGE);
		this.abilities.addAll(stepMoveAbility, shootAbility);
	}
	
	public StepMove getStepMoveAbility() {
		return stepMoveAbility;
	}
	
	public Shoot getShootAbility() {
		return shootAbility;
	}

	/**
	 * If this {@link Goob} can shoot a {@link TeamUnit} from its current position, it does so. Otherwise, if it has more than one move
	 * remaining, it tries to get into a position where it could shoot a player's unit. Otherwise, if there's only one move remaining, it
	 * tries to get into a position where it is not on the same row or column as any of the player's units.
	 */
	@Override
	public Move chooseMove(final Board board, final int movesRemaining) {
		final Collection<int[]> stepMoveLegals = stepMoveAbility.getLegals();
		final Collection<int[]> shootLegals = shootAbility.getLegals();
		System.out.printf("\tentered Goob::chooseMove for Goob@(%d,%d), shootLegals=%s%n", row, col, 
				shootLegals.stream().map(Arrays::toString).collect(Collectors.joining(", ","[","]")
				));
		if(shootLegals.size() > 0) {
			int[] min = null;
			Unit minUnit = null;
			for(int[] legal : shootLegals) {
				Unit unit = board.getUnitAtOrNull(legal[0], legal[1]);
				if(unit == null)
					continue;
				if(minUnit == null || unit.getHealth() > minUnit.getHealth()) {
					minUnit = unit;
					min = legal;
				}
			}
			if(minUnit != null)
				return shootAbility.createMoveFor(min, minUnit);
		}
		return Move.EMPTY_MOVE; //TODO finish AI
	}
	
}

