package utils;

import java.util.*;
import java.util.function.Predicate;

/**
 * This class is not safe for use by multiple concurrent threads.
 * @author Sam Hooper
 *
 */
public final class BFS<T> {
	public static <S> BFS<S> of(final S[][] grid, final int startRow, final int startCol, Predicate<S> targetPredicate) {
		return new BFS<>(grid, startRow, startCol, targetPredicate);
	}
	
	public static class Result<S> {
		@SuppressWarnings("rawtypes")
		public static final Result EMPTY_RESULT = new Result<>(null, null);
		@SuppressWarnings("unchecked")
		public static final <U> Result<U> emptyResult() {
			return EMPTY_RESULT;
		}
		
		private PathNode start;
		private S target;
		public Result(PathNode start, S target) {
			this.start = start;
			this.target = target;
		}
		
		/**
		 * Returns {@code true} if the {@link BFS} that produced this {@link Result} found something in its search, {@code false} otherwise.
		 */
		public boolean found() {
			return start != null;
		}
		
		public PathNode startNode() {
			if(!found())
				throw new IllegalArgumentException("Cannot get start PathNode because the BFS did not find anything.");
			return start;
		}
		
		public S target() {
			return target;
		}
	}
	
	public static class PathNode {
		private final PathNode parent;
		private final int row, col;
		
		public PathNode(int[] spot) {
			this.row = spot[0];
			this.col = spot[1];
			this.parent = null;
		}
		
		public PathNode(int row, int col) {
			this.row = row;
			this.col = col;
			this.parent = null;
		}
		
		public PathNode(int row, int col, PathNode parent) {
			this.row = row;
			this.col = col;
			this.parent = parent;
		}
		
		public PathNode getParent() {
			return parent;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getCol() {
			return col;
		}
	}
	
	private static final int[][] DEFAULT_MOVE_SET = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	
	private final T[][] grid;
	private final int startRow, startCol;
	private final Predicate<T> targetPredicate;
	private int[][] moveSet;
	private boolean includeStart;
	private boolean[][] vis;
	private boolean searchCalled = false;
	
	private BFS(T[][] grid, final int startRow, final int startCol, Predicate<T> targetPredicate) {
		this.grid = grid;
		this.startRow = startRow;
		this.startCol = startCol;
		this.targetPredicate = targetPredicate;
		this.moveSet = DEFAULT_MOVE_SET;
	}
	
	public void setIncludeStart(boolean value) {
		this.includeStart = value;
	}
	/**
	 * Can only be called once on any {@link BFS}.
	 * @return
	 */
	public Result<T> search() {
		if(searchCalled)
			throw new IllegalStateException("This BFS has already been searched");
		searchCalled = true;
		if(grid.length == 0 || grid[0].length == 0)
			return Result.emptyResult();
		vis = new boolean[grid.length][grid[0].length];
		ArrayDeque<PathNode> spotsToVisit = new ArrayDeque<>(); //all spots in here are assumed to be in bounds and not false in the vis array. They are also assumed to not be the target.
		final PathNode startNode = new PathNode(startRow, startCol, null);
		if(includeStart) {
			if(targetPredicate.test(grid[startRow][startCol]))
				return new Result<>(startNode, grid[startRow][startCol]);
			spotsToVisit.add(startNode);
		}
		else {
			vis[startRow][startCol] = true;
			for(int[] move : moveSet) {
				final int nr = startRow + move[0], nc = startCol + move[1];
				if(inBounds(nr, nc)) {
					final PathNode node = new PathNode(nr, nc, startNode);
					if(targetPredicate.test(grid[nr][nc]))
						return new Result<>(node, grid[nc][nc]);
					spotsToVisit.add(node);
				}
			}
		}
		while(!spotsToVisit.isEmpty()) {
			PathNode node = spotsToVisit.remove(); //node assumed to be in bounds, vis[node.getRow()][node.getCol()] = false
			final int row = node.getRow(), col = node.getCol();
			vis[row][col] = true;
			for(int[] move : moveSet) {
				final int nr = row + move[0], nc = col + move[1];
				if(inBounds(nr, nc) && !vis[nr][nc]) {
					final PathNode newNode = new PathNode(nr, nc, node);
					if(targetPredicate.test(grid[nr][nc]))
						return new Result<>(newNode, grid[nr][nc]);
					spotsToVisit.add(newNode);
				}
			}
		}
		Collections.emptySet();
		return Result.emptyResult();
	}
	
	private boolean inBounds(int row, int col) {
		return row >= 0 && row < grid.length && col >= 0 && col < grid[row].length;
	}
}
