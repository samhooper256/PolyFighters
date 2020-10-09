package logic.abilities;

import logic.Ability;
import logic.Board;
import logic.GameObject;
import logic.Move;

/**
 * <p>An {@link Ability} that requires a target to be used. This target may be the same as the unit using the {@code Ability}. This interface defines
 * one new method, {@link #canTarget(GameObject)}, and <b>further restricts the contract of</b> {@link Ability#createMoveFor(int, int, logic.GameObject)}.</p>
 * @author Sam Hooper
 *
 */
public interface TargetingAbility extends Ability {
	
	/**
	 * Returns {@code true} if the given {@link GameObject} could be the target of a use of this {@link TargetAbility}, {@code false} otherwise.
	 * Behavior is undefined if the given {@code GameObject} is {@code null}.
	 */
	boolean canTarget(GameObject object);
	
	/**
	 * Creates a {@link Move} that when executed (by invoking {@link Move#execute(Board)}) will use this {@link TargetingAbility} on the appropriate
	 * {@link Board}. The destination tile (that is, the tile the user selects in order to use this {@code Ability}) will be given by
	 * the first two parameters. The {@link GameObject} parameter is the "target" of this {@code Ability} use. It must be a {@code GameObject}
	 * on the destination tile. <b>The target must not be null.</b> Behavior of this method is undefined if the destination row and column do not indicate a legal move
	 * or the target is not on the destination tile.
	 * @throws NullPointerException if {@code target} is {@code null}.
	 */
	@Override
	Move createMoveFor(int destRow, int destCol, GameObject target);
	
}
