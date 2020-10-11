package logic.units;

import java.util.*;
import java.util.stream.Collectors;

import logic.*;
import logic.abilities.Shoot;
import logic.abilities.StepMove;
import utils.BooleanRef;
import utils.CollectionRef;
import utils.IntRef;

/**
 * @author Sam Hooper
 *
 */
public class Goob extends AbstractEnemyUnit {
	
	public static final String NAME = "Goob";
	
	private static final int DEFAULT_MAX_HEALTH = 2;
	private static final int DEFAULT_MOVE_DISTANCE = 3;
	private static final int DEFAULT_SHOOT_DAMAGE = 1;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final StepMove stepMoveAbility;
	private final Shoot shootAbility;
	
	public Goob() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.shootAbility = new Shoot(this, DEFAULT_SHOOT_DAMAGE);
		this.abilities.addAll(stepMoveAbility, shootAbility);
	}
	
	public StepMove getStepMoveAbility() {
		return stepMoveAbility;
	}
	
	public Shoot getShootAbility() {
		return shootAbility;
	}

	/**
	 * If this {@link Goob} can shoot a {@link TeamUnit} from its current position, it does so. Otherwise, if it has more than one move
	 * remaining, it tries to get into a position where it could shoot a player's unit. Otherwise, if there's only one move remaining, it
	 * tries to get into a position where it is not on the same row or column as any of the player's units.
	 */
	@Override
	public Move chooseMove(final Board board, final int movesRemaining) {
		final Collection<int[]> stepMoveLegals = stepMoveAbility.getLegals();
		final Collection<int[]> shootLegals = shootAbility.getLegals();
		final int myRow = getRow(), myCol = getCol();
		System.out.printf("\tentered Goob::chooseMove for Goob@(%d,%d), shootLegals=%s%n", row, col, 
				shootLegals.stream().map(Arrays::toString).collect(Collectors.joining(", ","[","]")
				));
		if(shootLegals.size() > 0) {
			int[] min = null;
			Unit minUnit = null;
			for(int[] legal : shootLegals) {
				Unit unit = board.getUnitAtOrNull(legal[0], legal[1]);
				if(unit == null || unit instanceof EnemyUnit)
					continue;
				if(minUnit == null || unit.getHealth() > minUnit.getHealth()) {
					minUnit = unit;
					min = legal;
				}
			}
			if(minUnit != null)
				return shootAbility.createMoveFor(min, minUnit);
		}
		int[] rowUnitCounts = new int[board.getRows()]; //do NOT include this unit
		int[] colUnitCounts = new int[board.getCols()]; //do NOT include this unit
		for(int i = 0; i < board.getRows(); i++) {
			for(int j = 0; j < board.getCols(); j++) {
				if(board.getUnitAtOrNull(i, j) instanceof TeamUnit) {
					rowUnitCounts[i]++;
					colUnitCounts[j]++;
				}
			}
		}
		if(movesRemaining == 1) {
			int[] pref = null;
			int bestScore = Integer.MAX_VALUE;
			for(int[] legal : stepMoveLegals) {
				final int score = rowUnitCounts[legal[0]] + colUnitCounts[legal[1]];
				if(score <  bestScore) {
					pref = legal;
					bestScore = score;
				}
			}
			if(pref == null)
				return Move.EMPTY_MOVE;
			return stepMoveAbility.createMoveFor(pref, null);
		}
		System.out.printf("\t\tcase 3: nothing to shoot rn, but more than one move:%n");
		// more than one move remaining, but there's nothing we can shoot immediately:
		int[] pref = null;
		int fewestOptions = Integer.MAX_VALUE; //search for the spot with the fewest (non-zero) number of units attackable, and attack there. That way we're not in the crossfire of several units.
		for(int[] legal : stepMoveLegals) {
			int options = teamUnitsVisibleFrom(board, legal[0], legal[1]).size();
			if(options > 0 && options < fewestOptions) {
				pref = legal;
				fewestOptions = options;
			}
		}
		System.out.printf("\t\tpref=%s, fewestOptions=%s%n", Arrays.toString(pref), fewestOptions);
		if(pref == null)
			return Move.EMPTY_MOVE;
		return stepMoveAbility.createMoveFor(pref, null);
	}
	
	private Collection<TeamUnit> teamUnitsVisibleFrom(Board board, final int startRow, final int startCol) {
		final int[][] deltas = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};
		ArrayList<TeamUnit> unitsList = new ArrayList<>(4);
		outer:
		for(int[] delta : deltas) {
			int dr = delta[0], dc = delta[1];
			int r = startRow + dr, c = startCol + dc;
			while(board.inBounds(r, c)) {
				if(board.hasObstacle(r, c))
					continue outer;
				Unit unit = board.getUnitAtOrNull(r, c);
				if(unit instanceof TeamUnit) {
					unitsList.add((TeamUnit) unit);
					continue outer;
				}
				else if(unit != null) {
					continue outer;
				}
				r += dr;
				c += dc;
			}
		}
		return unitsList;
	}
	
	private boolean isClearPath(final Board board, int myRow, int myCol, int destRow, int destCol) {
		int dr = myCol == destCol ? 0 : (myRow < destRow ? 1 : -1);
		int dc = myRow == destRow ? 0 : (myCol < destCol ? 1 : -1);
		int r = myCol + dr, c = myCol + dc;
		while(r != destRow || c != destCol) {
			if(board.hasUnit(r, c) || board.hasObstacle(r, c))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Goob@%h", this);
	}
	
}

