package logic;

import java.util.*;
import java.util.function.Supplier;
import logic.units.*;

/**
 * @author Sam Hooper
 *
 */
public final class EnemyGenerator {
	private static Map<Class<? extends EnemyUnit>, EnemyFactory> map;
	static {
		map = new HashMap<>();
		put(Goob.class, Goob::new, 1);
		put(Assassin.class, Assassin::new, 1.5);
		put(Brute.class, Brute::new, 2.5);
//		put(Summoner.class, Summoner::new, 2);
		put(Lobber.class, Lobber::new, 2);
	}
	
	private static <T extends EnemyUnit> void put(Class<? extends T> enemyClass, Supplier<T> enemySupplier, double difficulty) {
		map.put(enemyClass, new EnemyFactory(enemyClass, enemySupplier, difficulty));
	}
	
	public static final class EnemyFactory {
		private final Class<? extends EnemyUnit> enemyClass;
		private final Supplier<? extends EnemyUnit> enemySupplier;
		private final double difficulty;
		
		public EnemyFactory(Class<? extends EnemyUnit> enemyClass, Supplier<? extends EnemyUnit> enemySupplier,
				double difficulty) {
			super();
			this.enemyClass = enemyClass;
			this.enemySupplier = enemySupplier;
			this.difficulty = difficulty;
		}
		
		public double getDifficulty() {
			return difficulty;
		}
		
		public EnemyUnit make() {
			return enemySupplier.get();
		}
		
		public Class<? extends EnemyUnit> getEnemyClass() {
			return enemyClass;
		}
	}
	
	public static int enemyCount() {
		return map.size();
	}
	
	/**
	 * Returns an unmodifiable view of the {@link EnemyFactory EnemyFactories}.
	 * @return
	 */
	public static Collection<EnemyFactory> factories() {
		return Collections.unmodifiableCollection(map.values());
	}
}
