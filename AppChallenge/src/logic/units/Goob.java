package logic.units;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import logic.Ability;
import logic.Board;
import logic.EnemyUnit;
import logic.TileType;
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
	
}

