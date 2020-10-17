package graphics;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Obstacle;
import logic.TileType;

/**
 * @author Sam Hooper
 *
 */
public enum Theme {
	DEFAULT_THEME {
		private static final String TILE_DESCRIPTION = "A Grass Tile.";
		private static final String SMALL_OBSTACLE_DESCRIPTION = "A tree.";
		private static final String LARGE_OBSTACLE_DESCRIPTION = "A mountain.";
		
		private final ImageInfo background = new ImageInfo("Background.png");
		private final ImageInfo grass1 = new ImageInfo("Grass1.png");
		private final ImageInfo grass2 = new ImageInfo("Grass2.png");
		private final ImageInfo water1 = new ImageInfo("Water1.png");
		private final ImageInfo water2 = new ImageInfo("Water2.png");
		private final ImageInfo smallObstacleInfo = new ImageInfo("Tree.png");
		private final ImageInfo largeObstacleInfo = new ImageInfo("Mountain.png");
		private final Effect highlightEffect = new InnerShadow(BlurType.GAUSSIAN, Color.LAWNGREEN, 8, .1, 0, 0);
		
		@Override
		public Image imageFor(Obstacle obstacle) {
			return switch(obstacle.getSize()) {
			case SMALL -> smallObstacleInfo.getImage();
			case LARGE -> largeObstacleInfo.getImage();
			default -> throw new UnsupportedOperationException("Unsupported size: " + obstacle.getSize());
			};
			
		}
		@Override
		public String tileDescription() {
			return TILE_DESCRIPTION;
		}
		@Override
		public Image solidTileImage() {
			return (Math.random() < 0.5 ? grass1 : grass2).getImage();
		}
		@Override
		public Image liquidTileImage() {
			return (Math.random() < 0.5 ? water1 : water2).getImage();
		}
		@Override
		public Effect highlightEffect() {
			return highlightEffect;
		}
		@Override
		public Image backgroundImage() {
			return background.getImage();
		}
		@Override
		protected String obstacleDescription(Obstacle obstacle) {
			return switch(obstacle.getSize()) {
			case SMALL -> SMALL_OBSTACLE_DESCRIPTION;
			case LARGE -> LARGE_OBSTACLE_DESCRIPTION;
			default -> throw new UnsupportedOperationException("Unsupported Obstacle size: " + obstacle.getSize());
			};
		}
	};
	
	public Image imageForTileType(TileType type) {
		return switch(type) {
		case SOLID -> solidTileImage();
		case LIQUID -> liquidTileImage();
		default -> throw new UnsupportedOperationException("Tile type is unsupported: " + type);
		};
	}
	public abstract Image solidTileImage();
	public abstract Image liquidTileImage();
	public abstract Image backgroundImage();
	public abstract Image imageFor(Obstacle obstacle);
	/**
	 * The description that will be displayed in the {@link InfoPanel} when a tile is clicked on.
	 */
	public abstract String tileDescription();
	public abstract Effect highlightEffect();
	/**
	 * @param obstacle
	 * @return
	 */
	protected abstract String obstacleDescription(Obstacle obstacle);
}
