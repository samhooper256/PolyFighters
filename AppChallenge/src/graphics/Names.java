package graphics;

import logic.*;

/**
 * Utility class that provides the display names for things that need to be displayed to the user.
 * @author Sam Hooper
 *
 */
public final class Names {
	
	private Names() {}
	
	/**
	 * The given {@link Unit} must not be {@code null}.
	 */
	public static String of(Unit obj) {
		return of(obj.getClass());
	}
	
	/**
	 * The given {@link Class} must not be {@code null}.
	 */
	public static String of(Class<? extends Unit> clazz) {
		return clazz.getSimpleName();
	}
}
