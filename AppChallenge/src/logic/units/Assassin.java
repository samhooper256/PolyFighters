package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;
import utils.Coll;

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
		Collection<int[]> meleeLegals = meleeAbility.getLegals();
		Collection<int[]> teleportLegals = teleportAbility.getLegals();
		if(meleeLegals.size() > 0) {
			Unit bestUnit = null;
			int bestHealth = Integer.MIN_VALUE;
			for(int[] legal : meleeLegals) {
				Unit unit = board.getUnitAtOrNull(legal[0], legal[1]);
				if(!(unit instanceof TeamUnit))
					continue;
				if(unit.getHealth() > bestHealth) {
					bestHealth = unit.getHealth();
					bestUnit = unit;
				}
			}
			if(bestUnit != null)
				return meleeAbility.createMoveFor(bestUnit.getRow(), bestUnit.getCol(), bestUnit);
		}
		
		//case 2: no legal attacks:
		if(movesRemaining == 1) {
			//case 2.1: no legal attacks, and no time to make one (only 1 move remaining)
			int[][] teamUnitCounts = getRowColCountsOfTeamUnits(board);
			int[] pref = leastVisibleSpotOf(teleportLegals, teamUnitCounts);
			if(pref == null)
				return Move.EMPTY_MOVE;
			return teleportAbility.createMoveFor(pref, null);
		}
		//case 2.2: no legal attacks, but there may be time to make one on a future move.
		int[] pref = null;
		int fewestOptions = Integer.MAX_VALUE;
		for(int[] legal : teleportLegals) {
			if(!meleeAbility.canAttackFrom(board.getTileAt(legal).getType()))
				continue;
			int options = teamUnits8Adjacent(board, legal).size();
			if(options > 0 && options < fewestOptions) {
				pref = legal;
				fewestOptions = options;
			}
		}
		if(pref == null) {
			if(teleportLegals.size() > 0)
				return teleportAbility.createMoveFor(Coll.getRandom(teleportLegals), null); //make random move, it might take us closer and we can attack on a future turn
			return Move.EMPTY_MOVE;
		}
		return teleportAbility.createMoveFor(pref, null);
	}
}
