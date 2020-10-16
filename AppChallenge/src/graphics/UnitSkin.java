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
	REX(Rex.class) {
		@Override
		public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
			if(clazz == Shoot.class)
				return bulletInfo.getImage();
			throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
		}
		@Override
		public double[] projectileSizeFor(Class<? extends SingleProjectileAbility> clazz) {
			return new double[] {1,1};
		}
	},
	JULES(Jules.class),
	KOT(Kot.class),
	SCALES(Scales.class) {
		@Override
		public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
			if(clazz == Lob.class)
				return rockInfo.getImage();
			throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
		}
		@Override
		public double[] projectileSizeFor(Class<? extends SingleProjectileAbility> clazz) {
			return new double[] {1,1};
		}
	},
	GOOB(Goob.class) {
		@Override
		public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
			if(clazz == Shoot.class)
				return bulletInfo.getImage();
			throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
		}
		@Override
		public double[] projectileSizeFor(Class<? extends SingleProjectileAbility> clazz) {
			return new double[] {1,1};
		}
	},
	ASSASSIN(Assassin.class),
	BRUTE(Brute.class) {
		@Override
		public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
			if(clazz == Shoot.class)
				return bulletInfo.getImage();
			throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
		}
		@Override
		public double[] projectileSizeFor(Class<? extends SingleProjectileAbility> clazz) {
			return new double[] {1,1};
		}
	},
	LOBBER(Lobber.class) {
		@Override
		public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
			if(clazz == Lob.class)
				return rockInfo.getImage();
			throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
		}
		@Override
		public double[] projectileSizeFor(Class<? extends SingleProjectileAbility> clazz) {
			return new double[] {1,1};
		}
	};
	private static final ImageInfo bulletInfo = new ImageInfo("Bullet.png");
	private static final ImageInfo rockInfo = new ImageInfo("Rock.png");
	private static final Map<Class<? extends Unit>, UnitSkin> map;
	static {
		map = new HashMap<>();
		for(UnitSkin skin : UnitSkin.class.getEnumConstants()) {
			map.put(skin.getUnitClass(), skin);
		}
	}
	
	private static final Map<Class<? extends Unit>, ImageInfo> infoMap;
	
	static {
		infoMap = new HashMap<>();
		infoMap.put(Rex.class, new ImageInfo("Rex.png"));
		infoMap.put(Jules.class, new ImageInfo("Jules.png"));
		infoMap.put(Kot.class, new ImageInfo("Kot.png"));
		infoMap.put(Scales.class, new ImageInfo("Scales.png"));
		infoMap.put(Goob.class, new ImageInfo("Goob.png"));
		infoMap.put(Assassin.class, new ImageInfo("Assassin.png"));
		infoMap.put(Brute.class, new ImageInfo("Brute.png"));
		infoMap.put(Lobber.class, new ImageInfo("Lobber.png"));
		infoMap.put(Summoner.class, new ImageInfo("Summoner.png"));
	}
	
	public static Image imageFor(Unit unit) {
		return imageFor(unit.getClass());
	}
	
	public static Image imageFor(Class<? extends Unit> unitClazz) {
		ImageInfo info = infoMap.get(unitClazz);
		if(info == null)
			throw new IllegalArgumentException("There is no image associated with: " + unitClazz);
		return info.getImage();
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
	
	public Image getImage() {
		return imageFor(unitClass);
	}
	
	public Image projectileImageFor(Class<? extends SingleProjectileAbility> clazz) {
		throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
	}
	
	/**
	 * The given {@link SingleProjectileAbility} must not be {@code null}.
	 */
	public Image projectileImageFor(SingleProjectileAbility ability) {
		Objects.requireNonNull(ability);
		return projectileImageFor(ability.getClass());
	}
	
	/**
	 * Returns a length-2 {@code double} array containing the percentage of a {@link TerrainTile} the projectile should take up.
	 * The returned values are the width percent and the height percent, in that order.
	 */
	public double[] projectileSizeFor(Class<? extends SingleProjectileAbility> clazz) {
		throw new UnsupportedOperationException(clazz + " is not a recognized Ability of the UnitSkin: " + this);
	}
	
	/**
	 * The given {@link SingleProjectileAbility} must not be {@code null}.
	 */
	public double[] projectileSizeFor(SingleProjectileAbility ability) {
		Objects.requireNonNull(ability);
		return projectileSizeFor(ability.getClass());
	}
	
	/**
	 * In degrees north of east.
	 */
	public double getDefaultRotationFor(Class<? extends SingleProjectileAbility> clazz) {
		return 0;
	}
	
	public double getDefaultRotationFor(SingleProjectileAbility ability) {
		Objects.requireNonNull(ability);
		return getDefaultRotationFor(ability.getClass());
	}
}

