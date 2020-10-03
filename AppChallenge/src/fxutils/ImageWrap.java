package fxutils;

import javafx.scene.image.*;

/**
 * @author Sam Hooper
 *
 */
public class ImageWrap extends ImageView {
	
	public static final int DEFAULT_MIN_WIDTH = 40;
	public static final int DEFAULT_MIN_HEIGHT = 40;
	public static final int MAX_WIDTH = 40;
	public static final int MAX_HEIGHT = 40;
	
	private final int minWidth, minHeight;
	
	/** Creates a new {@code ImageWrap} with no image, a minimum width of {@link #DEFAULT_MIN_WIDTH},
	 * and a minimum height of {@link #DEFAULT_MIN_HEIGHT}.*/
	public ImageWrap() {
		this((Image) null, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
	}
	
	/** Creates a new {@code ImageWrap} with no image, a minimum width of {@code minWidth},
	 * and a minimum height of {@code minHeight}.*/
	public ImageWrap(int minWidth, int minHeight) {
		this((Image) null, minWidth, minHeight);
    }
	
	/** Creates a new {@code ImageWrap} with the image indicated by the given filename,
	 *  a minimum width of {@link #DEFAULT_MIN_WIDTH},
	 * and a minimum height of {@link #DEFAULT_MIN_HEIGHT}. The image file must be in the
	 * "resources" folder.*/
	public ImageWrap(String filename) {
		this(Images.getImage(filename), DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
	}
	
	/** Creates a new {@code ImageWrap} with the image indicated by the given filename,
	 *  a minimum width of {@code minWidth}, and a minimum height of {@code minHeight}.
	 *  The image file must be in the "resources" folder.*/
	public ImageWrap(String filename, int minWidth, int minHeight) {
		this(Images.getImage(filename), minWidth, minHeight);
	}
	
	/** Creates a new {@code ImageWrap} with the given image, a minimum width of {@link #DEFAULT_MIN_WIDTH},
	 * and a minimum height of {@link #DEFAULT_MIN_HEIGHT}. The given image may be null.*/
	public ImageWrap(Image image) {
		this(image, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
	}
	
	/** Creates a new {@code ImageWrap} with the given image, a minimum width of {@code minWidth},
	 * and a minimum height of {@code minHeight}. The given image may be null.*/
	public ImageWrap(Image image, int minWidth, int minHeight) {
		setImage(image);
		this.minWidth = minWidth;
        this.minHeight = minHeight;
		setPreserveRatio(false);
	}

    @Override
    public double minWidth(double height) {
        return minWidth;
    }

    @Override
    public double prefWidth(double height) {
        Image im = getImage();
        if (im == null)
        	return minWidth(height);
        return im.getWidth();
    }

    @Override
    public double maxWidth(double height) {
        return MAX_WIDTH;
    }

    @Override
    public double minHeight(double width) {
        return minHeight;
    }

    @Override
    public double prefHeight(double width)
    {
        Image im = getImage();
        if (im == null)
        	return minHeight(width);
        return im.getHeight();
    }

    @Override
    public double maxHeight(double width) {
        return MAX_HEIGHT;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }
}
