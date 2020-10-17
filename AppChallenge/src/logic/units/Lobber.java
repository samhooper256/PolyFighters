package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;
import utils.BFS;
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
		final int myRow = getRow(), myCol = getCol();
		//case 1: if we can lob, do so:
		Collection<int[]> lobLegals = lobAbility.getLegals();
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
		//case 2: we can't lob, so try to move to a spot where we could in the future:
		Collection<int[]> stepMoveLegals = stepMoveAbility.getLegals();
		BoardTile nearestTile = board.getNearestTileWithPlayerUnit(myRow, myCol, traversableTileTypes);
		if(nearestTile == null) { //no PlayerUnits on the board
			return Move.EMPTY_MOVE;
		}
		int nearestRow = nearestTile.getRow(), nearestCol = nearestTile.getCol();
		best = null;
		int minDist = Integer.MAX_VALUE;
		for(int[] legal : stepMoveLegals) {
			BFS.Result<BoardTile> bfsResult = BFS.of(board.getRawTiles(), legal[0], legal[1], tile -> {
				final int tileRow = tile.getRow(), tileCol = tile.getCol();
				return 	tileRow == nearestRow && Math.abs(tileCol - nearestCol) >= lobMin ||
						tileCol == nearestCol && Math.abs(tileRow - nearestRow) >= lobMin;
			}, tile -> !traversableTileTypes.contains(tile.getType()))
					.setIncludeStart(true)
					.search();
			if(bfsResult.found()) {
				int pathLength = bfsResult.startNode().length();
				if(pathLength < minDist) {
					minDist = pathLength;
					best = legal;
				}
			}
		}
		if(best != null) {
			return stepMoveAbility.createMoveFor(best, null);
		}
		//case 3: can't do anything.
		return Move.EMPTY_MOVE;
	}
	
	@Override
	public String toString() {
		return String.format("Lobber[health=%d, maxHealth=%d]", getHealth(), getMaxHealth());
	}
}
