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
	public InfoPanel() {
		super();
		defaultContent = new VBox();
		initDefaultContent();
		getChildren().add(defaultContent);
	}
	
	private void initDefaultContent() {
		defaultContent.setAlignment(Pos.CENTER);
		final Label label = new Label("Nothing selected.");
		label.setWrapText(true);
		defaultContent.getChildren().add(label);
	}
	
}
