package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;

/**
 * @author Sam Hooper
 *
 */
public class Scales extends AbstractPlayerUnit {
	public static final String NAME = "Scales";
	
	private static final int DEFAULT_MAX_HEALTH = 4;
	private static final int DEFAULT_MOVE_DISTANCE = 2;
	private static final int DEFAULT_LOB_DAMAGE = 2;
	private static final int DEFAULT_LOB_MIN_DISTANCE = 2;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID);
	
	private final StepMove stepMoveAbility;
	private final Lob lobAbility;
	
	public Scales() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.lobAbility = new Lob(this, DEFAULT_LOB_DAMAGE, DEFAULT_LOB_MIN_DISTANCE);
		this.abilities.addAll(stepMoveAbility, lobAbility);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public StepMove getDiamondTeleportAbility() {
		return stepMoveAbility;
	}
	
	public Lob getSquareSingleHealAbility() {
		return lobAbility;
	}
}
