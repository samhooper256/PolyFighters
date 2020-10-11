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
public class Assassin extends AbstractEnemyUnit {
	
	private static final String NAME = "ASSASSIN";
	
	private static final int DEFAULT_MAX_HEALTH = 1;
	private static final int DEFAULT_TELEPORT_DISTANCE = 4;
	private static final int DEFAULT_MELEE_DAMAGE = 2;
	
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);

	private final DiamondTeleport teleportAbility;
	private final Melee meleeAbility;
	
	public Assassin() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.teleportAbility = new DiamondTeleport(this, DEFAULT_TELEPORT_DISTANCE);
		this.meleeAbility = new Melee(this, DEFAULT_MELEE_DAMAGE);
		this.abilities.addAll(teleportAbility, meleeAbility);
	}

	@Override
	public Move chooseMove(Board board, int movesRemaining) {
		// TODO Auto-generated method stub
		return null;
	}
}
