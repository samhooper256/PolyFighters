package logic.units;

import java.util.*;

import logic.Ability;
import logic.Board;
import logic.TeamUnit;
import logic.TileType;

/**
 * @author Sam Hooper
 *
 */
abstract class AbstractTeamUnit implements TeamUnit {
	
	protected final List<Ability> abilities;
	
	/**
	 * An {@link EnumSet} containing all of the {@link TileType}s that this unit can traverse.
	 * Note that move-based abilities may  also have the ability to restrict the types of tiles that can be traversed. Both the
	 * unit <b>and</b> the ability must allow a tile to be traversed for the action to be legal. Will not be {@code null}.
	 */
	protected final EnumSet<TileType> traversableTileTypes;
	
	protected Board board;
	/** {@code -1} if not on a board. */
	protected int row;
	/** {@code -1} if not on a board. */
	protected int col;
	
	public AbstractTeamUnit() {
		this(null, -1, -1, new ArrayList<>());
	}
	public AbstractTeamUnit(List<Ability> abilities) {
		this(null, -1, -1, abilities);
	}
	public AbstractTeamUnit(Board board, int row, int col) {
		this(board, row, col, new ArrayList<>());
	}
	
	public AbstractTeamUnit(Board board, int row, int col, List<Ability> abilities) {
		this.abilities = Objects.requireNonNull(abilities);
		this.board = Objects.requireNonNull(board);
		this.traversableTileTypes = EnumSet.noneOf(TileType.class);
		this.row = row;
		this.col = col;
	}
	
	@Override
	public List<Ability> getAbilitiesUnmodifiable() {
		return Collections.unmodifiableList(abilities);
	}
	
	@Override
	public Board getBoard() {
		return board;
	}
	
	@Override
	public int getRow() {
		if(!isOnBoard())
			throw new IllegalArgumentException("This unit is not on a board");
		return row;
	}
	
	@Override
	public int getCol() {
		if(!isOnBoard())
			throw new IllegalArgumentException("This unit is not on a board");
		return col;
	}
}
