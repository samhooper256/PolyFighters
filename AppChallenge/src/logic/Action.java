package logic;

/**
 * An action that can be used with 0 or more other actions to form a {@link Move}. An action can be executed on a {@link Board}
 * by calling the {@link #execute(Board)} method.
 * 
 * @author Sam Hooper
 *
 */
public interface Action {
	/**
	 * Executes this {@code Action} on the given {@link Board}.
	 */
	void execute(Board board);
}
