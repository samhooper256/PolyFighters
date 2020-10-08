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
	private final AbilityPanel abilityPanel;
	
	private boolean abilityPanelShowing;
	
	public InfoPanel() {
		super();
		abilityPanelShowing = false;
		defaultContent = new VBox();
		initDefaultContent();
		abilityPanel = new AbilityPanel();
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
		if(abilityPanelShowing) {
			abilityPanelShowing = false;
			AbilityPane pane = abilityPanel.getSelectedAbilityPane();
			if(pane != null) {
				pane.deselect();
			}
		}
		getChildren().clear();
	}
	
	/**
	 * Sets the content of this {@code InfoPanel} to its default state.
	 */
	public void setToDefaultState() {
		clearContent();
		getChildren().add(defaultContent);
	}
	
	/**
	 * Returns the {@link AbilityPanel} associated with this {@code InfoPanel}. The {@code AbilityPanel}
	 * may or may not be currently showing.
	 */
	public AbilityPanel getAbilityPanel() {
		return abilityPanel;
	}
	
	/**
	 * Adds the {@link AbilityPanel} associated with this {@code InfoPanel} to the top of its stack if it is not already on the stack.
	 * Otherwise, does nothing.
	 */
	public void displayAbilityPanel() {
		if(!abilityPanelShowing) {
			getChildren().add(abilityPanel);
			abilityPanelShowing = true;
		}
	}
	
	public boolean isAbilityPanelShowing() {
		return abilityPanelShowing;
	}
}
