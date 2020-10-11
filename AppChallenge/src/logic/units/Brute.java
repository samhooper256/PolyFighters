package logic.units;

import java.util.EnumSet;

import logic.Board;
import logic.Move;
import logic.TileType;
import logic.abilities.*;

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
		// TODO Auto-generated method stub
		return Move.EMPTY_MOVE;
	}
	
	@Override
	public String toString() {
		return String.format("Brute@%h", this);
	}
}
