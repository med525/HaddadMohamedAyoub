package beesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import beesweeper.model.field.Coordinate;
import beesweeper.model.field.GameField;
import beesweeper.model.field.GameFieldFactory;
import beesweeper.model.shape.CoordinateGenerator;
import beesweeper.model.shape.RectangularShapeFactory;
import beesweeper.model.shape.ShapeFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Basic tests for {@link BeeSweeper} with a rectangular game field. */
public class BeeSweeperRectangularShapeBasicTest {

  /** Number of rows of rectangular playing field, given to BeeSweeper as argument. */
  private final int NUM_ROWS = 5;

  /** Number of columns of rectangular playing field, given to BeeSweeper as argument. */
  private final int NUM_COLS = 5;

  /** Expected number of cells on a rectangular playing field with the given rows and columns. */
  private final int NUM_CELLS = 25;

  @Test
  public void testGameDimensions() {
    final int numBees = 1;
    BeeSweeper game = BeeSweeper.newRectangularGame(NUM_COLS, NUM_ROWS, numBees);

    int rows = game.getGameState().getField().getMaxRow();
    int columns = game.getGameState().getField().getMaxColumn();

    assertEquals(NUM_ROWS, rows);
    assertEquals(NUM_COLS, columns);
    assertEquals(numBees, game.getGameState().getField().getFlowersAvailable());
  }

  @Test
  public void testLoseGameWithKBees() {
    final int numBees = NUM_CELLS / 3;
    final int numEmptyCells = NUM_CELLS - numBees;
    BeeSweeper game = BeeSweeper.newRectangularGame(NUM_COLS, NUM_ROWS, numBees);

    // by pigeonhole principle, after randomly revealing numEmptyCells + 1 cells,
    // there will be at least one bee cell being revealed -> game over
    BeeSweeperTestUtils.assertGameIsLostAfterKRandomReveals(
        game, BeeSweeperTestUtils.getRectBoardCoordinates(NUM_COLS, NUM_ROWS), numEmptyCells + 1);
  }

  @Test
  public void testWinGameWithRevealing_singleBee() {
    ShapeFactory shapeFactory = new RectangularShapeFactory(NUM_COLS, NUM_ROWS);

    for (Coordinate c : shapeFactory.create().getAllCoordinates()) {
      final Coordinate beeCoordinate = c;
      CoordinateGenerator beeGenerator =
          (n, shape) -> {
            assert n == 1;
            return Collections.singleton(beeCoordinate);
          };

      GameField field = new GameFieldFactory(shapeFactory, beeGenerator).create(1, 1);
      BeeSweeper game = new BeeSweeper(field);

      Set<Coordinate> cellsNotBee = new HashSet<>(field.getAllCoordinates());
      cellsNotBee.remove(beeCoordinate);

      BeeSweeperTestUtils.assertGameIsWonAfterAllRevealed(game, cellsNotBee);
    }
  }

  @Test
  public void testWinGameWithRevealing_allButFirstBee() {
    ShapeFactory shapeFactory = new RectangularShapeFactory(NUM_COLS, NUM_ROWS);

    List<Coordinate> bees = new ArrayList<>(shapeFactory.create().getAllCoordinates());
    bees.remove(0);

    assert NUM_COLS * NUM_ROWS - 1 == bees.size();

    CoordinateGenerator beeGenerator =
        (n, shape) -> {
          assert n == NUM_COLS * NUM_ROWS - 1;
          return bees;
        };

    GameField field =
        new GameFieldFactory(shapeFactory, beeGenerator).create(bees.size(), bees.size());
    BeeSweeper game = new BeeSweeper(field);

    Set<Coordinate> cellsNotBee = new HashSet<>(field.getAllCoordinates());
    cellsNotBee.removeAll(bees);
    assert cellsNotBee.size() == 1;

    BeeSweeperTestUtils.assertGameIsWonAfterAllRevealed(game, cellsNotBee);
  }

  @Test
  public void testWinGameWithRevealing_allButLastBee() {
    ShapeFactory shapeFactory = new RectangularShapeFactory(NUM_COLS, NUM_ROWS);

    List<Coordinate> bees = new ArrayList<>(shapeFactory.create().getAllCoordinates());
    bees.remove(bees.size() - 1);

    assert NUM_COLS * NUM_ROWS - 1 == bees.size();

    CoordinateGenerator beeGenerator =
        (n, shape) -> {
          assert n == NUM_COLS * NUM_ROWS - 1;
          return bees;
        };

    GameField field =
        new GameFieldFactory(shapeFactory, beeGenerator).create(bees.size(), bees.size());
    BeeSweeper game = new BeeSweeper(field);

    Set<Coordinate> cellsNotBee = new HashSet<>(field.getAllCoordinates());
    cellsNotBee.removeAll(bees);
    assert cellsNotBee.size() == 1;

    BeeSweeperTestUtils.assertGameIsWonAfterAllRevealed(game, cellsNotBee);
  }
}
