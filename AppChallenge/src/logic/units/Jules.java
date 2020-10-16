package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;

/**
 * @author Sam Hooper
 *
 */
public class Jules extends AbstractPlayerUnit {
	public static final String NAME = "Jules";
	
	private static final int DEFAULT_MAX_HEALTH = 3;
	private static final int DEFAULT_MOVE_DISTANCE = 4;
	private static final int DEFAULT_MELEE_DAMAGE = 2;
	private static final EnumSet<TileType> DEFAULT_MELEE_ATTACK_FROM = EnumSet.of(TileType.SOLID);
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final StepMove stepMoveAbility;
	private final Melee meleeAbility;
	
	public Jules() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.meleeAbility = new Melee(this, DEFAULT_MELEE_DAMAGE, DEFAULT_MELEE_ATTACK_FROM);
		this.abilities.addAll(stepMoveAbility, meleeAbility);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public StepMove getStepMoveAbility() {
		return stepMoveAbility;
	}
	
	public Melee getMeleeAbility() {
		return meleeAbility;
	}
}
