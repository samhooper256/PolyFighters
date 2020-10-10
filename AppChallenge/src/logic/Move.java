package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A move in the game. A move contains a list of one or more {@link Action}s, which must executed in the order they are provided to the {@code Move}.
 * A {@code Move} is executed on a given board by its {@link #execute(Board)}.
 * @author Sam Hooper
 *
 */
public class Move {
	public static final Move EMPTY_MOVE = new Move() {
		@Override
		public void addAction(Action action) {
			throw new UnsupportedOperationException("Cannot modify the empty move.");
		}
		@Override
		public boolean isEmpty() {
			return true;
		}
	};
	
	private final Ability ability;
	List<Action> actions;
	
	private Move() {
		actions = null;
		ability = null;
	}
	
	public Move(Ability unit, Action... actions) {
		this.ability = unit;
		this.actions = new ArrayList<>(actions.length);
		for(int i = 0; i < actions.length; i++)
			this.actions.add(actions[i]);
	}
	
	public Move(Ability unit) {
		this.ability = unit;
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
	
	public List<Action> getActionsUnmodifiable() {
		return Collections.unmodifiableList(actions);
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
	
	public Ability getAbility() {
		return ability;
	}
	
	public boolean isEmpty() {
		return actions.size() == 0;
	}
	
	@Override
	public String toString() {
		return String.format("Move[actions=%s]", actions);
	}
}
