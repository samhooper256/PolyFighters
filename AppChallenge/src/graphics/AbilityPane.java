package graphics;

import java.util.*;
import java.util.function.Function;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
	
	protected AbilityPane(Ability ability) {
		this.ability = ability;
	}
	
	public Ability getAbility() {
		return ability;
	}
}
