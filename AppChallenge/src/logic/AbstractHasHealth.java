package logic;

import java.util.*;

import logic.HasHealth.EtherealDeathListener;
import utils.BooleanRef;
import utils.IntRef;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractHasHealth implements HasHealth {

	private static int checkHealth(int health, int maxHealth) {
		if(health < 0)
			throw new IllegalArgumentException("Health must be >= 0");
		if(health > maxHealth)
			throw new IllegalArgumentException("Health must not be greater than the maximum");
		return health;
	}
	
	public class AliveProperty extends BooleanRef {
		private AliveProperty(boolean value) {
			super(value);
		}
		
		@Override
		public boolean set(boolean newValue) {
			boolean changed = super.set(newValue);
			if(changed) {
				if(newValue == false) {
					health.set(0);
					fireEtherealDeathListeners();
				}
			}
			return changed;
		}
	}
	
	public class HealthProperty extends IntRef {
		/**
		 * The given value must not be less than zero or greater than this {@link HasHealth HasHealth's} maximum health.
		 */
		private HealthProperty(int value) {
			super(checkHealth(value, getMaxHealth()));
			
		}

		@Override
		public boolean set(int newValue) {
			checkHealth(newValue, getMaxHealth());
			boolean changed = super.set(newValue);
			if(changed) {
				if(newValue == 0) {
					aliveProperty.set(false);
				}
				else { //newValue is positive
					aliveProperty.set(true);
				}
			}
			return changed;
		}
	}
	
	protected final Queue<EtherealDeathListener> etherealDeathListeners;
	protected final AliveProperty aliveProperty;
	protected final IntRef maxHealth;
	protected final HealthProperty health;
	
	/**
	 * Creates a new {@link AbstractHasHealth} the given max health. The {@link AbstractHasHealth} will start at full (max) health.
	 */
	public AbstractHasHealth(int maxHealth) {
		this(maxHealth, maxHealth);
	}
	
	/**
	 * Creates a new {@link AbstractHasHealth} the given max health and starting health.
	 */
	public AbstractHasHealth(int maxHealth, int currentHealth) {
		if(currentHealth < 0 || currentHealth > maxHealth)
			throw new IllegalArgumentException("Current health value (" + currentHealth + ") is invalid for max health " + maxHealth);
		this.maxHealth = new IntRef(maxHealth);
		this.health = new HealthProperty(currentHealth);
		this.aliveProperty = new AliveProperty(currentHealth > 0);
		this.etherealDeathListeners = new LinkedList<>();
	}
	
	@Override
	public BooleanRef aliveProperty() {
		return aliveProperty;
	}
	
	
	@Override
	public IntRef maxHealthProperty() {
		return maxHealth;
	}

	@Override
	public IntRef healthProperty() {
		return health;
	}

	@Override
	public void addEtherealDeathListener(EtherealDeathListener listener) {
		etherealDeathListeners.add(listener);
	}

	/**
	 * This is an O(n) operation, where n is the number of {@link EtherealDeathListener EtherealDeathListeners} registered with this {@link HasHealth}.
	 */
	@Override
	public boolean removeEtherealDeathListener(EtherealDeathListener listener) {
		return etherealDeathListeners.remove(listener);
	}
	
	protected void fireEtherealDeathListeners() {
		while(!etherealDeathListeners.isEmpty()) {
			EtherealDeathListener listener = etherealDeathListeners.peek();
			listener.died();
			etherealDeathListeners.remove(); //do peek and remove in two different steps because we are supposed to remove after we fire the listener.
		}
	}
}
