package beesweeper.model.shape;

/**
 * {@link ShapeFactory} for a honeycombed game field. For n (an odd number >= 3) rows, the created
 * game field has the shape of a regular hexagon with height = rows.
 */
public class HoneyCombedShapeFactory implements ShapeFactory {

  private final int numRows;

  /**
   * Creates a new HoneyCombedShapeFactory. The created factory will create {@link FieldShape}s of a
   * regular hexagon of the given height.
   *
   * @param numRows intended height of the hexagon. Must be an odd number >= 3.
   * @throws IllegalArgumentException if an invalid height is given
   */
  public HoneyCombedShapeFactory(int numRows) {
    if (numRows < 3 || numRows % 2 == 0) {
      throw new IllegalArgumentException(
          "Honeycomb shape can not be generated with this number of rows");
    }
    this.numRows = numRows;
  }

  @Override
  public FieldShape create() {
    throw new UnsupportedOperationException();
  }
}
