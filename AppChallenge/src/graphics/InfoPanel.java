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
	private final ObstacleInfoPanel obstacleInfoPanel;
	private boolean abilityInfoPanelShowing;
	private boolean tileInfoPanelShowing;
	private boolean obstacleInfoPanelShowing;
	
	public InfoPanel() {
		super();
		abilityInfoPanelShowing = false;
		tileInfoPanelShowing = false;
		obstacleInfoPanelShowing = false;
		defaultContent = new VBox();
		initDefaultContent();
		abilityInfoPanel = new AbilityInfoPanel();
		tileInfoPanel = new TileInfoPanel();
		obstacleInfoPanel = new ObstacleInfoPanel();
		getChildren().add(defaultContent);
	}
	
	/**
	 * Must only be called from constructor.
	 */
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
		takeDownObstacleInfoPanel();
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
	
	/**
	 * Handles any necessary actions that need to be done before the {@link #obstacleInfoPanel} is removed from this {@code InfoPanel}'s
	 * list of children.
	 */
	private void takeDownObstacleInfoPanel() {
		if(obstacleInfoPanelShowing) {
			obstacleInfoPanelShowing = false;
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
	
	public void hideObstacleInfoPanel() {
		if(obstacleInfoPanelShowing) {
			takeDownObstacleInfoPanel();
			getChildren().remove(obstacleInfoPanel);
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
	 * Returns the {@link TileInfoPanel} associated with this {@code InfoPanel}. The {@code TileInfoPanel}
	 * may or may not be currently showing.
	 */
	public TileInfoPanel getTileInfoPanel() {
		return tileInfoPanel;
	}

	/**
	 * Returns the {@link ObstacleInfoPanel} associated with this {@code InfoPanel}. The {@code ObstacleInfoPanel}
	 * may or may not be currently showing.
	 */
	public ObstacleInfoPanel getObstacleInfoPanel() {
		return obstacleInfoPanel;
	}
	
	/**
	 * Removes all other panels from this {@link InfoPanel} and displays only the {@link AbilityInfoPanel}. If the {@code AbilityInfoPanel} is already
	 * showing, does nothing.
	 */
	public void displayOnlyAbilityInfoPanel() {
		hideTileInfoPanel();
		hideObstacleInfoPanel();
		if(!abilityInfoPanelShowing) {
			getChildren().add(abilityInfoPanel);
			abilityInfoPanelShowing = true;
		}
	}
	
	/**
	 * Removes all other panels from this {@link InfoPanel} and displays only the {@link TileInfoPanel}. If the {@code TileInfoPanel} is already
	 * showing, does nothing.
	 */
	public void displayOnlyTileInfoPanel() {
		hideAbilityInfoPanel();
		hideObstacleInfoPanel();
		if(!tileInfoPanelShowing) {
			getChildren().add(tileInfoPanel);
			tileInfoPanelShowing = true;
		}
	}
	
	/**
	 * Removes all other panels from this {@link InfoPanel} and displays only the {@link ObstacleInfoPanel}. If the {@code ObstacleInfoPanel} is already
	 * showing, does nothing.
	 */
	public void displayOnlyObstacleInfoPanel() {
		hideAbilityInfoPanel();
		hideTileInfoPanel();
		if(!obstacleInfoPanelShowing) {
			getChildren().add(obstacleInfoPanel);
			obstacleInfoPanelShowing = true;
		}
	}

	public boolean isAbilityInfoPanelShowing() {
		return abilityInfoPanelShowing;
	}
	
	public boolean isTileInfoPanelShowing() {
		return tileInfoPanelShowing;
	}
	
	public boolean isObstacleInfoPanelShowing() {
		return obstacleInfoPanelShowing;
	}
}
