package graphics;

import java.util.Objects;

import fxutils.Images;
import javafx.scene.image.Image;

/**
 * A data class for storing an {@link javafx.scene.image.Image} and its filename. The {@link #getImage()} method returns the image. The
 * image is computed only once, at the first time that {@link #getImage()} is called. The value is cached so later calls to {@link #getImage()}
 * just returned the already computed {@code Image}.
 * @author Sam Hooper
 *
 */
public final class ImageInfo {
	
	private final String filename;
	private Image image; //lazy initialized, so not final
	
	public ImageInfo(String filename) {
		Objects.requireNonNull(filename);
		this.filename = filename;
		image = null;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public Image getImage() {
		if(image == null)
			image = Images.getImage(filename);
		return image;
	}
	
}
