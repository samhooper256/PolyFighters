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
			public void selectAction() {
				System.out.println("StepMove selected");
				Level.current().getInfoPanel().getAbilityPanel().setSelectedAbilityPane(this);
				TerrainGrid grid = Level.current().getTerrainPane().getGrid();
				legalsCache = ability.getLegals();
				for(int[] legalSpot : legalsCache) {
					final TerrainTile tile = grid.getTileAt(legalSpot[0], legalSpot[1]);
					tile.highlightOrThrow(highlightColor());
					tile.setUseCandidate(true);
				}
			}

			@Override
			public void deselectAction() {
				Level.current().getInfoPanel().getAbilityPanel().clearSelectedAbilityPane();
				TerrainGrid grid = Level.current().getTerrainPane().getGrid();
				for(int[] legalSpot : legalsCache) {
					final TerrainTile tile = grid.getTileAt(legalSpot[0], legalSpot[1]);
					tile.clearAllHighlights();
					tile.setUseCandidate(false);
				}
				System.out.println("StepMove deselected"); //TODO finish this method and make sure it gets called.
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
	/** can optionally be used by subclasses store the legal moves computed in the select method for use in the deselect method. */
	protected Collection<int[]> legalsCache = null;
	private boolean selected;
	
	protected AbilityPane(Ability ability) {
		this.ability = ability;
		selected = false;
		this.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton() != MouseButton.PRIMARY) //only left clicks
				return;
			if(isSelected()) {
				deselect();
			}
			else {
				select();
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
	
	public final void select() {
		if(!isSelected()) {
			selectAction();
			selected = true;
		}
	}
	
	public final void deselect() {
		if(isSelected()) {
			deselectAction();
			selected = false;
		}
	}
	/**
	 * Invoked when the user selects this {@code AbilityPane}. It should display which tiles the player can click on to
	 * make a legal move with the {@link Ability} represented by this {@code AbilityPane}. {@link #isSelected()} is {@code false}
	 * immediately before this method is invoked and is set to {@code true} immediately after this method is invoked.
	 */
	public abstract void selectAction();
	
	/**
	 * Invoked when the user deselects this {@code AbilityPane}. It should hide everything that was displayed by the previous
	 * {@link #selectAction()} call. It must also call {@link AbilityPanel#clearSelectedAbilityPane()}. {@link #isSelected()} is
	 * {@code true} immediately before this method is invoked and is set to {@code false} immediately after this method is invoked.
	 */
	public abstract void deselectAction();
	
	public Paint highlightColor() {
		return TerrainTile.DEFAULT_HIGHLIGHT;
	}
}
