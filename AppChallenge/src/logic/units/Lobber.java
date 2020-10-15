package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;
import utils.Coll;

/**
 * @author Sam Hooper
 *
 */
public class Lobber extends AbstractEnemyUnit {
	
	private static final int DEFAULT_MAX_HEALTH = 6;
	private static final int DEFAULT_MOVE_DISTANCE = 1;
	private static final int DEFAULT_LOB_MIN_DISTANCE = 2;
	private static final int DEFAULT_LOB_DAMAGE = 2;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID);
	
	private final StepMove stepMoveAbility;
	private final Lob lobAbility;
	
	public Lobber() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.lobAbility = new Lob(this, DEFAULT_LOB_DAMAGE, DEFAULT_LOB_MIN_DISTANCE);
		this.abilities.addAll(stepMoveAbility, lobAbility);
	}

	@Override
	public Move chooseMove(Board board, int movesRemaining) {
		System.out.printf("\tEntered Lobber::chooseMove%n");
		final int myRow = getRow(), myCol = getCol();
		//case 1: if we can lob, do so:
		System.out.printf("\t\tcase 1: if we can lob, do so%n");
		Collection<int[]> lobLegals = lobAbility.getLegals();
		System.out.printf("\t\tlobLegals.size()=%d%n", lobLegals.size());
		final int lobMin = lobAbility.getMinimumDistance();
		int[] best = null;
		int bestHealth = Integer.MIN_VALUE;
		for(int[] legal : lobLegals) {
			Unit u = board.getTileAt(legal).getUnitOrNull();
			if(u instanceof PlayerUnit) {
				int uHealth = u.getHealth();
				if(uHealth > bestHealth) {
					bestHealth = uHealth;
					best = legal;
				}
			}
		}
		if(best != null) {
			return lobAbility.createMoveFor(best, board.getTileAt(best).getPlayerUnitOrThrow());
		}
		//case 2: we can't lob, so try to move to a spot where we could next time:
		System.out.printf("\t\tcase 2: we can't lob, so try to move to a spot where we could next time%n");
		Collection<int[]> stepMoveLegals = stepMoveAbility.getLegals();
		if(movesRemaining > 1) {
			//TODO find the nearest enemy and move towards it.
		}
		//case 4: can't do anything.
		System.out.printf("\t\tcase 4: can't do anything%n");
		return Move.EMPTY_MOVE;
	}
	
	@Override
	public String toString() {
		return String.format("Lobber[health=%d, maxHealth=%d]", getHealth(), getMaxHealth());
	}
}
