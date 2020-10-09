package graphics;

import javafx.scene.layout.*;
import logic.Ability;
import logic.Unit;

/**
 * A {@link Pane} used for displaying {@link AbilityPane}s. Objects of this class keep track of the currently selected {@link Ability}.
 * @author Sam Hooper
 *
 */
public class AbilityInfoPanel extends VBox {
	
	private AbilityPane selectedAbilityPane;
	private Unit unit;
	
	public AbilityInfoPanel() {
		super();
		selectedAbilityPane = null;
		unit = null;
	}
	
	/**
	 * Returns the {@link Unit} whose abilities are being displayed by this {@code AbilityPanel}, or {@code null} if no {@code Unit}'s abilities
	 * are being displayed.
	 */
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit unitPane) {
		this.unit = unitPane;
	}
	
	/**
	 * Updates this {@code AbilityInfoPanel}'s {@link AbilityPane} pointer to the given {@code AbilityPane}, and deselects
	 * the currently selected {@code AbilityPane} if there is one. <b>Must ONLY be called from {@link AbilityPane#selectAction}.</b>
	 * @param abilityPane
	 */
	public void setSelectedAbilityPane(AbilityPane abilityPane) {
		if(selectedAbilityPane != null)
			selectedAbilityPane.deselect();
		selectedAbilityPane = abilityPane;
	}
	
	/**
	 * Returns the currently selected {@link AbilityPane}, or {@code null} if no {@code AbilityPane} is currently selected.
	 */
	public AbilityPane getSelectedAbilityPane() {
		return selectedAbilityPane;
	}
	
	/**
	 * Sets this {@code AbilityInfoPanel}'s {@link AbilityPane} pointer to {@code null}. <b>Must ONLY be called from {@link AbilityPane#deselectAction}.</b>
	 */
	public AbilityPane clearSelectedAbilityPane() {
		return selectedAbilityPane;
	}
	
	/**
	 * Returns the currently selected {@link Ability}, or throws an {@link IllegalStateException} if no {@code Ability}
	 * is selected.
	 * @throws IllegalStateException if no {@link Ability} is currently selected.
	 */
	public AbilityPane getSelectedAbiltiyOrThrow() {
		if(selectedAbilityPane == null)
			throw new IllegalArgumentException("No AbilityPane is currently selected.");
		return selectedAbilityPane;
	}
	
	public void addPane(AbilityPane pane) {
		getChildren().add(pane);
	}
	
	public void clearPanes() {
		getChildren().clear();
	}
	
}
