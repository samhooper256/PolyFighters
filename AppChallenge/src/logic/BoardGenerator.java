package logic;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Sam Hooper
 *
 */
public class BoardGenerator {
	public static final int DEFAULT_ROW_COUNT = 8;
	public static final int DEFAULT_COL_COUNT = 8;
	public static final double DEFAULT_LIQUID_PERCENT = 0.2;
	public static final double DEFAULT_POOL_STRENGTH = 0.4;
	
	private static final int[][] ADJACENTS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	private int rowCount, colCount;
	private double liquidPercent;
	private double poolStrength;
	
	public BoardGenerator() {
		this.rowCount = DEFAULT_ROW_COUNT;
		this.colCount = DEFAULT_COL_COUNT;
		this.liquidPercent = DEFAULT_LIQUID_PERCENT;
		this.poolStrength = DEFAULT_POOL_STRENGTH;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setRowCount(int rowCount) {
		this.rowCount = rowCount;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setColumnCount(int colCount) {
		this.colCount = colCount;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setLiquidPercent(double liquidPercent) {
		this.liquidPercent = liquidPercent;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BoardGenerator setPoolStrength(double poolStrength) {
		this.poolStrength = poolStrength;
		return this;
	}
	
	private int liquidRemainng;
	
	public Board build() {
		final Board board = new Board(rowCount, colCount);
		liquidRemainng = (int) Math.round(rowCount * colCount * liquidPercent);
		int[] liquifiedSpots = IntStream.range(0, rowCount * colCount).toArray();
		int liquifiedSpotsMaxUsableIndex = liquifiedSpots.length - 1;
		liquid_gen:
		while(liquidRemainng > 0) {
			int startRow, startCol;
			do {
				if(liquifiedSpotsMaxUsableIndex < 0)
					break liquid_gen;
				int liquifiedIndex;
				int liquifiedValue;
				{
					liquifiedIndex = (int) (Math.random() * (liquifiedSpotsMaxUsableIndex + 1));
					liquifiedValue = liquifiedSpots[liquifiedIndex];
				}
				startRow = liquifiedValue / colCount;
				startCol = liquifiedValue % colCount;
				liquifiedSpots[liquifiedIndex] = liquifiedSpots[liquifiedSpotsMaxUsableIndex];
				liquifiedSpots[liquifiedSpotsMaxUsableIndex] = liquifiedValue;
				liquifiedSpotsMaxUsableIndex--;
			} while(board.getTileAt(startRow, startCol).getType() == TileType.LIQUID);
			board.getTileAt(startRow, startCol).setType(TileType.LIQUID);
			liquidRemainng--;
			if(liquidRemainng <= 0)
				break liquid_gen;
			Queue<int[]> toVisit = new LinkedList<>();
			for(int[] adj : ADJACENTS) {
				int r = startRow + adj[0], c = startCol + adj[1];
				if(r >= 0 && r < rowCount && c >= 0 && c < colCount && board.getTileAt(r, c).getType() != TileType.LIQUID)
					toVisit.add(new int[] {r, c});
			}
			while(!toVisit.isEmpty()) {
				int[] spot = toVisit.remove();
				BoardTile tile = board.getTileAt(spot[0], spot[1]);
				if(Math.random() < poolStrength) {
					tile.setType(TileType.LIQUID);
					liquidRemainng--;
					if(liquidRemainng <= 0)
						break liquid_gen;
				}
				else {
					continue;
				}
				
				for(int[] adj : ADJACENTS) {
					int nr = spot[0] + adj[0], nc = spot[1] + adj[1];
					if(nr < 0 || nr >= rowCount || nc < 0 || nc >= colCount)
						continue;
					BoardTile newTile = board.getTileAt(nr, nc);
					if(newTile.getType() == TileType.LIQUID)
						continue;
					toVisit.add(new int[] {nr, nc});
				}
			}
		}
		return board;
	}
}
