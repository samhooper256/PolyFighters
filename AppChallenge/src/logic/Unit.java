package logic;

import java.util.Collection;

import utils.CollectionRef;

/**
 * </p>A unit on a {@link Board}. This interface must not be implemented directly; rather, one of its subinterfaces {@link PlayerUnit} or {@link EnemyUnit}
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
public interface Unit extends HasHealth {
	
	/**
	 * Returns an unmodifiable {@link Collection} containing this {@code Unit's} abilities.
	 */
	default Collection<Ability> getAbilitiesUnmodifiable() {
		return abilityCollectionRef().getUnmodifiable();
	}
	
	/**
	 * Returns a {@link CollectionRef} referring to a {@link Collection} that contains all of this {@link Unit Unit's} {@link Ability Abilities}.
	 */
	CollectionRef<Ability> abilityCollectionRef();
	
	/**
	 * Returns {@code true} if this {@code Unit} can traverse {@link BoardTile}s of the given {@link TileType}.
	 * */
	boolean canTraverse(TileType type);
	
	/**
	 * Returns a {@link Collection} of (row, col) ordered pairs. Each ordered
	 * pair is a destination tile where this unit could legally use the given ability.
	 * @param ability
	 * @throws IllegalArgumentException if this unit does not have the given ability.
	 */
	Collection<int[]> getLegalSpots(Ability ability);
	
	/**
	 * The {@link Turn} this {@link Unit} plays on.
	 */
	Turn playingTurn();
	
}
