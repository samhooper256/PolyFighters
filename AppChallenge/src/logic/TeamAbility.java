package logic;

/**
 * An {@link Ability} that only {@link TeamUnit}s can use.
 * @author Sam Hooper
 *
 */
public interface TeamAbility extends Ability {
	@Override
	TeamUnit getUnit();
}
