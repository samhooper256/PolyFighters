package graphics;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import logic.*;

/**
 * A {@link Pane} used for displaying {@link AbilityPane}s. Objects of this class keep track of the currently selected {@link Ability}.
 * @author Sam Hooper
 *
 */
public class AbilityInfoPanel extends BorderPane {
	private final VBox top;
	private final Label unitName, movesRemainingLabel;
	private final VBox abilityPaneBox;
	private Pane bottomInfo;
	private AbilityPane selectedAbilityPane;
	private Unit unit;
	private boolean panesEnabled;
	public AbilityInfoPanel() {
		super();
		this.abilityPaneBox = new VBox(10);
		abilityPaneBox.setPadding(new Insets(20, 0, 20, 0));
		this.setPadding(new Insets(10, 0, 10, 0));
		this.unitName = new Label();
		this.movesRemainingLabel = new Label();
		unitName.setFont(Font.font(24));
		top = new VBox(unitName, movesRemainingLabel);
		top.setAlignment(Pos.CENTER);
		selectedAbilityPane = null;
		unit = null;
		this.panesEnabled = true;
		setTop(top);
		setCenter(abilityPaneBox);
	}
	
	/**
	 * Returns the {@link Unit} whose abilities are being displayed by this {@code AbilityPanel}, or {@code null} if no {@code Unit}'s abilities
	 * are being displayed.
	 */
	public Unit getUnit() {
		return unit;
	}
	
	/**
	 * Sets this {@link AbilityInfoPanel}'s {@link Unit} pointer to the given {@link Unit} and updates the displayed text for the {@link Unit Unit's} name accordingly.
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
		unitName.setText(Names.of(this.unit));
		if(unit instanceof PlayerUnit) {
			movesRemainingLabel.setText(movesRemainingText(((PlayerUnit) unit).getMovesRemaining()));
		}
		else {
			movesRemainingLabel.setText("");
		}
	}
	
	public void updateMovesRemaining() {
		if(unit instanceof PlayerUnit) {
			movesRemainingLabel.setText(movesRemainingText(((PlayerUnit) unit).getMovesRemaining()));
		}
	}
	
	private String movesRemainingText(int moves) {
		return moves + " move" + (moves == 1 ? "" : "s") + " remaining";
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
	public void clearSelectedAbilityPane() {
		selectedAbilityPane = null;
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
	
	public void deselectAndClearContent() {
		if(selectedAbilityPane != null) {
			selectedAbilityPane.deselect();
			selectedAbilityPane = null;
		}
		abilityPaneBox.getChildren().clear();
	}
	
	public void addPane(AbilityPane pane) {
		abilityPaneBox.getChildren().add(pane);
	}
	
	public void clearPanes() {
		abilityPaneBox.getChildren().clear();
	}
	
	public void setInfo(Pane info) {
		Objects.requireNonNull(info);
		bottomInfo = info;
		setBottom(bottomInfo);
	}
	
	public void disableSelectionForAllAbilityPanes() {
		if(!panesEnabled)
			return;
		for(Node n : abilityPaneBox.getChildren()) {
			((AbilityPane) n).disableSelection();
		}
		panesEnabled = false;
	}
}
