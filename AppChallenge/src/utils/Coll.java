package utils;

import java.util.*;

/**
 * Utility methods for {@link Collection Collections}.
 * @author Sam Hooper
 *
 */
public final class Coll {
	/**
	 * Returns a random item in the given {@link Collection}. <b>This is an O(n) operation and should be used with care.</b>
	 * @throws IllegalArgumentException if the given {@linkplain Collection} is empty.
	 */
	public static <T> T getRandom(Collection<T> items) {
		int itemSpot = (int) (Math.random() * items.size());
		for(T item : items) {
			if(itemSpot == 0)
				return item;
			itemSpot--;
		}
		throw new IllegalArgumentException("Collection is empty. items.size() == " + items.size());
	}
}
