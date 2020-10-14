package logic.units;

import java.util.Collection;
import java.util.EnumSet;

import logic.*;
import logic.abilities.*;
import utils.Coll;

/**
 * @author Sam Hooper
 *
 */
public class Brute extends AbstractEnemyUnit {
	
	private static final int DEFAULT_MAX_HEALTH = 4;
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final int DEFAULT_SHOOT_DAMAGE = 2;
	private static final int DEFAULT_SMASH_DAMAGE = 3;
	private static final int DEFAULT_SMASH_RADIUS = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final StepMove stepMoveAbility;
	private final Shoot shootAbility;
	private final Smash smashAbility;
	
	public Brute() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.shootAbility = new Shoot(this, DEFAULT_SHOOT_DAMAGE);
		this.smashAbility = new Smash(this, DEFAULT_SMASH_DAMAGE, DEFAULT_SMASH_RADIUS);
		this.abilities.addAll(stepMoveAbility, shootAbility, smashAbility);
	}
	
	public StepMove getStepMoveAbility() {
		return stepMoveAbility;
	}
	
	public Shoot getShootAbility() {
		return shootAbility;
	}
	
	public Smash getSmashAbility() {
		return smashAbility;
	}

	@Override
	public Move chooseMove(Board board, int movesRemaining) {
		//case 1: If we can smash, and it would damage at least one player unit, DO IT:
		final int myRow = getRow(), myCol = getCol(), smashRadius = smashAbility.getRadius();
		final Collection<int[]> smashLegals = smashAbility.getLegals();
		if(smashLegals.size() > 0 && board.anyInSquare(myRow, myCol, smashRadius, BoardTile::hasPlayerUnit))
			return smashAbility.createMoveFor(smashLegals.iterator().next(), null);
		//case 2: otherwise, if we could make a move that would put us into a position to smash on our next move, do so:
		final Collection<int[]> moveLegals = stepMoveAbility.getLegals();
		if(movesRemaining > 1) {
			int[] best = null;
			int bestCount = Integer.MIN_VALUE;
			for(int[] legal : moveLegals) {
				int count = board.countSatisfyingInSquare(legal, smashRadius, BoardTile::hasPlayerUnit);
				if(count > bestCount) {
					count = bestCount;
					best = legal;
				}
			}
			if(bestCount > 0) {
				return stepMoveAbility.createMoveFor(best, null);
			} //otherwise, fall through.
		}
		//case 3: either we can't make a move that would put us into a position to smash, or we don't have enough moves remaining to do so. Try shooting:
		final Collection<int[]> shootLegals = shootAbility.getLegals();
		int[] best = null;
		int maxHealth = Integer.MIN_VALUE;
		for(int[] legal : shootLegals) {
			Unit u = board.getUnitAtOrNull(legal);
			if(u instanceof PlayerUnit && shootAbility.canTarget(u) && u.getHealth() > maxHealth) {
				maxHealth = u.getHealth();
				best = legal;
			}
		}
		if(maxHealth > 0) {
			return shootAbility.createMoveFor(best, board.getUnitAtOrThrow(best));
		}
		//case 4: can't smash or shoot, can't move into a position to smash on the next turn. Try moving into a position to shoot on the next turn.
		best = null;
		int fewestOptions = Integer.MAX_VALUE;
		for(int[] legal : moveLegals) {
			int options = playerUnitsVisibleFrom(board, legal[0], legal[1]).size();
			if(options > 0 && options < fewestOptions) {
				fewestOptions = options;
				best = legal;
			}
		}
		if(best != null) {
			return stepMoveAbility.createMoveFor(best, null);
		}
		//case 5: none of the above, but we have legal movements, so pick one at random:
		if(moveLegals.size() > 0) {
			return stepMoveAbility.createMoveFor(Coll.getRandom(moveLegals), null);
		}
		//case 6: can't smash, shoot, or move:
		return Move.EMPTY_MOVE;
	}
	
	@Override
	public String toString() {
		return String.format("Brute@%h", this);
	}
}
