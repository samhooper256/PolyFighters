package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;

/**
 * @author Sam Hooper
 *
 */
public class BasicUnit extends AbstractTeamUnit {
	
	public static final String NAME = "BasicUnit";
	
	private static final int DEFAULT_MAX_HEALTH = 3;
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final int DEFAULT_SHOOT_DAMAGE = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final StepMove stepMoveAbility;
	private final Shoot shootAbility;
	
	public BasicUnit() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.shootAbility = new Shoot(this, DEFAULT_SHOOT_DAMAGE);
		this.abilities.addAll(stepMoveAbility, shootAbility);
		this.abilities.addAll(new DiamondTeleport(this, 3), new Melee(this, 2), new Smash(this, 2, 1), new Lob(this, 2, 2),
				new SelfHeal(this, 1), new Summon(this, Goob.class, Goob::new, 2)); //TODO REMOVE, BasicUnits should only have StepMove and Shoot, this line is for testing purposes only.
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
