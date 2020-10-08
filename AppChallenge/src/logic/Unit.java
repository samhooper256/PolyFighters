package logic;

import java.util.Collection;

import utils.CollectionRef;

/**
 * </p>A unit on a {@link Board}. This interface must not be implemented directly; rather, one of its subinterfaces {@link TeamUnit} or {@link EnemyUnit}
 * should be implemented instead.</p>
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
	default Collection<Ability> getAbilitiesUnmodifiable() {
		return abilityCollectionRef().getUnmodifiable();
	}
	
	CollectionRef<Ability> abilityCollectionRef();
	
	
	
	/** Returns {@code true} if this {@code Unit} can traverse {@link BoardTile}s of the given {@link TileType}. */
	boolean canTraverse(TileType type);
	
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
	
}
