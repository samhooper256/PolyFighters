package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;

/**
 * @author Sam Hooper
 *
 */
public class Rex extends AbstractPlayerUnit {
	public static final String NAME = "Rex";
	
	private static final int DEFAULT_MAX_HEALTH = 3;
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final int DEFAULT_SHOOT_DAMAGE = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final StepMove stepMoveAbility;
	private final Shoot shootAbility;
	
	public Rex() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.shootAbility = new Shoot(this, DEFAULT_SHOOT_DAMAGE);
		this.abilities.addAll(stepMoveAbility, shootAbility);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public StepMove getStepMoveAbility() {
		return stepMoveAbility;
	}
	
	public Shoot getShootAbility() {
		return shootAbility;
	}
}
