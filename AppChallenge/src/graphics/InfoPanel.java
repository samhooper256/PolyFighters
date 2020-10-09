package graphics;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * The side panel on the right of the screen that will display information about what the user has clicked.
 * @author Sam Hooper
 *
 */
public class InfoPanel extends StackPane {
	private final VBox defaultContent;
	private final AbilityInfoPanel abilityInfoPanel;
	private final TileInfoPanel tileInfoPanel;
	private boolean abilityInfoPanelShowing;
	private boolean tileInfoPanelShowing;
	
	public InfoPanel() {
		super();
		abilityInfoPanelShowing = false;
		tileInfoPanelShowing = false;
		defaultContent = new VBox();
		initDefaultContent();
		abilityInfoPanel = new AbilityInfoPanel();
		tileInfoPanel = new TileInfoPanel();
		getChildren().add(defaultContent);
	}
	
	private void initDefaultContent() {
		defaultContent.setAlignment(Pos.CENTER);
		final Label label = new Label("Nothing selected.");
		label.setWrapText(true);
		defaultContent.getChildren().add(label);
	}
	
	/**Clears the content of this {@code InfoPanel}.*/
	public void clearContent() {
		takeDownAbilityInfoPanel();
		takeDownTileInfoPanel();
		getChildren().clear();
	}

	/**
	 * Handles any necessary actions that need to be done before the {@link #tileInfoPanel} is removed from this {@code InfoPanel}'s
	 * list of children.
	 */
	private void takeDownTileInfoPanel() {
		if(tileInfoPanelShowing) {
			tileInfoPanelShowing = false;
		}
	}

	/**
	 * Handles any necessary actions that need to be done before the {@link #abilityInfoPanel} is removed from this {@code InfoPanel}'s
	 * list of children.
	 */
	private void takeDownAbilityInfoPanel() {
		if(abilityInfoPanelShowing) {
			abilityInfoPanelShowing = false;
			AbilityPane pane = abilityInfoPanel.getSelectedAbilityPane();
			if(pane != null) {
				pane.deselect();
			}
		}
	}
	
	public void hideTileInfoPanel() {
		if(tileInfoPanelShowing) {
			takeDownTileInfoPanel();
			getChildren().remove(tileInfoPanel);
		}
	}
	
	public void hideAbilityInfoPanel() {
		if(abilityInfoPanelShowing) {
			takeDownAbilityInfoPanel();
			getChildren().remove(abilityInfoPanel);
		}
	}
	
	/**
	 * Sets the content of this {@code InfoPanel} to its default state.
	 */
	public void setToDefaultState() {
		clearContent();
		getChildren().add(defaultContent);
	}
	
	/**
	 * Returns the {@link AbilityInfoPanel} associated with this {@code InfoPanel}. The {@code AbilityInfoPanel}
	 * may or may not be currently showing.
	 */
	public AbilityInfoPanel getAbilityInfoPanel() {
		return abilityInfoPanel;
	}
	
	/**
	 * Adds the {@link AbilityInfoPanel} associated with this {@code InfoPanel} to the top of its stack if it is not already on the stack.
	 * Otherwise, does nothing.
	 */
	public void displayOnlyAbilityInfoPanel() {
		hideTileInfoPanel();
		if(!abilityInfoPanelShowing) {
			getChildren().add(abilityInfoPanel);
			abilityInfoPanelShowing = true;
		}
	}
	
	public boolean isAbilityInfoPanelShowing() {
		return abilityInfoPanelShowing;
	}
	
	/**
	 * Returns the {@link TileInfoPanel} associated with this {@code InfoPanel}. The {@code TileInfoPanel}
	 * may or may not be currently showing.
	 */
	public TileInfoPanel getTileInfoPanel() {
		return tileInfoPanel;
	}
	
	/**
	 * Adds the {@link AbilityInfoPanel} associated with this {@code InfoPanel} to the top of its stack if it is not already on the stack.
	 * Otherwise, does nothing.
	 */
	public void displayOnlyTileInfoPanel() {
		hideAbilityInfoPanel();
		if(!tileInfoPanelShowing) {
			getChildren().add(tileInfoPanel);
			tileInfoPanelShowing = true;
		}
	}
	
	public boolean isTileInfoPanelShowing() {
		return tileInfoPanelShowing;
	}
}
