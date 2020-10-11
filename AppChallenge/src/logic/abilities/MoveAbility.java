package logic.abilities;

import logic.Ability;
import logic.TileType;

/**
 * A marker interface for any {@link Ability} whose legal moves are movements.
 * @author Sam Hooper
 *
 */
public interface MoveAbility extends Ability {
	boolean canTraverse(TileType type);
}
