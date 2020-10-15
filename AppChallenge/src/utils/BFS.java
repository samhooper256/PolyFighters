package utils;

import java.util.*;
import java.util.function.Predicate;

/**
 * This class is not safe for use by multiple concurrent threads.
 * <ul>
 * <li><b>Include Start:</b> if {@code true}, the start tile will be where the search starts. This means that the search will terminate with
 * no result if the stop predicate evaluates to {@code true} on the start tile, and the search will terminate with a target of the start tile
 * if the target predicate evaluates to {@code true} on the start tile. If include start if {@code false}, the start tile will not be included
 * in the search. The predicates (stop and target) will never be evaluated on the start tile.</li>
 * <li><b>Stop Predicate:</b> A {@link Predicate} that evaluates to {@code true} if the search is not allowed to access a tile, {@code false} otherwise.
 * If not specified, a {@link Predicate} that always returns {@code false} will be used. If the stop predicate evaluates to {@code true} on a tile, that
 * tile cannot be the target, even if the target predicate evaluates to {@code true} on that tile. (In this sense, the stop predicate "takes precedence" over the
 * target predicate).</li>
 * <li><b>Target Predicate:</b> A {@link Predicate} that returns {@code true} if the given tile is the target of this search (in other words, that tile is the thing
 * this BFS is searching for), {@code false} otherwise.</li>
 * </ul>
 * @author Sam Hooper
 *
 */
public final class BFS<T> {
	public static <S> BFS<S> of(final S[][] grid, final int startRow, final int startCol, Predicate<? super S> targetPredicate) {
		return new BFS<>(grid, startRow, startCol, targetPredicate);
	}
	
	public static <S> BFS<S> of(final S[][] grid, final int startRow, final int startCol, Predicate<? super S> targetPredicate, Predicate<? super S> stopPredicate) {
		return new BFS<>(grid, startRow, startCol, targetPredicate, stopPredicate);
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
		
		/**
		 * Returns the number of {@link PathNode PathNodes} that follow this one in its path, including this one. (For example,
		 * if this {@link PathNode PathNode's} parent is {@code null}, returns {@code 1}). This method runs in O(n) where n
		 * is the length of the path.
		 */
		public int length() {
			int count = 1;
			PathNode ptr = this;
			while(ptr.parent != null) {
				count++;
				ptr = ptr.parent;
			}
			return count;
		}
	}
	
	private static final int[][] DEFAULT_MOVE_SET = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	private static final Predicate<Object> TRUE_PREDICATE = o -> true, FALSE_PREDICATE = o -> false;
	
	private final T[][] grid;
	private final int startRow, startCol;
	private final Predicate<? super T> targetPredicate;
	private Predicate<? super T> stopPredicate;
	private int[][] moveSet;
	private boolean includeStart;
	private boolean[][] vis;
	private boolean searchCalled = false;
	
	private BFS(T[][] grid, final int startRow, final int startCol, Predicate<? super T> targetPredicate, Predicate<? super T> stopPredicate) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(targetPredicate);
		Objects.requireNonNull(stopPredicate);
		this.grid = grid;
		this.startRow = startRow;
		this.startCol = startCol;
		this.targetPredicate = targetPredicate;
		this.stopPredicate = stopPredicate;
		this.moveSet = DEFAULT_MOVE_SET;
	}
	
	private BFS(T[][] grid, final int startRow, final int startCol, Predicate<? super T> targetPredicate) {
		this(grid, startRow, startCol, targetPredicate, FALSE_PREDICATE);
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BFS<T> setIncludeStart(boolean value) {
		this.includeStart = value;
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 */
	public BFS<T> setStopPredicate(Predicate<? super T> stopPredicate) {
		Objects.requireNonNull(stopPredicate);
		this.stopPredicate = stopPredicate;
		return this;
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
		ArrayDeque<PathNode> spotsToVisit = new ArrayDeque<>(); //all spots in here are assumed to be in bounds and not false in the vis array.
		//They are also assumed to not be the target, and it is further assumed that the stop predicate returns false for these tiles.
		final PathNode startNode = new PathNode(startRow, startCol, null);
		if(includeStart) {
			if(stopPredicate.test(grid[startRow][startCol]))
				return Result.emptyResult();
			if(targetPredicate.test(grid[startRow][startCol]))
				return new Result<>(startNode, grid[startRow][startCol]);
			spotsToVisit.add(startNode);
		}
		else {
			vis[startRow][startCol] = true;
			for(int[] move : moveSet) {
				final int nr = startRow + move[0], nc = startCol + move[1];
				if(inBounds(nr, nc) && !stopPredicate.test(grid[nr][nc])) {
					final PathNode node = new PathNode(nr, nc, startNode);
					if(targetPredicate.test(grid[nr][nc]))
						return new Result<>(node, grid[nr][nc]);
					spotsToVisit.add(node);
				}
			}
		}
		while(!spotsToVisit.isEmpty()) {
			PathNode node = spotsToVisit.remove(); //node assumed to be in bounds, vis[node.getRow()][node.getCol()] == false
			final int row = node.getRow(), col = node.getCol();
			vis[row][col] = true;
			for(int[] move : moveSet) {
				final int nr = row + move[0], nc = col + move[1];
				if(inBounds(nr, nc) && !vis[nr][nc] && !stopPredicate.test(grid[nr][nc])) {
					final PathNode newNode = new PathNode(nr, nc, node);
					if(targetPredicate.test(grid[nr][nc]))
						return new Result<>(newNode, grid[nr][nc]);
					spotsToVisit.add(newNode);
				}
			}
		}
		return Result.emptyResult();
	}
	
	private boolean inBounds(int row, int col) {
		return row >= 0 && row < grid.length && col >= 0 && col < grid[row].length;
	}
}
