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
	public InfoPanel() {
		super();
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
	
	/**Sets the content of this {@code InfoPanel} to its default state.*/
	public void clearContent() {
		getChildren().clear();
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
	 * Adds the {@link AbilityPanel} associated with this {@code InfoPanel} to the top of its stack.
	 */
	public void displayAbilityPanel() {
		getChildren().add(abilityPanel);
	}
}
