package beesweeper.model.shape;

import beesweeper.model.field.Cell;
import beesweeper.model.field.Coordinate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A field of a given shape. Consists of rows and columns. Each row can have an arbitrary amount of
 * columns.
 */
public class FieldShape {


  private int rows;
  private int columns;

  /** Returns the maximum row amount of any column in the shape. */
  public int getNumberOfRows() {
    return rows;
  }

  /** Returns the maximum column amount of any row in the shape. */
  public int getNumberOfColumns() {
    return columns;
  }

  /**
   * Returns the coordinates of all cells in the shape. Coordinates may not be sorted.
   *
   * @return the coordinates of all cells in this shape
   */
  public List<Coordinate> getAllCoordinates() {
    List<Coordinate> coordinates = new ArrayList<>();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        coordinates.add(Coordinate.of(row, col));
      }
    }

    return coordinates;
  }


  /**
   * Returns whether the given coordinate exists in this shape.
   *
   * @param coordinate coordinate to check
   * @return <code>true</code> if the coordinate exists, <code>false</code> otherwise
   */
  public boolean contains(Coordinate coordinate) {
    int row = coordinate.getRow();
    int col = coordinate.getColumn();
    return row >= 0 && row < rows && col >= 0 && col < columns;
  }

  /**
   * Replaces the cell at the given coordinate in the shape with the given cell.
   *
   * @param coordinate coordinate to replace cell at
   * @param newCell new cell to replace old cell with
   * @throws IndexOutOfBoundsException if the given coordinate is not contained in this shape
   * @see #contains(Coordinate)
   */
  public void replace(Coordinate coordinate, Cell newCell) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the cell at the given coordinate in this shape.
   *
   * @param cellCoordinate coordinate to return cell for
   * @return the cell at the given coordinate in this shape
   * @throws IndexOutOfBoundsException if the coordinate is not in this shape
   */

  public Cell get(Coordinate cellCoordinate) {
    if (!contains(cellCoordinate)) {
      throw new IndexOutOfBoundsException("Coordinate is not in this shape");
    }
    // Implement logic to retrieve the cell at the given coordinate
    // Assuming the implementation of the Cell class is available
    return new Cell(false, 0); // Replace with actual logic
  }


  public List<Coordinate> getAdjacentCoordinates(Coordinate coordinate) {
    int row = coordinate.getRow();
    int col = coordinate.getColumn();
    List<Coordinate> adjacentCoordinates = new ArrayList<>();

    // Add coordinates of adjacent cells
    for (int i = row - 1; i <= row + 1; i++) {
      for (int j = col - 1; j <= col + 1; j++) {
        Coordinate adjacentCoordinate = Coordinate.of(i, j);
        if (!adjacentCoordinate.equals(coordinate) && contains(adjacentCoordinate)) {
          adjacentCoordinates.add(adjacentCoordinate);
        }
      }
    }

    return adjacentCoordinates;
  }

  public void setGameOver(boolean gameOver) {
    // Implementation to set the game over state based on the boolean parameter
  }

  public static FieldShape create(int rows, int columns) {
    FieldShape fieldShape = new FieldShape();
    fieldShape.rows = rows;
    fieldShape.columns = columns;
    return fieldShape;
  }


}
