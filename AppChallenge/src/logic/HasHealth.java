package logic;

import utils.BooleanRef;
import utils.IntRef;

/**
 * <p>Interface for any {@link GameObject} that has health.</p>
 * 
 * <p>{@link EtherealDeathListener EtherealDeathListeners} can be registered with a {@link HasHealth}. When one such listener is added, it is fired the next time this
 * {@link HasHealth} dies (that is, its health changes from a nonzero value to zero). After an {@link EtherealDeathListener} is fired, it is unregistered from this
 * {@link HasHealth} and will not be fired again unless it is explicitly {@link #addEtherealDeathListener() added} again. {@link EtherealDeathListener EtherealDeathListeners}
 * are run after all other listeners on this {@link HasHealh HasHealth's} properties.</p>
 * 
 * <p>Subtypes are <i>strongly</i> advised to extend {@link AbstractHasHealth} instead of implementing this interface directly.</p>
 * 
 * @author Sam Hooper
 *
 */
public interface HasHealth extends GameObject {
	
	interface EtherealDeathListener {
		void died();
	}
	
	/**
	 * Returns this {@link HasHealth HasHealth's} max health property. The value contained to by the property will not be negative.
	 */
	IntRef maxHealthProperty();
	
	default int getMaxHealth() {
		return maxHealthProperty().get();
	}
	
	/**
	 * Returns this {@link HasHealth HasHealth's} current health property. The value contained by the property will be between 0 and {@link #getMaxHealth()}
	 * (inclusive on both ends). If the returned value is {@code 0}, this {@link HasHealth} is dead.
	 */
	IntRef healthProperty();
	
	
	default int getHealth() {
		return healthProperty().get();
	}
	
	/**
	 * Returns {@code true} if this {@link GameObject} is dead, {@code false} otherwise.
	 */
	default boolean isAlive() {
		return aliveProperty().get();
	}
	
	default boolean isFullHealth() {
		return getHealth() == getMaxHealth();
	}
	
	/**
	 * The {@link BooleanRef} stores {@code true} if this {@link GameObject} is alive, {@code false} otherwise.
	 */
	BooleanRef aliveProperty();
	
	/**
	 * Adds the given {@link EtherealDeathListener} to this {@link HasHealth}. The listener is automatically removed after it is fired. When this
	 * {@link HasHealth} dies, its {@link EtherealDeathListener EtherealDeathListeners} are fired in the order they are added. They are immediately removed
	 * after being fired. (That is, a listener is removed before the next one is fired). The same listener may be added more than once.
	 */
	void addEtherealDeathListener(final EtherealDeathListener listener);
	
	/**
	 * Returns {@code true} if the given {@link EtherealDeathListener} was present and has been removed, {@code false} otherwise. Note that this
	 * method returns {@code false} for a listener that was {@link #addEtherealDeathListener(EtherealDeathListener) added}
	 * and has been fired (and has not been re-added since the last time it was fired).
	 */
	boolean removeEtherealDeathListener(final EtherealDeathListener listener);
}
