package logic;

import java.util.Collection;

/**
 * </p>A unit on a {@link Board}. This interface must not be implemented directly; rather, one of its subinterfaces {@link TeamUnit} or {@link EnemyUnit}
 * should be implemented instead.</p>
 * 
 * <p>Every {@code Unit} may optionally be associated with a single {@link Board}, which can be retrieved by {@link #getBoard()}. The {@code Board} a
 * {@code Unit} is associated with must be the {@code Board} that that unit is on. {@link #getBoard()} returns {@code null} if the {@code Unit} is not associated
 * with a {@code Board}.</p>
 * 
 * <p>Every {@code Unit} has a row and column value that can be set and retrieved using the {@link #getRow()}, {@link #getCol()},
 * {@link #setRow()}, {@link #setCol()} methods. These row and column values represent the location of the {@code Unit} on its {@code Board}.
 * Behavior of the getter methods is undefined if the {@code Unit} is not on a {@code Board}. Note that the method {@link #hasBoard()} can
 * be used to check if a {@code Unit} is on a {@code Board}.</p>
 * 
 * <p>Every {@code Unit} has a set of abilities, represented by a {@link Collection} of {@code Ability} objects that can be retrieved by calling {@link #getAbilitiesUnmodifiable()}.
 * A {@code Unit}'s {@code Collection} of abilities must contain no two abilities <b>a</b> and <b>b</b> such that {@code a == b}.</p>
 * 
 * <p>A {@code Unit} <b>U</b> <i>can traverse</i> a {@link TileType} <b>T</b> if and only if {@code U.canTraverse(T)} returns {@code true}.
 * (See {@link #canTraverse(TileType)}).</p>
 * 
 * @author Sam Hooper
 *
 */
public interface Unit extends GameObject {
	
	/**
	 * @return an unmodifiable {@link Collection} containing this {@code Unit's} abilities.
	 */
	Collection<Ability> getAbilitiesUnmodifiable();
	
	/** Returns {@code true} if this {@code Unit} can traverse {@link BoardTile}s of the given {@link TileType}. */
	boolean canTraverse(TileType type);
	
	/**
	 * Returns the {@link Board} that this unit is on, or {@code null} if this unit is not on a {@code Board}. If this
	 * method does not return {@code null} for a {@code Unit} <b>U</b>,
	 * <pre><code>U.getBoard().isOnBoard(U)</code></pre>
	 * is {@code true}.
	 */
	Board getBoard();
	
	/** Sets the {@link Board} that this unit is associated with. */
	void setBoard(Board board);
	
	/**
	 * Returns the 0-based index of the row of this {@code Unit} on its {@link Board}, unless this {@code Unit} is not on a {@code Board}, in which
	 * case this method's behavior is undefined.
	 * @return the row of this {@code Unit} on its {@link Board}.
	 */
	int getRow();
	
	/**
	 * Returns the 0-based index of the column of this {@code Unit} on its {@link Board}, unless this {@code Unit} is not on a {@code Board}, in which
	 * case this method's behavior is undefined.
	 * @return the column of this {@code Unit} on its {@link Board}.
	 */
	int getCol();
	
	/** 
	 * Sets the row value of this {@code Unit}. The given value <b>must</b> line up with its actual location on its associated {@link Board} at the time of
	 * invocation as described by {@link #getRow()}.
	 */
	void setRow(int row);
	
	/** 
	 * Sets the row value of this {@code Unit}. The given value <b>must</b> line up with its actual location on its associated {@link Board} at the time of
	 * invocation as described by {@link #getCol()}.
	 */
	void setCol(int col);
	
	/** Returns {@code true} if this {@code Unit} is currently associated with a {@link Board}, {@code false} otherwise. */
	default boolean hasBoard() {
		return getBoard() != null;
	}
	
}
