package beesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import beesweeper.model.field.Coordinate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class BeeSweeperTestUtils {

  // Utility class with only static methods
  private BeeSweeperTestUtils() {}

  static void assertMarkAndUnmarkAreConsistent(
      BeeSweeper game, List<Coordinate> coordinates, int numBees) {
    int numFlowers = numBees;
    List<Coordinate> markedCoordinates = getKRandomCoordinates(coordinates, numBees);
    assertEquals(numFlowers, game.getGameState().getField().getFlowersAvailable());
    assertGameIsAlive(game);

    // test mark
    for (Coordinate p : markedCoordinates) {
      game.mark(p);
      // the number of flowers left is decremented by 1 due to mark
      numFlowers--;
      assertEquals(numFlowers, game.getGameState().getField().getFlowersAvailable());
      // marking should not change the GameState
      assertGameIsAlive(game);
    }

    // test unmark
    Collections.shuffle(markedCoordinates);
    for (Coordinate p : markedCoordinates) {
      game.unmark(p);
      // the number of flowers left is incremented by 1 due to unmark
      numFlowers++;
      assertEquals(numFlowers, game.getGameState().getField().getFlowersAvailable());
      // unmarking should not change the GameState
      assertGameIsAlive(game);
    }
  }

  static void assertGameIsAlive(final BeeSweeper game) {
    final GameState gameState = game.getGameState();
    assertTrue(gameState.isGameAlive());
    assertFalse(gameState.isGameWon());
    assertFalse(gameState.isGameOver());
  }

  static void assertGameIsLost(final BeeSweeper game) {
    final GameState gameState = game.getGameState();
    assertFalse(gameState.isGameAlive());
    assertFalse(gameState.isGameWon());
    assertTrue(gameState.isGameOver());
  }

  static void assertGameIsWon(final BeeSweeper game) {
    final GameState gameState = game.getGameState();
    assertFalse(gameState.isGameAlive());
    assertTrue(gameState.isGameWon());
    assertFalse(gameState.isGameOver());
  }

  static void assertGameIsLostAfterKRandomReveals(
      BeeSweeper game, List<Coordinate> coordinates, int k) {
    for (Coordinate p : getKRandomCoordinates(coordinates, k)) {
      assertGameIsAlive(game);
      game.reveal(p);
      if (game.getGameState().isGameOver()) {
        break;
      }
    }

    assertGameIsLost(game);
  }

  static void assertGameIsWonAfterAllMarked(BeeSweeper game, List<Coordinate> cellsToMark) {
    // Preconditions: Game must be running and game can only be won if all available flowers are
    // used
    assertGameIsAlive(game);
    assertEquals(cellsToMark.size(), game.getGameState().getField().getFlowersAvailable());

    for (Coordinate toMark : cellsToMark) {
      assertGameIsAlive(game);
      game.mark(toMark);
    }

    assertEquals(0, game.getGameState().getField().getFlowersAvailable());
    assertGameIsWon(game);
  }

  static List<Coordinate> getRectBoardCoordinates(int numCols, int numRows) {
    final int numCells = numRows * numCols;
    List<Coordinate> coordinates = new ArrayList<Coordinate>(numCells);
    for (int i = 0; i < numRows; ++i) {
      for (int j = 0; j < numCols; ++j) {
        coordinates.add(Coordinate.of(i, j));
      }
    }
    assert coordinates.size() == numCells;
    return coordinates;
  }

  private static List<Coordinate> getKRandomCoordinates(List<Coordinate> coordinates, int k) {
    Collections.shuffle(coordinates);
    return coordinates.subList(0, k);
  }

  public static void assertGameIsWonAfterAllRevealed(
      BeeSweeper game, Collection<Coordinate> cellsToReveal) {
    // Preconditions: Game must be running and game can only be won if all non-bee cells are
    // revealed
    assertGameIsAlive(game);
    assertEquals(
        cellsToReveal.size(),
        game.getGameState().getField().getAllCoordinates().size()
            - game.getGameState().getField().getFlowersAvailable());

    for (Coordinate toReveal : cellsToReveal) {
      assertGameIsAlive(game);
      game.reveal(toReveal);
    }
    assertGameIsWon(game);
  }
}
