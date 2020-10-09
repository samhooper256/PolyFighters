package graphics;

/**
 * Something that can be highlighted.
 * @author Sam Hooper
 *
 */
public interface Highlightable {
	
	/**
	 * If this {@link Highlightable} is already highlighted, does nothing.
	 */
	void highlight();
	
	/**
	 * If this {@link Highlightable} is already not highlighted, does nothing.
	 */
	void clearHighlight();
	
	boolean isHighlighted();
	
}
