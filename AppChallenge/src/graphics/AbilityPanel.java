package graphics;

import javafx.scene.layout.*;

/**
 * A {@link Pane} used for displaying {@link AbilityPane}s.
 * @author Sam Hooper
 *
 */
public class AbilityPanel extends VBox {
	public AbilityPanel() {
		super();
	}
	
	public void addPane(AbilityPane pane) {
		getChildren().add(pane);
	}
	
	public void clearPanes() {
		getChildren().clear();
	}
}
