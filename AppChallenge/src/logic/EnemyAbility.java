package logic;

/**
 * An {@link Ability} that only {@link EnemyUnit}s can use.
 * @author Sam Hooper
 *
 */
public interface EnemyAbility extends Ability {
	@Override
	EnemyUnit getUnit();
}
