package graphics;

import java.util.*;
import java.util.function.Function;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import logic.Ability;
import logic.abilities.StepMove;
/**
 * @author Sam Hooper
 *
 */
public abstract class AbilityPane extends StackPane	{
	private static Map<Class<? extends Ability>, Function<Ability, AbilityPane>> paneFactories;
	
	static {
		paneFactories = new HashMap<>();
		
		paneFactories.put(StepMove.class, a -> new AbilityPane(a) {
			{
				StepMove stepMove = (StepMove) a;
				VBox vBox = new VBox();
				getChildren().add(vBox);
				Label label = new Label("Step Move: " + stepMove.distanceProperty().get());
				vBox.getChildren().add(label);
				stepMove.distanceProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Step Move: " + newValue);
				});
			}

			@Override
			public void select() {
				System.out.println("StepMove selected");
				TerrainGrid grid = Main.currentLevel().getTerrainPane().getGrid();
				for(int[] legalSpot : ability.getLegals()) {
					grid.getTileAt(legalSpot[0], legalSpot[1]).highlight(highlightColor());
				}
			}

			@Override
			public void deselect() {
				System.out.println("StepMove deselected");
			}
		});
	}
	
	public static final <T extends Ability> AbilityPane of(T ability) {
		Objects.requireNonNull(ability);
		Function<Ability, AbilityPane> factory = paneFactories.get(ability.getClass());
		if(factory == null)
			throw new UnsupportedOperationException("Unsupported ability type: " + ability.getClass());
		return factory.apply(ability);
	}
	
	protected final Ability ability;
	
	private boolean selected;
	
	protected AbilityPane(Ability ability) {
		this.ability = ability;
		selected = false;
		this.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton() != MouseButton.PRIMARY) //only left clicks
				return;
			if(isSelected()) {
				deselect();
				selected = false;
			}
			else {
				select();
				selected = true;
			}
			mouseEvent.consume();
		});
	}
	
	public Ability getAbility() {
		return ability;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Invoked when the user selects this {@code AbilityPane}. It should display which tiles the player can click on to
	 * make a legal move with the {@link Ability} represented by this {@code AbilityPane}. {@link #isSelected()} is {@code false}
	 * immediately before this method is invoked and is set to {@code true} immediately after this method is invoked.
	 */
	public abstract void select();
	
	/**
	 * Invoked when the user deselects this {@code AbilityPane}. It should hide everything that was displayed by the previous
	 * {@link #select()} call. {@link #isSelected()} is {@code true} immediately before this method is invoked and is set to
	 * {@code false} immediately after this method is invoked.
	 */
	public abstract void deselect();
	
	public Paint highlightColor() {
		return Color.LAWNGREEN;
	}
}
