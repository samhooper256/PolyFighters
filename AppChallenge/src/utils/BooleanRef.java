package utils;

import java.util.ArrayList;

/**
 * @author Sam Hooper
 *
 */
public class BooleanRef {
	private boolean value;
	
	private ArrayList<BooleanChangeListener> changeListeners; //only constructed when a listener is actually added.
	
	/**
	 * Creates a new {@code BooleanRef} storing the given value.
	 */
	public BooleanRef(boolean value) {
		this.value = value;
	}
	
	/**
	 * @return the {@code boolean} value currently stored by this {@code BooleanRef}.
	 */
	public boolean get() {
		return value;
	}
	
	/**
	 * Does <b>not</b> trigger the {@link BooleanChangeListener}s if {@code newValue} is the same as the {@code boolean} currently stored by this
	 * {@code booleanRef}.
	 * @param newValue
	 */
	public void set(boolean newValue) {
		if(value == newValue) return;
		boolean oldValue = this.value;
		this.value = newValue;
		if(changeListeners != null)
			for(BooleanChangeListener listener : changeListeners)
				listener.changed(oldValue, newValue);
	}
	
	/**
	 * Adds the given {@link BooleanChangeListener} to this {@code BooleanRef}'s list of {@code BooleanChangeListener}s.
	 * @param listener
	 */
	public void addChangeListener(BooleanChangeListener listener) {
		if(changeListeners == null)
			changeListeners = new ArrayList<>();
		changeListeners.add(listener);
	}
	
	/**
	 * Removes the given {@link BooleanChangeListener} from this {@code BooleanRef}'s list of {@code BooleanChangeListener}s.
	 * This is an O(n) operation, where n is the number of listeners on this {@code BooleanRef}.
	 * @param listener
	 * @return {@code true} if the listener was present and has been removed, {@code false} if the listener was not present.
	 */
	public boolean removeChangeListener(BooleanChangeListener listener) {
		return changeListeners != null && changeListeners.remove(listener);
	}
}
