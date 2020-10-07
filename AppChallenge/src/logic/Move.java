package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * A move in the game. A move consists of one or more {@link Action}s, which are executed in the order they are provided to the {@code Move}.
 * A {@code Move} is executed on a given board by its {@link #execute(Board)}.
 * @author Sam Hooper
 *
 */
public final class Move {
	
	List<Action> actions;
	
	public Move(Action... actions) {
		this.actions = new ArrayList<>(actions.length);
		for(int i = 0; i < actions.length; i++)
			this.actions.add(actions[i]);
	}
	
	public Move() {
		actions = new ArrayList<>();
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	/**
	 * Returns {@code true} if this {@code Move} had the given {@link Action} and it has been removed, {@code false} otherwise.
	 */
	public boolean removeAction(Action action) {
		return actions.remove(action);
	}
	
	public int getActionCount() {
		return actions.size();
	}
	
	/**
	 * Executes this {@code Move} on the given {@link Board}.
	 * @param board
	 */
	public void execute(Board board) {
		for(Action a : actions)
			a.execute(board);
	}
}
