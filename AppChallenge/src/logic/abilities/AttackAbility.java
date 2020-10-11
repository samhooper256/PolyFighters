package logic.abilities;

import logic.TileType;

/**
 * @author Sam Hooper
 *
 */
public interface AttackAbility {
	
	boolean canAttackFrom(TileType type);
	
}
