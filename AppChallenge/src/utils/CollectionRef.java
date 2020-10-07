package utils;

import java.util.*;
import java.util.function.Supplier;

/**
 * A reference to a {@link List}. Allows for {@link SingleListener}s to be run when an element is added or removed.
 * Note that the listeners are run <i>after</i> the element is added or removed, and the object passed to the listener
 * is the element that was added/removed.
 * @author Sam Hooper
 *
 */
public class CollectionRef<E> {
	private Collection<E> collection;
	
	private ArrayList<SingleListener<E>> addListeners; //only constructed when a listener is actually added.
	private ArrayList<SingleListener<E>> removeListeners; //only constructed when a listener is actually added.
	
	public CollectionRef(Collection<E> collection) {
		this.collection = collection;
	}
	
	public CollectionRef(Supplier<Collection<E>> collectionFactory) {
		this.collection = collectionFactory.get();
	}
	
	public boolean add(E item) {
		if(collection.add(item)) {
			runAddListeners(item);
			return true;
		}
		return false;
	}
	
	private void runAddListeners(E item) {
		if(addListeners == null)
			return;
		for(SingleListener<E> listener : addListeners)
			listener.listen(item);
	}
	
	public void addAddListener(SingleListener<E> addListener) {
		if(addListeners == null)
			addListeners = new ArrayList<>();
		addListeners.add(addListener);
	}
	
	/**
	 * Returns {@code true} if this {@code CollectionRef} had that listener and it has been removed, {@code false} otherwise.
	 */
	public boolean removeAddListener(SingleListener<E> addListener) {
		if(addListeners == null)
			return false;
		return addListeners.remove(addListener);
	}
	
	/**
	 * Removes the element from the list that this {@code ListRef} refers to.
	 * The listeners are only run if an item was removed.
	 * @param item
	 * @return {@code true} if an item was removed, {@code false} otherwise.
	 */
	public boolean remove(E item) {
		if(collection.remove(item)) {
			runRemoveListeners(item);
			return true;
		}
		return false;
	}
	
	private void runRemoveListeners(E item) {
		if(removeListeners == null)
			return;
		for(SingleListener<E> listener : removeListeners)
			listener.listen(item);
	}
	
	public void addRemoveListener(SingleListener<E> removeListener) {
		if(removeListeners == null)
			removeListeners = new ArrayList<>();
		removeListeners.add(removeListener);
	}
	
	/**
	 * Returns {@code true} if this {@code CollectionRef} had that listener and it has been removed, {@code false} otherwise.
	 */
	public boolean removeRemoveListener(SingleListener<E> removeListener) {
		if(removeListeners == null)
			return false;
		return removeListeners.remove(removeListener);
	}
	
	public boolean contains(E item) {
		return collection.contains(item);
	}
	
	public Collection<E> getUnmodifiable() {
		return Collections.unmodifiableCollection(collection);
	}
}
