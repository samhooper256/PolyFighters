package graphics;

import java.util.*;
import java.util.function.Function;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import logic.*;
import logic.abilities.*;
import utils.*;
/**
 * @author Sam Hooper
 *
 */
public abstract class AbilityPane extends StackPane	{
	
	private static class PaneMap extends HashMap<Class<? extends Ability>, Function<? extends Ability, AbilityPane>>{
		
		public <T extends Ability> Function<T, AbilityPane> getOrThrow(Class<? extends Ability> clazz) {
			Function<T, AbilityPane> function = null;
			boolean error = false;
			try {
				function = (Function<T, AbilityPane>) get(clazz);
				if(function == null)
					error = true;
			}
			catch(ClassCastException e) {
				throw new IllegalStateException("An invalid AbilityPane is registered with the class: " + clazz);
			}
			catch(Exception e) {
				error = true;
			}
			if(error) {
				throw new UnsupportedOperationException("No AbilityPane is registered with the class: " + clazz);
			}
			return function;
		}
	}
	
	private static PaneMap paneFactories;
	
	@FunctionalInterface
	protected interface IntToString {
		String convert(int i);
	}
	
	@FunctionalInterface
	protected interface BoolToString {
		String convert(boolean b);
	}
	
	static {
		paneFactories = new PaneMap();
		put(StepMove.class, a -> new AbilityPane(a) {{
			prop(a.distanceProperty(), "Distance");
		}});
		put(DiamondTeleport.class, a -> new AbilityPane(a) {{
			prop(a.distanceProperty(), "Distance");
		}});
		put(Shoot.class, a -> new AbilityPane(a) {{
			prop(a.damageProperty(), "Damage");
		}});
		put(Melee.class, a -> new AbilityPane(a) {{
			prop(a.damageProperty(), "Damage");
		}});
		put(Smash.class, a -> new AbilityPane(a) {{
			prop(a.damageProperty(), "Damage");
			prop(a.radiusProperty(), "Radius");
		}});
		put(Lob.class, a -> new AbilityPane(a) {{
			prop(a.damageProperty(), "Damage");
			prop(a.minimumDistanceProperty(), "Minimum Distance");
		}});
		put(SelfHeal.class, a -> new AbilityPane(a) {{
			prop(a.healProperty(), "Heal Amount");
		}});
		put(SquareSingleHeal.class, a -> new AbilityPane(a) {{
			prop(a.healProperty(), "Heal Amount");
			prop(a.radiusProperty(), "Radius");
		}});
		put(RadiusSummon.class, a -> new AbilityPane(a) {{
			prop("Sumons " + a.getUnitClass());
			prop(a.radiusProperty(), "Radius");
		}});
	}
	
	private static <T extends Ability> void put(Class<T> clazz, Function<T, AbilityPane> function) {
		paneFactories.put(clazz, function);
	}
	
	public static final <T extends Ability> AbilityPane of(T ability) {
		Objects.requireNonNull(ability);
		Function<T, AbilityPane> factory = paneFactories.getOrThrow(ability.getClass());
		if(factory == null)
			throw new UnsupportedOperationException("Unsupported ability type: " + ability.getClass());
		return factory.apply(ability);
	}
	
	protected final StackPane infoContent;
	protected final VBox vBox;
	protected final Ability ability;
	protected final Button button;
	/** can optionally be used by subclasses store the legal moves computed in the select method for use in the deselect method. */
	protected Collection<int[]> legalsCache = null;
	private boolean selected;
	private boolean selectionEnabled;
	
	protected AbilityPane(Ability ability) {
		this(ability, ability.getClass().getSimpleName().replaceAll("(?=[A-Z])", " "));
	}
	
	/**
	 * The {@link Ability} must be associated with a {@link Unit} when this constructor is called.
	 */
	protected AbilityPane(Ability ability, String buttonText) {
		this.infoContent = new StackPane();
		this.vBox = new VBox();
		this.ability = ability;
		this.button = new Button(buttonText);
		Unit unit = this.ability.getUnit();
		this.selectionEnabled = true;
		if(unit instanceof PlayerUnit) {
			PlayerUnit pu = (PlayerUnit) unit;
			pu.movesRemainingProperty().addChangeListener((oldValue, newValue) -> {
				if(newValue == 0)
					disableSelection();
				else
					enableSelection();
			});
			if(pu.getMovesRemaining() == 0)
				disableSelection();
		}
		selected = false;
		button.setOnMouseClicked(mouseEvent -> {
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
		infoContent.getChildren().add(vBox);
		this.getChildren().add(button);
	}
	
	protected void prop(IntRef property, IntToString list) {
		Label label = new Label();
		label.setText(list.convert(property.get()));
		property.addChangeListener((o, n) -> label.setText(list.convert(n))); //TODO do we need to remove the listeners at some point?
		vBox.getChildren().add(label);
	}
	
	protected void prop(IntRef property, String name) {
		prop(property, v -> name + ":" + v);
	}
	
	protected void prop(String text) {
		vBox.getChildren().add(new Label(text));
	}
	
	public Pane getInfoContent() {
		return infoContent;
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
		final AbilityInfoPanel abilityInfoPanel = Level.current().getInfoPanel().getAbilityInfoPanel();
		abilityInfoPanel.setSelectedAbilityPane(this);
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
		abilityInfoPanel.setInfo(infoContent);
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
	
	public void disableSelection() {
		if(selectionEnabled) {
			button.setDisable(true);
			selectionEnabled = false;
		}
	}
	
	public void enableSelection() {
		if(!selectionEnabled) {
			button.setDisable(false);
			selectionEnabled = true;
		}
	}
	
	public Paint highlightColor() {
		return TerrainTile.DEFAULT_HIGHLIGHT;
	}
}
