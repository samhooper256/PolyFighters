package logic.units;

import java.util.*;

import logic.Ability;
import logic.Board;
import logic.TeamUnit;
import logic.TileType;

/**
 * Abstract base class that classes implementing {@link TeamUnit} may extend to make implementation easier.
 * 
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
	
	protected AbstractTeamUnit() {
		this(null, -1, -1, new ArrayList<>());
	}
	protected AbstractTeamUnit(List<Ability> abilities) {
		this(null, -1, -1, abilities);
	}
	protected AbstractTeamUnit(Board board, int row, int col) {
		this(board, row, col, new ArrayList<>());
	}
	
	/**
	 * 
	 * @param board the {@code Board} that this {@code Unit} is associated with, or {@code null} if this unit is not yet assoicated with one.
	 * @param row
	 * @param col
	 * @param abilities the list of this {@code Unit}'s abilities. Must not be {@code null}.
	 */
	protected AbstractTeamUnit(Board board, int row, int col, List<Ability> abilities) {
		this.abilities = Objects.requireNonNull(abilities);
		this.board = board;
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
	public void setBoard(Board board) {
		this.board = board;
	}
	
	/**
	 * Returns the 0-based row index of this {@code Unit} on its {@link Board}, or {@code -1} if this {@code Unit} is not on a {@code Board}.
	 */
	@Override
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns the 0-based column index of this {@code Unit} on its {@link Board}, or {@code -1} if this {@code Unit} is not on a {@code Board}.
	 */
	@Override
	public int getCol() {
		return col;
	}
	
	@Override
	public void setRow(int row) {
		this.row = row;
	}
	
	@Override
	public void setCol(int col) {
		this.col = col;
	}
}
