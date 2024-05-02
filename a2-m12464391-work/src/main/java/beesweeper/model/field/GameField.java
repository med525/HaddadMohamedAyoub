package beesweeper.model.field;

import beesweeper.model.BeeSweeper;
import beesweeper.model.shape.FieldShape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents the game field of the {@link BeeSweeper} game. Consists of the field shape
 * with the current cell status, and the number of flowers that are available for use. It is
 * responsible for all internal cell manipulation.
 *
 * <p>Users of the {@link BeeSweeper} game logic should avoid using methods of this class, if
 * possible, and rely on methods provided by {@link BeeSweeper}.
 */
public final class GameField {

  private final FieldShape field;
  private int flowersAvailable;
  private boolean gameWon;
  private boolean gameOver;


  GameField(FieldShape shape, int numFlowers) {
    field = shape;
    flowersAvailable = numFlowers;
  }

  public boolean contains(Coordinate coordinate) {
    return field.contains(coordinate);
  }

  public Cell get(Coordinate coordinate) {
    return field.get(coordinate);
  }

  /** Returns coordinates of all bees on the game field. */
  public Collection<Coordinate> getAllBeeCoordinates() {
    Collection<Coordinate> bees = new ArrayList<>();
    for (Coordinate c : field.getAllCoordinates()) {
      if (field.get(c).isBee()) {
        bees.add(c);
      }
    }
    return bees;
  }

  /** Returns coordinates of all marked cells on the game field. */
  public Collection<Coordinate> getAllMarkedCoordinates() {
    Collection<Coordinate> marked = new ArrayList<>();
    for (Coordinate c : field.getAllCoordinates()) {
      if (field.get(c).isMarked()) {
        marked.add(c);
      }
    }
    return marked;
  }

  /** Returns coordinates of all cells on the game field. */
  public Collection<Coordinate> getAllCoordinates() {
    return field.getAllCoordinates();
  }

  public int getMaxColumn() {
    return field.getNumberOfColumns();
  }

  public int getMaxRow() {
    return field.getNumberOfRows();
  }

  /**
   * Reveals the cell on the game field.
   *
   * @param coordinate Coordinate of the cell
   * @return the operation status of this method. {@link BeeSweeper.OperationStatus#SUCCESS} if
   *     action was successful. Otherwise, a corresponding error status.
   */
  public BeeSweeper.OperationStatus reveal(Coordinate coordinate) {
    throw new UnsupportedOperationException();
  }

  /**
   * Marks the cell on the game field.
   *
   * @param coordinate Coordinate of the cell
   * @return the operation status of this method. {@link BeeSweeper.OperationStatus#SUCCESS} if
   *     action was successful. Otherwise, a corresponding error status.
   */
  public BeeSweeper.OperationStatus mark(Coordinate coordinate) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unmarks the cell on the game field.
   *
   * @param coordinate coordinate of the cell
   * @return the operation status of this method. {@link BeeSweeper.OperationStatus#SUCCESS} if
   *     action was successful. Otherwise, a corresponding error status.
   */
  public BeeSweeper.OperationStatus unmark(Coordinate coordinate) {
    throw new UnsupportedOperationException();
  }

  public int getFlowersAvailable() {
    return flowersAvailable;
  }

  public boolean isValidCoordinate(Coordinate coordinate) {
    int row = coordinate.getRow();
    int column = coordinate.getColumn();
    return row >= 0 && row < getMaxRow() && column >= 0 && column < getMaxColumn();
  }

  public Cell getCell(Coordinate coordinate) {
    if (isValidCoordinate(coordinate)) {
      return field.get(coordinate);
    } else {
      throw new IllegalArgumentException("Invalid coordinate: " + coordinate);
    }
  }

  public void setGameOver(boolean b) {
    // Set the game over state of the field shape
    field.setGameOver(true);
  }


  public List<Coordinate> getAdjacentCoordinates(Coordinate coordinate) {
    return field.getAdjacentCoordinates(coordinate);

  }

  public boolean isGameWon() {
    for (Coordinate coordinate : getAllCoordinates()) {
      Cell cell = getCell(coordinate);
      if (!cell.isBee() && !cell.isRevealed()) {
        return false; // If any non-bee cell is not revealed, game is not won
      }
    }
    return gameWon; // All non-bee cells are revealed, game is won
  }

  public void setGameWon(boolean gameWon) {
    this.gameWon = gameWon;
  }

  public boolean isGameOver() {
    return gameOver;
  }

}
