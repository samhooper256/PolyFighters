package graphics;

import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * A {@link Rectangle} that can be overlayed on another {@link Region} to serve as a "highlight" of that {@code Region}. The
 * {@link #widthProperty()} and {@link #heightProperty()} of the {@code Highlight} are bound to the corresponding properties
 * of the {@code Region} provided for the constructors. The {@code Highlight} does not keep a reference to the {@code Region} it
 * is displayed over.
 * 
 * @author Sam Hooper
 *
 */
public class Highlight extends Rectangle {
	
	public static final double DEFAULT_HIGHLIGHT_OPACITY = .5;
	
	public Highlight(Region region, Paint paint) {
		this(region, paint, DEFAULT_HIGHLIGHT_OPACITY);
	}
	
	public Highlight(Region region, Paint paint, double opacity) {
		setFill(paint);
		setOpacity(opacity);
		this.widthProperty().bind(region.widthProperty());
		this.heightProperty().bind(region.heightProperty());
	}
}
