package beesweeper.model;

import beesweeper.model.field.Cell;
import beesweeper.model.field.Coordinate;
import beesweeper.model.field.GameField;
import beesweeper.model.field.GameFieldFactory;

import java.util.List;


/** BeeSweeper game. Same rules as Minesweeper, but different setting. */
public class BeeSweeper {
  private final GameField gameField;
  private GameState gameState;


  BeeSweeper(GameField playingField) {
    this.gameField = playingField;
    this.gameState = GameState.create(playingField);
  }


  /** Status of a game operation. */
  public enum OperationStatus {
    /** Status that represents the operation was executed successfully. */
    SUCCESS,
    /** Status that represents the operation reached a cell that is out of bounds. */
    INDEX_OOB,
    /**
     * Status that represents the operation could not be done for other reasons, e.g. trying to mark
     * a cell that has already been revealed.
     */
    FAIL
  }

  /**
   * For testing only. Use {@link #newCombGame(int, int)} and {@link #newRectangularGame(int, int,
   * int)} instead.
   */
  /*BeeSweeper(GameField playingField) {
    throw new UnsupportedOperationException();
  }*/

  /**
   * TODO
   *
   * @param columns
   * @param rows
   * @param numBees
   * @return
   */

  public static BeeSweeper newRectangularGame(int columns, int rows, int numBees) {
    // Implementation to create a rectangular game field
    GameField field = GameFieldFactory.createRectangularGameField(columns, rows, numBees);
    return new BeeSweeper(field);
  }


  /**
   * TODO
   *
   * @param rows
   * @param numBees
   * @return
   */
  public static BeeSweeper newCombGame(int rows, int numBees) {
    // Implementation to create a comb-shaped game field
    GameField field = GameFieldFactory.createCombGameField(rows, numBees);
    return new BeeSweeper(field);
  }

  /**
   * Reveals the cell at the given location.
   *
   * @param coordinate the coordinate of the cell to act on
   * @return the operation status of this method. {@link OperationStatus#SUCCESS} if action was
   *     successful. Otherwise, a corresponding error status.
   */
  public OperationStatus reveal(Coordinate coordinate) {
    if (!gameField.isValidCoordinate(coordinate)) {
      return OperationStatus.INDEX_OOB;
    }

    Cell cell = gameField.getCell(coordinate);

    // Check if the cell has already been revealed or marked
    if (cell.isRevealed() || cell.isMarked()) {
      return OperationStatus.FAIL;
    }

    // Reveal the cell
    cell.setRevealed(true);

    // If the cell is a bee, the game is over
    if (cell.isBee()) {
      gameField.setGameOver(true);
      return OperationStatus.FAIL;
    }

    // If the cell has no surrounding bees, recursively reveal neighboring cells
    if (cell.getNumberOfBeesSurrounding() == 0) {
      revealNeighbors(coordinate);
    }

    // Check if the game is won
    if (gameField.isGameWon()) {
      gameField.setGameWon(true);
      return OperationStatus.SUCCESS;
    }

    return OperationStatus.SUCCESS;
  }

  public OperationStatus revealNeighbors(Coordinate coordinate) {
    // Check if the coordinate is valid
    if (!gameField.isValidCoordinate(coordinate)) {
      return OperationStatus.INDEX_OOB;
    }

    // Get the cell at the given coordinate
    Cell cell = gameField.getCell(coordinate);

    // Check if the cell is already revealed or marked
    if (cell.isRevealed() || cell.isMarked()) {
      return OperationStatus.FAIL;
    }

    // Get the adjacent coordinates
    List<Coordinate> adjacentCoordinates = gameField.getAdjacentCoordinates(coordinate);

    // Reveal each neighbor recursively
    for (Coordinate neighbor : adjacentCoordinates) {
      // Skip if the neighbor is a bee
      if (gameField.getCell(neighbor).isBee()) {
        continue;
      }

      // Reveal the neighbor
      reveal(neighbor);
    }

    return OperationStatus.SUCCESS;
  }


  /**
   * Marks the cell at the given location.
   *
   * @param coordinate the coordinate of the cell to act on
   * @return the operation status of this method. {@link OperationStatus#SUCCESS} if action was
   *     successful. Otherwise, a corresponding error status.
   */
  public OperationStatus mark(Coordinate coordinate) {
    // Implementation to mark the cell at the given coordinate
    // (not implemented here)
    throw new UnsupportedOperationException("Method not implemented yet");
  }

  /**
   * Unmarks the cell at the given location.
   *
   * @param coordinate the coordinate of the cell to act on
   * @return the operation status of this method. {@link OperationStatus#SUCCESS} if action was
   *     successful. Otherwise, a corresponding error status.
   */
  public OperationStatus unmark(Coordinate coordinate) {
    // Implementation to unmark the cell at the given coordinate
    // (not implemented here)
    throw new UnsupportedOperationException("Method not implemented yet");
  }
  /** Gets the current GameState. */
  public final GameState getGameState() {
    return gameState;
  }

}
