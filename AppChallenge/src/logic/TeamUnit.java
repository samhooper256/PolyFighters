package logic;

import java.util.List;
import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public abstract class TeamUnit implements Unit {
	List<Ability> abilities;
	
	public TeamUnit(List<Ability> abilities) {
		this.abilities = Objects.requireNonNull(abilities);
	}
	
	@Override
	public List<Ability> getAbilities() {
		return abilities;
	}
}
