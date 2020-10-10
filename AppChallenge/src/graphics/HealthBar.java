package graphics;

import fxutils.ImageWrap;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.Node;
import logic.HasHealth;
import utils.IntChangeListener;

/**
 * A Health bar that can be used to display a {@link HasHealth}'s health.
 * @author Sam Hooper
 *
 */
public class HealthBar extends HBox {
	
	/**
	 * The recommended percentage of a tile that a {@link HealthBar} should occupy.
	 */
	public static final double HEALTH_BAR_PERCENT = .1;
	
	private static final ImageInfo HEALTH_POINT_INFO = new ImageInfo("HealthPoint.png");
	private static final ImageInfo MISSING_HEALTH_POINT_INFO = new ImageInfo("MissingHealthPoint.png");
	private static final double HEALTH_BAR_SPACING = 2;
	
	private final IntChangeListener healthListener = (oldValue, newValue) -> {
		clearAndfillHealthBar();
	};
	
	private HasHealth gameObject;
	private boolean displayOnMax;
	
	public HealthBar(boolean displayWhenAtMaxHealth) {
		super(HEALTH_BAR_SPACING);
		this.gameObject = null;
		this.displayOnMax = displayWhenAtMaxHealth;
		this.setAlignment(Pos.CENTER);
	}
	
	private void clearAndfillHealthBar() {
		getChildren().clear();
		int pointsAdded = 0;
		int currentHealth = gameObject.getHealth();
		int maxHealth = gameObject.getMaxHealth();
		if(!displayOnMax && currentHealth == maxHealth)
			return;
		while(pointsAdded < maxHealth) {
			ImageWrap wrap;
			if(pointsAdded < currentHealth)
				wrap = new ImageWrap(HEALTH_POINT_INFO.getImage(), 0, 0);
			else
				wrap = new ImageWrap(MISSING_HEALTH_POINT_INFO.getImage(), 0, 0);
			StackPane stack = new StackPane();
			stack.prefWidthProperty().bind(stack.heightProperty());
			stack.getChildren().add(wrap);
			getChildren().add(stack);
			pointsAdded++;
		}
	}
	
	/**
	 * Sets the current {@link HasHealth} to the given one. Updates the displayed health points
	 * as appropriate, and adds a listener to the {@code HasHealth} so that future changes to
	 * its health are automatically reflected on this {@link HealthBar}.
	 */
	public void setGameObject(HasHealth gameObject) {
		this.gameObject = gameObject;
		clearAndfillHealthBar();
		gameObject.healthProperty().addChangeListener(healthListener);
	}
	
	/**
	 * Removes all children from this {@link Node} and unlinks the {@link IntChangeListener}s
	 * that are listening to the current {@link HasHealth}. Sets the current game object to {@code null}.
	 */
	public void clearGameObject() {
		getChildren().clear();
		gameObject.healthProperty().removeChangeListener(healthListener);
		gameObject = null;
	}
	
	/**
	 * {@link #clearGameObject() Clears the game object} and sets this {@link Node}'s
	 * {@link Node#visibleProperty() visible property} to {@code false}.
	 */
	public void clearAndHide() {
		clearGameObject();
		this.setVisible(false);
	}
	
	public HasHealth getGameObject() {
		return gameObject;
	}
}
