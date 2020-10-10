package logic.units;

import java.util.*;

import logic.Ability;
import logic.Board;
import logic.TeamUnit;
import logic.TileType;
import logic.Unit;
import utils.BooleanRef;
import utils.CollectionRef;
import utils.IntRef;

/**
 * Abstract base class that classes implementing {@link TeamUnit} may extend to make implementation easier.
 * 
 * @author Sam Hooper
 *
 */
public abstract class AbstractUnit implements Unit {
	
	protected final CollectionRef<Ability> abilities;
	
	/**
	 * An {@link EnumSet} containing all of the {@link TileType}s that this unit can traverse.
	 * Note that move-based abilities may  also have the ability to restrict the types of tiles that can be traversed. Both the
	 * unit <b>and</b> the ability must allow a tile to be traversed for the action to be legal. Will not be {@code null}.
	 */
	protected final EnumSet<TileType> traversableTileTypes;
	
	protected Board board;
	protected int maxHealth;
	protected IntRef health;
	/** {@code -1} if not on a board. */
	protected int row;
	/** {@code -1} if not on a board. */
	protected int col;
	protected BooleanRef aliveProperty;
	
	/**
	 * Constructs a new {@code AbstractUnit} with the given maximum health. The current health of the {@link Unit} will be set to
	 * the maximum health. The constructed unit will not be associated with a {@link Board}, nor will it have any abilities.
	 * @param maxHealth the maximum health of the {@code AbstractUnit}.
	 */
	protected AbstractUnit(int maxHealth) {
		this(maxHealth, maxHealth);
	}
	
	/**
	 * Constructs a new {@code AbstractUnit} with the given maximum health and current health.
	 * The constructed unit will not be associated with a {@link Board}, nor will it have any abilities.
	 * @param maxHealth the maximum health of the {@code AbstractUnit}.
	 * @throws IllegalArgumentException if {@code currentHealth} is negative or greater than {@code maxHealth}.
	 */
	protected AbstractUnit(int maxHealth, int currentHealth) {
		this(null, -1, -1, maxHealth, currentHealth, new ArrayList<>());
	}
	
	/**
	 * 
	 * @param board the {@code Board} that this {@code Unit} is associated with, or {@code null} if this unit is not yet assoicated with one.
	 * @param abilities the list of this {@code Unit}'s abilities. Must not be {@code null}.
	 * @throws IllegalArgumentException if {@code currentHealth} is negative or greater than {@code maxHealth}.
	 */
	protected AbstractUnit(Board board, int row, int col, int maxHealth, int currentHealth, List<Ability> abilities) {
		if(currentHealth < 0 || currentHealth > maxHealth)
			throw new IllegalArgumentException("Current health value (" + currentHealth + ") is invalid for max health " + maxHealth);
		this.abilities = new CollectionRef<>(Objects.requireNonNull(abilities));
		this.maxHealth = maxHealth;
		this.health = new IntRef(currentHealth);
		this.board = board;
		this.traversableTileTypes = EnumSet.noneOf(TileType.class);
		this.row = row;
		this.col = col;
		this.aliveProperty = new BooleanRef(currentHealth == 0 ? false : true);
	}
	
	@Override
	public boolean canTraverse(TileType type) {
		return traversableTileTypes.contains(type);
	}
	
	@Override
	public Collection<int[]> getLegalSpots(Ability ability) {
		if(!abilities.contains(ability))
			throw new IllegalArgumentException("This unit does not have the given ability");
		return ability.getLegals();
	}
	
	/**
	 * If this {@code Unit} does not have the indicated {@link Ability}, adds that {@code Ability} to this {@code Unit}'s
	 * set of {@code Abilities} and returns {@code true}. Otherwise, returns {@code false}.
	 * @param ability
	 * @return {@code true} if the ability was added, {@code false} otherwise.
	 */
	public boolean addAbility(Ability ability) {
		if(hasAbility(ability))
			return false;
		abilities.add(ability);
		return true;
	}
	
	/**
	 * Returns {@code true} if {@code ability} is a member of this {@code Unit}'s set of abilities, {@code false} otherwise.
	 */
	public boolean hasAbility(Ability ability) {
		return abilities.contains(ability);
	}
	
	@Override
	public CollectionRef<Ability> abilityCollectionRef() {
		return abilities;
	}
	
	@Override
	public Collection<Ability> getAbilitiesUnmodifiable() {
		return abilities.getUnmodifiable();
	}
	
	@Override
	public Board getBoard() {
		return board;
	}
	
	/**
	 * {@inheritDoc}. If {@code board} is {@code null}, the row and column values of this {@code Unit} are set to {@code -1}.
	 */
	@Override
	public void setBoard(Board board) {
		if(board == null) {
			row = -1;
			col = -1;
		}
		this.board = board;
	}
	
	/**
	 * Returns the 0-based row index of this {@code Unit} on its {@link Board}, or {@code -1} if this {@code Unit} is not on a {@code Board}.
	 */
	@Override
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns the 0-based column index of this {@code Unit} on its {@link Board}, or {@code -1} if this {@code Unit} is not on a {@code Board}.
	 */
	@Override
	public int getCol() {
		return col;
	}
	
	@Override
	public void setRow(int row) {
		this.row = row;
	}
	
	@Override
	public void setCol(int col) {
		this.col = col;
	}
	
	@Override
	public int getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public IntRef healthProperty() {
		return health;
	}
	
	@Override
	public BooleanRef aliveProperty() {
		return aliveProperty;
	}
}
