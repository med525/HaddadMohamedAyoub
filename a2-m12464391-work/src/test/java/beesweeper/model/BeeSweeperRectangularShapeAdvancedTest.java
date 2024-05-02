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
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Tests for {@link BeeSweeper}. */
public class BeeSweeperRectangularShapeAdvancedTest {

  /** Number of rows of rectangular playing field, given to BeeSweeper as argument. */
  private final int NUM_ROWS = 51;

  /** Number of columns of rectangular playing field, given to BeeSweeper as argument. */
  private final int NUM_COLS = 76;

  /** Expected number of cells on a rectangular playing field with the given rows and columns. */
  private final int NUM_CELLS = 3876;

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testGameDimensionsAboveShell() {
    final int numBees = 1;
    BeeSweeper game = BeeSweeper.newRectangularGame(NUM_COLS, NUM_ROWS, numBees);

    int rows = game.getGameState().getField().getMaxRow();
    int columns = game.getGameState().getField().getMaxColumn();

    assertEquals(NUM_ROWS, rows);
    assertEquals(NUM_COLS, columns);
    assertEquals(numBees, game.getGameState().getField().getFlowersAvailable());
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
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

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testMarkAndUnmarkKeepGameStateConsistent() {
    final int numBees = NUM_CELLS / 3;
    BeeSweeper game = BeeSweeper.newRectangularGame(NUM_COLS, NUM_ROWS, numBees);

    List<Coordinate> allCoordinates =
        BeeSweeperTestUtils.getRectBoardCoordinates(NUM_COLS, NUM_ROWS);
    BeeSweeperTestUtils.assertMarkAndUnmarkAreConsistent(game, allCoordinates, numBees);
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testWinGameWithMark() {
    ShapeFactory shapeFactory = new RectangularShapeFactory(NUM_COLS, NUM_ROWS);

    List<Coordinate> beesToTest =
        List.of(
            // Fields in upper left
            Coordinate.of(0, 0),
            Coordinate.of(0, 1),
            Coordinate.of(1, 0),
            // Fields in lower left
            Coordinate.of(NUM_ROWS - 1, 0),
            Coordinate.of(NUM_ROWS - 2, 0),
            Coordinate.of(NUM_ROWS - 1, 1),
            Coordinate.of(NUM_ROWS - 2, 1),
            // Fields in upper right
            Coordinate.of(0, NUM_COLS - 1),
            Coordinate.of(0, NUM_COLS - 2),
            Coordinate.of(1, NUM_COLS - 1),
            Coordinate.of(1, NUM_COLS - 2),
            // Fields in lower right
            Coordinate.of(NUM_ROWS - 1, NUM_COLS - 1),
            Coordinate.of(NUM_ROWS - 2, NUM_COLS - 2),
            // Fields in middle left
            Coordinate.of(NUM_ROWS / 2, 0),
            Coordinate.of(NUM_ROWS / 2, 1),
            // Fields in middle right
            Coordinate.of(NUM_ROWS / 2, NUM_COLS - 1),
            Coordinate.of(NUM_ROWS / 2, NUM_COLS - 2),
            // Fields in middle top
            Coordinate.of(0, NUM_COLS / 2),
            Coordinate.of(1, NUM_COLS / 2),
            // Fields in middle bottom
            Coordinate.of(NUM_ROWS - 1, NUM_COLS / 2),
            Coordinate.of(NUM_ROWS - 2, NUM_COLS / 2));

    for (Coordinate c : beesToTest) {
      final List<Coordinate> beeCoordinates = Collections.singletonList(c);
      CoordinateGenerator beeGenerator =
          (n, shape) -> {
            assert n == 1;
            return beeCoordinates;
          };

      GameField field = new GameFieldFactory(shapeFactory, beeGenerator).create(1, 1);
      BeeSweeper game = new BeeSweeper(field);

      BeeSweeperTestUtils.assertGameIsWonAfterAllMarked(game, beeCoordinates);
    }
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testWinGameWithTwoMarks() {
    ShapeFactory shapeFactory = new RectangularShapeFactory(NUM_COLS, NUM_ROWS);

    final Coordinate beeCoordinate1 = Coordinate.of(0, 1);
    final Coordinate beeCoordinate2 = Coordinate.of(1, 1);
    List<Coordinate> coords = new ArrayList<>(2);
    coords.add(beeCoordinate1);
    coords.add(beeCoordinate2);

    CoordinateGenerator beeGenerator =
        (n, shape) -> {
          assert n == 2;
          return coords;
        };

    GameField field = new GameFieldFactory(shapeFactory, beeGenerator).create(2, 2);
    BeeSweeper game = new BeeSweeper(field);

    BeeSweeperTestUtils.assertGameIsWonAfterAllMarked(game, coords);
  }
}
