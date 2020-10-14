package logic.abilities;

import logic.Ability;
import logic.TileType;

/**
 * @author Sam Hooper
 *
 */
public interface AttackAbility extends Ability {
	
	boolean canAttackFrom(TileType type);
	
}
