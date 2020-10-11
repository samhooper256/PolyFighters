package graphics;

import java.util.*;
import java.util.function.Function;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import logic.Ability;
import logic.abilities.*;
import utils.IntChangeListener;
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
		});
		paneFactories.put(DiamondTeleport.class, a -> new AbilityPane(a) {
			{
				DiamondTeleport diamondTp = (DiamondTeleport) a;
				VBox vBox = new VBox();
				getChildren().add(vBox);
				Label label = new Label("Diamond Teleport: " + diamondTp.distanceProperty().get());
				vBox.getChildren().add(label);
				diamondTp.distanceProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Diamond Teleport: " + newValue);
				});
			}
		});
		paneFactories.put(Shoot.class, a -> new AbilityPane(a) {
			{
				Shoot shoot = (Shoot) a;
				VBox vBox = new VBox();
				getChildren().add(vBox);
				Label label = new Label("Shoot with Damage: " + shoot.damageProperty().get());
				vBox.getChildren().add(label);
				shoot.damageProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Shoot with Damage: " + newValue);
				});
			}
		});
		paneFactories.put(Melee.class, a -> new AbilityPane(a) {
			{
				Melee melee = (Melee) a;
				VBox vBox = new VBox();
				getChildren().add(vBox);
				Label label = new Label("Melee: " + melee.damageProperty().get());
				vBox.getChildren().add(label);
				melee.damageProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Melee: " + newValue);
				});
			}
		});
		paneFactories.put(Smash.class, a -> new AbilityPane(a) {
			{
				Smash smash = (Smash) a;
				VBox vBox = new VBox();
				getChildren().add(vBox);
				Label label = new Label("Smash: " + smash.damageProperty().get() + " damage, " + smash.radiusProperty().get() + " radius");
				label.setWrapText(true);
				vBox.getChildren().add(label);
				smash.damageProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Smash: " + newValue + " damage, " + smash.radiusProperty().get() + " radius");
				});
				smash.radiusProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Smash: " + smash.damageProperty().get() + " damage, " + newValue + " radius");
				});
			}
		});
		paneFactories.put(Lob.class, a -> new AbilityPane(a) {
			{
				Lob lob = (Lob) a;
				VBox vBox = new VBox();
				getChildren().add(vBox);
				Label label = new Label("Lob: " + lob.damageProperty().get() + " damage, " + lob.minimumDistanceProperty().get() + " minimum distance");
				label.setWrapText(true);
				vBox.getChildren().add(label);
				lob.damageProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Lob: " + newValue + " damage, " + lob.minimumDistanceProperty().get() + " minimum distance");
				});
				lob.minimumDistanceProperty().addChangeListener((oldValue, newValue) -> {
					label.setText("Lob: " + lob.damageProperty().get() + " damage, " + newValue + " minimum distance");
				});
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
	public void selectAction() {
		Level.current().getInfoPanel().getAbilityInfoPanel().setSelectedAbilityPane(this);
		TerrainGrid grid = Level.current().getTerrainPane().getGrid();
		legalsCache = ability.getLegals();
		for(int[] legalSpot : legalsCache) {
			final TerrainTile tile = grid.getTileAt(legalSpot[0], legalSpot[1]);
			if(ability instanceof TargetingAbility) {
				for(GameObjectRepresentation rep : tile.getGameObjectRepresentations()) {
					if(((TargetingAbility) ability).canTarget(rep.getGameObject())) {
						rep.highlight();
						rep.setUseCandidate(true);
					}
				}
			}
			else {
				tile.highlightOrThrow(highlightColor());
				tile.setUseCandidate(true);
			}
		}
	}
	
	/**
	 * Invoked when the user deselects this {@code AbilityPane}. It should hide everything that was displayed by the previous
	 * {@link #selectAction()} call. It must also call {@link AbilityInfoPanel#clearSelectedAbilityPane()}. {@link #isSelected()} is
	 * {@code true} immediately before this method is invoked and is set to {@code false} immediately after this method is invoked.
	 */
	public void deselectAction() {
		Level.current().getInfoPanel().getAbilityInfoPanel().clearSelectedAbilityPane();
		TerrainGrid grid = Level.current().getTerrainPane().getGrid();
		for(int[] legalSpot : legalsCache) {
			final TerrainTile tile = grid.getTileAt(legalSpot[0], legalSpot[1]);
			tile.clearAllHighlights();
			tile.clearGameObjectHighlights();
			tile.setUseCandidate(false);
			tile.setGameObjectUseCandidates(false);
		}
	}
	
	public Paint highlightColor() {
		return TerrainTile.DEFAULT_HIGHLIGHT;
	}
}
