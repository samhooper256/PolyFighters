package logic.units;

import java.util.*;

import logic.Ability;
import logic.TeamUnit;
import logic.TileType;
import logic.abilities.Shoot;
import logic.abilities.StepMove;

/**
 * @author Sam Hooper
 *
 */
public class BasicUnit extends AbstractUnit implements TeamUnit {
	
	public static final String NAME = "BasicUnit";
	
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final int DEFAULT_SHOOT_DAMAGE = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID);
	
	private final StepMove stepMoveAbility;
	private final Shoot shootAbility;
	
	public BasicUnit() {
		super();
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
