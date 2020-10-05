package graphics;

import java.util.Objects;

import fxutils.Images;
import javafx.scene.image.Image;

/**
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
