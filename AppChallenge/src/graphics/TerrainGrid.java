package graphics;

import fxutils.Borders;
import fxutils.ImageWrap;
import javafx.animation.*;
import javafx.beans.binding.DoubleBinding;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import logic.*;
import logic.abilities.SingleProjectileAbility;
import logic.actions.*;

/**
 * @author Sam Hooper
 *
 */
public class TerrainGrid extends GridPane {
	
	public static final long ENEMY_MOVE_DELAY = 500; //in milliseconds. The wait time between each enemy move.
	private final int rows, cols;
	private final Board backingBoard;
	private final TerrainTile[][] terrainTiles;
	private final Theme theme;
	private final TerrainGridWrap wrap;
	
	/** Creates a new {@code TerrainGrid} with {@code size} rows and {@code size} columns. */
	public TerrainGrid(Level level, TerrainGridWrap wrap, int size) {
		this(level, wrap, size, size);
	}
	
	/** Creates a new {@code TerrainGrid} with the given amount of rows and columns */
	public TerrainGrid(final Level level, TerrainGridWrap wrap, int rows, int cols) {
		super();
		this.theme = level.getTheme();
		this.rows = rows;
		this.cols = cols;
		this.wrap = wrap;
		backingBoard = new BoardGenerator()
				.setRowCount(rows)
				.setColumnCount(cols)
				.setTeamUnits(Main.getPlayer().getUnitsUnmodifiable())
				.setTurnDifficulty(level.getTurnDifficulty())
				.build();
		terrainTiles = new TerrainTile[rows][cols];
		initConstraints();
		initTiles();
	}
	
	/** Must only be called from constructor. {@link #rows} and {@link #cols} must be initialized. */
	private void initConstraints() {
		initRowConstraints();
		initColumnConstraints();
	}
	
	/** Must only be called from {@link #initConstraints()}. {@link #rows} must be initialized. */
	private void initRowConstraints() {
		for(int i = 0; i < rows; i++) {
			RowConstraints rc = new RowConstraints();
			rc.setPercentHeight(100.0/rows);
			getRowConstraints().add(rc);
		}
	}
	
	/** Must only be called from {@link #initConstraints()}. {@link #cols} must be initialized. */
	private void initColumnConstraints() {
		for(int i = 0; i < cols; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(100.0/cols);
			getColumnConstraints().add(cc);
		}
	}
	
	/** Creates the tiles for this {@code TerrainGrid}. Must only be called from the constructor.
	 * {@link #rows}, {@link #cols}, and {@link #backingBoard} must be initialized.*/
	private void initTiles() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				TerrainTile tile = TerrainTile.forBoardTile(this, backingBoard.getTileAt(i, j)); //TODO use level theme
				terrainTiles[i][j] = tile;
				GridPane.setConstraints(tile, j, i); //this method takes (col, row) instead of (row, col). That's why j comes before i.
				getChildren().add(tile);
			}
		}
	}
	
	public Board getBackingBoard() {
		return backingBoard;
	}
	
	public BoardTile getBackingBoardTile(int row, int col) {
		return backingBoard.getTileAt(row, col);
	}
	
	public TerrainTile getTileAt(int row, int col) {
		return terrainTiles[row][col];
	}
	
	public boolean isInBounds(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}
	
	/**
	 * Adds the given {@link GameObject} to this {@link TerrainGrid} at the given tile, and updates the backing board. The
	 * given {@code GameObject} must be a {@link Unit} or an {@link Obstacle}.
	 */
	public void addOrThrow(GameObject object, int row, int col) {
		if(object instanceof Unit) {
			addUnitOrThrow((Unit) object, row, col);
		}
		else if(object instanceof Obstacle) {
			addObstacleOrThrow((Obstacle) object, row, col);
		}
		else {
			throw new IllegalArgumentException("Must be Unit or Obstacle");
		}
	}
	public void addUnitOrThrow(Unit unit, int row, int col) {
		backingBoard.addUnitOrThrow(unit, row, col);
		terrainTiles[row][col].getUnitPane().setUnit(unit);
	}
	
	/**
	 * @throws IllegalStateException
	 */
	public void addObstacleOrThrow(Obstacle obstacle, int row, int col) {
		backingBoard.addObstacleOrThrow(obstacle, row, col); //will throw if an obstacle is already present
		terrainTiles[row][col].setObstacle(obstacle);
	}
	
	public Theme getTheme() {
		return theme;
	}
	
	private volatile boolean moveNotify;
	
	private final MoveService MOVE_SERVICE = new MoveService();
	private final EnemyTurnService ENEMY_TURN_SERVICE = new EnemyTurnService();
	class MoveService extends Service<Void> {
		private volatile Move move;
		@Override
		protected Task<Void> createTask() {
			return new Task<>() {
				@Override
				protected Void call() throws Exception {
					try {
						TerrainGrid.this.executeMoveInternal(move);
						return null;
					}
					catch(Exception e) { 
						e.printStackTrace();
					}
					return null;
				}
				
			};
		}
		
		public void executeMove(Move move) {
			this.move = move;
			this.restart();
		}
	};
	
	class EnemyTurnService extends Service<Void> {
		@Override
		protected Task<Void> createTask() {
			return new Task<>() {
				@Override
				protected Void call() throws Exception {
					try {
						playEnemyTurnInternalAndStackTurnBackToPlayer();
					}
					catch(Exception e) { 
						e.printStackTrace();
					}
					return null;
				}
				
			};
		}
		
		public void playEnemyTurn() {
			this.restart();
		}
	};
	
	/**
	 * Executes the {@link Move} on a background thread. Updates the moves remaining value of the {@link Move Move's} unit. Disables the
	 * {@link AbilityInfoPanel} if the moves remaining value becomes zero.
	 * @param move
	 */
	public void executeMove(Move move) {
		MOVE_SERVICE.executeMove(move);
	}
	
	private void executeMoveInternal(final Move move) {
		System.out.printf("(enter) executeMoveInternal(%s), thread = %s%n", move, Thread.currentThread().getName());
		final AbilityInfoPanel abilityInfoPanel = Level.current().getInfoPanel().getAbilityInfoPanel();
		final AbilityPane selectedAbilityPane = abilityInfoPanel.getSelectedAbilityPane();
		if(selectedAbilityPane != null)
			Main.runOnFXAndBlock(() -> selectedAbilityPane.deselect());
		final Ability actingAbility = move.getAbility();
		final Unit actingUnit = actingAbility.getUnit();
		if(actingUnit instanceof PlayerUnit) {
			PlayerUnit pu = (PlayerUnit) actingUnit;
			pu.setMovesRemaining(pu.getMovesRemaining() - 1); //TODO this should probably be done by the Board? But how when we have to delay between actions b/c of projectiles and the like?
			Main.runOnFXAndBlock(() -> abilityInfoPanel.updateMovesRemaining());
		}
		if(move.isEmpty())
			 return;
		final Pane region = wrap.getRegion();
		final TerrainTile actingStartTile = getTileAt(actingUnit.getRow(), actingUnit.getCol());
		final UnitPane actingUnitPane = getTileAt(actingUnit.getRow(), actingUnit.getCol()).getUnitPane();
		final int actingRow = actingUnit.getRow();
		final int actingCol = actingUnit.getCol();
		final UnitSkin actingSkin = UnitSkin.forUnitOrDefault(actingUnit);
		for(Action a : move.getActionsUnmodifiable()) {
//			System.out.printf("\tentered loop, a=%s%n", a);
			if(a instanceof Relocate) {
				Main.runOnFXAndBlock(() -> {
					Relocate r = (Relocate) a;
					r.execute(backingBoard);
					TerrainTile startTile = terrainTiles[r.getStartRow()][r.getStartCol()];
					UnitPane startUnitPane = startTile.getUnitPane();
					Unit unit = startUnitPane.removeUnit();
					TerrainTile destTile = terrainTiles[r.getDestRow()][r.getDestCol()];
					UnitPane destUnitPane = destTile.getUnitPane();
					destUnitPane.setUnit(unit);
				});
			}
			else if(a instanceof FireProjectile) {
				FireProjectile fp = (FireProjectile) a;
				if(actingAbility instanceof SingleProjectileAbility) {
					SingleProjectileAbility spa = (SingleProjectileAbility) actingAbility;
					final GameObject target = fp.getTarget();
					final TerrainTile destTile = getTileAt(target.getRow(), target.getCol());
					double defRot = actingSkin.getDefaultRotationFor(spa);
					Image image = actingSkin.projectileImageFor(spa);
					ImageWrap wrap = new ImageWrap(image, 0, 0);
					double[] imgSize = actingSkin.projectileSizeFor(spa);
					StackPane pane = new StackPane(wrap);
					final int rowDiff = actingRow - destTile.getRow();
					final int colDiff = actingCol - destTile.getCol();
					double rot = Math.atan(1.0 * rowDiff / colDiff) / Math.PI * 180;
					if(colDiff > 0)
						rot += 180;
					pane.setRotate(defRot - rot);
					final DoubleBinding widthBinding = actingStartTile.widthProperty().multiply(imgSize[0]);
					final DoubleBinding heightBinding = actingStartTile.heightProperty().multiply(imgSize[1]);
					final DoubleBinding startX = actingStartTile.layoutXProperty().add(actingStartTile.widthProperty().divide(2)).subtract(widthBinding.divide(2));
					final DoubleBinding startY = actingStartTile.layoutYProperty().add(actingStartTile.heightProperty().divide(2)).subtract(heightBinding.divide(2));
					final DoubleBinding destX = destTile.layoutXProperty().add(destTile.widthProperty().divide(2)).subtract(widthBinding.divide(2));
					final DoubleBinding destY = destTile.layoutYProperty().add(destTile.heightProperty().divide(2)).subtract(heightBinding.divide(2));
					pane.setLayoutX(startX.get());
					pane.setLayoutY(startY.get());
					pane.prefWidthProperty().bind(widthBinding);
					pane.prefHeightProperty().bind(heightBinding);
					Main.runOnFXAndBlock(() -> region.getChildren().add(pane));
					double tileDistance = Math.sqrt(Math.pow(destTile.getRow() - actingRow, 2) + Math.pow(destTile.getCol() - actingCol, 2));
					Object lock = new Object();
					moveNotify = false;
					final Transition transition = new Transition() {
						{
							setCycleDuration(Duration.millis(tileDistance * 100));
						}
						@Override
						protected void interpolate(double frac) {
							double x = startX.get() + (destX.get() - startX.get()) * frac;
							double y = startY.get() + (destY.get() - startY.get()) * frac;
							pane.relocate(x, y);
						}
					};
					transition.setOnFinished(actionEvent -> {
							region.getChildren().remove(pane);
							a.execute(backingBoard);
							moveNotify = true;
							synchronized(lock) {
								lock.notify();
							}
					});
					transition.setInterpolator(Interpolator.LINEAR);
					transition.play();
					synchronized(lock) {
						while(!moveNotify) {
							try {
								lock.wait();
							}
							catch (InterruptedException e) {}
						}
					}
				}
				else {
					throw new UnsupportedOperationException("Unsupported ability type for a FireProjectile action: " + actingAbility.getClass());
				}
			}
			else if(a instanceof ChangeHealth) {
				Main.runOnFXAndBlock(() -> a.execute(backingBoard)); //needs to be on FX thread since this will trigger listeners, which modify the HealthBar display.
			}
			else if(a instanceof PlaceObject) {
				PlaceObject po = (PlaceObject) a;
				final GameObject obj = po.getObject();
				final int row = po.getRow(), col = po.getCol();
				Main.runOnFXAndBlock(() -> addOrThrow(obj, row, col)); //don't even need to execute the action, this updates the backing board.
			}
			else {
				throw new UnsupportedOperationException("Unsupported action type: " + a.getClass());
			}
		}
		System.out.printf("ESCAPED%n");
	}

	public Turn getTurn() {
		return backingBoard.getTurn();
	}
	
	/**
	 * Assumes that the current {@link #getTurn() turn} is {@link Turn#PLAYER the player's}. Sets the turn to {@link Turn#ENEMY the enemy's}
	 * and plays out the enemy's turn.
	 */
	public void playEnemyTurn() {
		ENEMY_TURN_SERVICE.playEnemyTurn();
	}
	
	/**
	 * Must NOT be run on FX Thread.
	 */
	private void playEnemyTurnInternal() {
		backingBoard.setToEnemyTurn();
		Main.runOnFXAndBlock(() -> Level.current().getInfoPanel().clearContent());
		while(backingBoard.hasNextEnemyMove()) {
			Move nextMove = backingBoard.nextEnemyMove();
			executeMoveInternal(nextMove);
			try {
				Thread.sleep(ENEMY_MOVE_DELAY);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void playEnemyTurnInternalAndStackTurnBackToPlayer() {
		playEnemyTurnInternal();
		TerrainGrid.this.backingBoard.setToPlayerTurn();
		Level.current().enemyTurnFinished();
	}
	
}
