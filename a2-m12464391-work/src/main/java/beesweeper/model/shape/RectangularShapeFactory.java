package beesweeper.model.shape;
import beesweeper.model.shape.FieldShape;

/** {@link ShapeFactory} for a rectangular game field. */
public class RectangularShapeFactory implements ShapeFactory {

  private int columns;
  private int rows;

  /**
   * Creates a new RectangularShapeFactory. The created factory will create {@link FieldShape}s of
   * rectangles with the given columns and rows.
   *
   * @param columns intended columns of the rectangle (width).
   * @param rows intended rows of the rectangle (height).
   * @throws IllegalArgumentException if an invalid width or height is given
   */
  public RectangularShapeFactory(int columns, int rows) {
    if (columns <= 0 || rows <= 0) {
      throw new IllegalArgumentException("Columns and rows must be positive integers");
    }
    this.columns = columns;
    this.rows = rows;
  }

 /* @Override
  public FieldShape create() {
    return null;
  }*/

  @Override
  public FieldShape create() {
    int rows = this.rows; // Assuming 'rows' is a property of the GameFieldFactory
    int columns = this.columns; // Assuming 'columns' is a property of the GameFieldFactory
    return new FieldShape();
  }
}
