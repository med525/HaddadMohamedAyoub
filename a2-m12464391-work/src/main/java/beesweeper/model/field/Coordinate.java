package beesweeper.model.field;

import beesweeper.model.shape.FieldShape;
import java.util.Objects;

/**
 * Coordinate ofa cell on a {@link FieldShape}.
 *
 * <p>Coordinates are comparable with each other, according to the following rule:
 *
 * <ol>
 *   <li>Coordinates in a lower row always come first.
 *   <li>Coordinates with the same row but lower column number come first.
 * </ol>
 */
public final class Coordinate implements Comparable<Coordinate> {
  private final int row;
  private final int column;

  public Coordinate(int row, int column) {
    this.row = row;
    this.column = column;
  }

  /** Creates a new Coordinate. */
  public static Coordinate of(int row, int column) {
    return new Coordinate(row, column);
  }
  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public int compareTo(Coordinate coordinate) {
    int rowDiff = getRow() - coordinate.getRow();
    if (rowDiff != 0) {
      return rowDiff;
    }
    return getColumn() - coordinate.getColumn();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coordinate that = (Coordinate) o;
    return row == that.row &&
            column == that.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }

  @Override
  public String toString() {
    return "Coordinate{" + "row=" + row + ", column=" + column + '}';
  }
}
