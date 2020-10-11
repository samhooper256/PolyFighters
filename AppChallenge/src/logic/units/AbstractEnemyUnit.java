package logic.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import logic.Ability;
import logic.Board;
import logic.EnemyUnit;
import logic.TeamUnit;
import logic.Unit;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractEnemyUnit extends AbstractUnit implements EnemyUnit {
	
	protected AbstractEnemyUnit(int maxHealth, int currentHealth) {
		super(maxHealth, currentHealth);
	}
	
	protected AbstractEnemyUnit(int maxHealth) {
		super(maxHealth);
	}

	protected AbstractEnemyUnit(Board board, int row, int col, int maxHealth, int currentHealth,
			List<Ability> abilities) {
		super(board, row, col, maxHealth, currentHealth, abilities);
	}
	
	/**
	 *	The returned array contains (rowCounts, colCounts).
	 */
	protected int[][] getRowColCountsOfTeamUnits(Board board) {
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
		return new int[][] {rowUnitCounts, colUnitCounts};
	}
	
	protected Collection<TeamUnit> teamUnitsVisibleFrom(Board board, final int startRow, final int startCol) {
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
	
	protected Collection<TeamUnit> teamUnits8Adjacent(Board board, int[] spot) {
		return teamUnits8Adjacent(board, spot[0], spot[1]);
	}
	
	/**
	 * Returns a {@link Collection} containg the {@link TeamUnit TeamUnits} that on the 8 adjacent tiles to the given one.
	 */
	protected Collection<TeamUnit> teamUnits8Adjacent(Board board, int row, int col) {
		List<TeamUnit> list = new ArrayList<>(8);
		for(int[] adj : Board.ADJACENT_8) {
			int nr = row + adj[0], nc = col + adj[1];
			Unit unit;
			if(board.inBounds(nr, nc) && (unit = board.getUnitAtOrNull(nr, nc)) instanceof TeamUnit)
				list.add((TeamUnit) unit);
		}
		return list;
	}
	
	/**
	 * Returns the spot in the given {@link Collection} where the sum of the number of {@link TeamUnit}s on that spot's row and the number of {@code TeamUnit}s
	 * on that spot's column is minimized. Returns {@code null} if {@code legals} is empty.
	 */
	protected int[] leastVisibleSpotOf(final Collection<int[]> legals, int[][] rowColCountsOfTeamUnits) {
		int[] pref = null;
		int bestScore = Integer.MAX_VALUE;
		for(int[] legal : legals) {
			final int score = rowColCountsOfTeamUnits[0][legal[0]] + rowColCountsOfTeamUnits[1][legal[1]];
			if(score <  bestScore) {
				pref = legal;
				bestScore = score;
			}
		}
		return pref;
	}
	
	@Override
	public String toString() {
		return String.format("EnemyUnit[health=%d, maxHealth=%d]", health.get(), maxHealth.get());
	}
}
