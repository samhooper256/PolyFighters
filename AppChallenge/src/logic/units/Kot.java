package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;

/**
 * @author Sam Hooper
 *
 */
public class Kot extends AbstractPlayerUnit {
	public static final String NAME = "Kot";
	
	private static final int DEFAULT_MAX_HEALTH = 2;
	private static final int DEFAULT_TELEPORT_DISTANCE = 4;
	private static final int DEFAULT_OTHER_HEAL = 1;
	private static final int DEFAULT_HEAL_RADIUS = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final DiamondTeleport teleportAbility;
	private final SquareSingleHeal otherHealAbility;
	
	public Kot() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.teleportAbility = new DiamondTeleport(this, DEFAULT_TELEPORT_DISTANCE);
		this.otherHealAbility = new SquareSingleHeal(this, DEFAULT_OTHER_HEAL, DEFAULT_HEAL_RADIUS);
		this.abilities.addAll(teleportAbility, otherHealAbility);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public DiamondTeleport getDiamondTeleportAbility() {
		return teleportAbility;
	}
	
	public SquareSingleHeal getSquareSingleHealAbility() {
		return otherHealAbility;
	}
}
