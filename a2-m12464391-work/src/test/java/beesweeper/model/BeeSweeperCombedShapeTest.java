package beesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import beesweeper.model.field.Coordinate;
import beesweeper.model.field.GameField;
import beesweeper.model.field.GameFieldFactory;
import beesweeper.model.shape.CoordinateGenerator;
import beesweeper.model.shape.HoneyCombedShapeFactory;
import beesweeper.model.shape.ShapeFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Basic tests for {@link BeeSweeper} with a honeycombed playfield. */
public class BeeSweeperCombedShapeTest {

  private static final int NUM_ROWS_SMALL = 5;

  /** Number of rows of honeycombed playing field, given to BeeSweeper as argument. */
  private final int NUM_ROWS = 51;

  /** Expected number of (max) columns for a honeycombed playing field with the given rows. */
  private final int NUM_COLS = 76;

  /** Expected number of cells on a honeycombed playing field with the given rows. */
  private final int NUM_CELLS = 2576;

  private List<Coordinate> getCombBoardCoordinates(int numRows) {
    final int numCols = (3 * numRows - 1) / 2;
    final int numCells = numRows * numRows - (numRows - 1) / 2;
    List<Coordinate> coordinates = new ArrayList<Coordinate>(numCells);

    int start = (numRows - 1) / 2, end = numRows;
    for (int i = 0; i < numRows; ++i) {
      for (int j = 0; j < numCols; ++j) {
        if (j >= start && j < end) {
          coordinates.add(Coordinate.of(i, j));
        }
      }
      if (i < (numRows - 1) / 2) {
        --start;
        ++end;
      } else {
        ++start;
        --end;
      }
    }
    assert coordinates.size() == numCells;
    return coordinates;
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testGameDimensions() {
    final int numBees = 1;
    BeeSweeper game = BeeSweeper.newCombGame(NUM_ROWS, numBees);

    int rows = game.getGameState().getField().getMaxRow();
    int columns = game.getGameState().getField().getMaxColumn();

    assertEquals(NUM_ROWS, rows);
    assertEquals(NUM_COLS, columns);
    assertEquals(numBees, game.getGameState().getField().getFlowersAvailable());
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testLoseGameKBees() {
    final int numBees = NUM_CELLS / 3;
    final int numEmptyCells = NUM_CELLS - numBees;
    BeeSweeper game = BeeSweeper.newCombGame(NUM_ROWS, numBees);

    // by pigeonhole principle, after randomly revealing numEmptyCells + 1 cells,
    // there will be at least one bee cell being revealed -> game over
    BeeSweeperTestUtils.assertGameIsLostAfterKRandomReveals(
        game, getCombBoardCoordinates(NUM_ROWS), numEmptyCells + 1);
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testMarkAndUnmarkKeepGameStateConsistent() {
    final int numBees = NUM_CELLS / 3;
    BeeSweeper game = BeeSweeper.newCombGame(NUM_ROWS, numBees);

    List<Coordinate> allCoordinates = getCombBoardCoordinates(NUM_ROWS);
    BeeSweeperTestUtils.assertMarkAndUnmarkAreConsistent(game, allCoordinates, numBees);
  }

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testWinGameWithRevealing_singleBee() {
    ShapeFactory shapeFactory = new HoneyCombedShapeFactory(NUM_ROWS_SMALL);

    List<Coordinate> beesToTest =
        List.of(
            // Fields in first row
            Coordinate.of(0, 2),
            Coordinate.of(0, 3),
            Coordinate.of(0, 4),
            // Some fields in second row
            Coordinate.of(1, 1),
            Coordinate.of(1, 3),
            Coordinate.of(1, 5),
            // Fields in last row
            Coordinate.of(NUM_ROWS_SMALL - 1, 2),
            Coordinate.of(NUM_ROWS_SMALL - 1, 3),
            Coordinate.of(NUM_ROWS_SMALL - 1, 4),
            // Some fields in second-to-last row
            Coordinate.of(NUM_ROWS_SMALL - 2, 1),
            Coordinate.of(NUM_ROWS_SMALL - 2, 3),
            Coordinate.of(NUM_ROWS_SMALL - 2, 5),
            // Fields in middle
            Coordinate.of(NUM_ROWS_SMALL / 2, 0),
            Coordinate.of(NUM_ROWS_SMALL / 2, 1),
            Coordinate.of(NUM_ROWS_SMALL / 2, 3),
            Coordinate.of(NUM_ROWS_SMALL / 2, 5),
            Coordinate.of(NUM_ROWS_SMALL / 2, 6));

    for (Coordinate c : beesToTest) {
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

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testWinGameWithRevealing_allButFirstBee() {
    ShapeFactory shapeFactory = new HoneyCombedShapeFactory(NUM_ROWS_SMALL);

    List<Coordinate> bees = new ArrayList<>(shapeFactory.create().getAllCoordinates());
    bees.remove(0);

    CoordinateGenerator beeGenerator =
        (n, shape) -> {
          assert n == bees.size();
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

  @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
  @Test
  public void testWinGameWithRevealing_allButLastBee() {
    ShapeFactory shapeFactory = new HoneyCombedShapeFactory(NUM_ROWS_SMALL);

    List<Coordinate> bees = new ArrayList<>(shapeFactory.create().getAllCoordinates());
    bees.remove(bees.size() - 1);

    CoordinateGenerator beeGenerator =
        (n, shape) -> {
          assert n == bees.size();
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
