package utils;

/**
 * @author Sam Hooper
 *
 */
public final class Types {
	/**
	 * Returns {@code true} if the given {@link Class} if a supertype of any of the given objects.
	 */
	public static boolean containsInstanceOf(Iterable<?> objects, Class<?> clazz) {
		for(Object o : objects)
			if(o != null && clazz.isAssignableFrom(o.getClass()))
				return true;
		return false;
	}
}
