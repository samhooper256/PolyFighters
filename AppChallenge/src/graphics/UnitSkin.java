package graphics;

import java.util.*;

import javafx.scene.image.Image;

import logic.Unit;
import logic.abilities.*;
import logic.units.*;

/**
 * @author Sam Hooper
 *
 */
public enum UnitSkin {
	DEFAULT_SKIN(Unit.class),
	BASIC_UNIT(BasicUnit.class) {
		@Override
		public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
			if(clazz == Shoot.class)
				return basicBullet.getImage();
			throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
		}
	};
	private static final ImageInfo basicBullet = new ImageInfo("BasicBullet.png");
	private static final Map<Class<? extends Unit>, UnitSkin> map;
	static {
		map = new HashMap<>();
		for(UnitSkin skin : UnitSkin.class.getEnumConstants()) {
			map.put(skin.getUnitClass(), skin);
		}
	}
	
	public static UnitSkin forUnitOrDefault(Unit unit) {
		Objects.requireNonNull(unit);
		return forUnitOrDefault(unit.getClass());
	}
	
	/**
	 * Returns the {@code UnitSkin} corresponding to {@link Class} object given, or the {@link #DEFAULT_SKIN default skin}
	 * if there is no defined {@code UnitSkin} for that {@code Class}.
	 * @param clazz
	 * @return
	 */
	public static UnitSkin forUnitOrDefault(Class<? extends Unit> clazz) {
		UnitSkin skin = map.get(clazz);
		if(skin == null)
			return DEFAULT_SKIN;
		return skin;
	}
	
	private final Class<? extends Unit> unitClass;
	
	UnitSkin(final Class<? extends Unit> clazz) {
		this.unitClass = clazz;
	}
	
	public Class<? extends Unit> getUnitClass() {
		return unitClass;
	}
	
	public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
		throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
	}
}
