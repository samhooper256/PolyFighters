package logic;

/**
 * An {@link Ability} that only {@link PlayerUnit}s can use.
 * @author Sam Hooper
 *
 */
public interface PlayerAbility extends Ability {
	@Override
	PlayerUnit getUnit();
}
