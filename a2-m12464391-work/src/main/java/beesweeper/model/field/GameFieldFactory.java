package beesweeper.model.field;

import beesweeper.model.shape.*;

import java.util.Collection;

/** Factory for {@link GameField}. */
public class GameFieldFactory {
  private  int rows;
  private  int columns;
  private final ShapeFactory shapeFactory;
  private final CoordinateGenerator beeCoordinateGenerator;

  public GameFieldFactory(ShapeFactory shapeFactory, CoordinateGenerator beeCoordinateGenerator) {
    this.shapeFactory = shapeFactory;
    this.beeCoordinateGenerator = beeCoordinateGenerator;
  }


  public void setDimensions(int rows, int columns) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Columns and rows must be positive integers");
    }
    this.rows = rows;
    this.columns = columns;
  }
  /**
   * Creates a new {@link GameField} object from this factory's configuration. The game field will
   * have the given number of bees hidden, and the given number of flowers available for marking
   * cells.
   *
   * <p>The location of hidden bees is determined by the {@link CoordinateGenerator} given in the
   * constructor of this class.
   *
   * @param numBees number of bees to hide on the game field.
   * @param numFlowers number of flowers that should be initially available to the player.
   * @return a new game field instance with the configured parameters.
   */
  public GameField create(int numBees, int numFlowers) {
    if (rows <= 0 || columns <= 0 || numBees <= 0) {
      throw new IllegalArgumentException("Columns, rows, and numBees must be positive integers");
    }

    // Create the initial shape with specified dimensions
    FieldShape initialShape = FieldShape.create(rows, columns);

    // Add bees to the shape
    addBeesToShape(initialShape, numBees, beeCoordinateGenerator);

    // Add numbers of surrounding bees to the shape
    addNumbersOfSurroundingBeesToShape(initialShape);

    // Return a new GameField instance with the configured parameters
    return new GameField(initialShape, numFlowers);
  }


  // Recommendation, may be changed if desired.
  // Add the number of bees to the given shape, using the beeCoordinateGenerator.
  private void addBeesToShape(
          FieldShape emptyShape, int numBees, CoordinateGenerator beeCoordinateGenerator) {
    Collection<Coordinate> allCoordinates = emptyShape.getAllCoordinates();
    if (allCoordinates.size() < numBees) {
      throw new IllegalArgumentException("Can't generate valid game field with number of bees >= number of field cells: "
              + allCoordinates.size() + " vs. " + numBees + " bees");
    }

    Collection<Coordinate> beeCoordinates = beeCoordinateGenerator.getCoordinates(numBees, emptyShape);
    for (Coordinate coordinate : beeCoordinates) {
      emptyShape.get(coordinate).setBee(true);
    }
  }

  /**
   * Convert the initial board into a concrete game board. The number of bees around an empty cell
   * is now counted.
   */
  private void addNumbersOfSurroundingBeesToShape(FieldShape field) {
    for (Coordinate coordinate : field.getAllCoordinates()) {
      Cell cell = field.get(coordinate);
      if (!cell.isBee()) {
        int surroundingBees = countSurroundingBees(field, coordinate);
        cell.setNumberOfBeesSurrounding(surroundingBees);
      }
    }
  }

  private int countSurroundingBees(FieldShape field, Coordinate coordinate) {
    int count = 0;
    for (Coordinate neighbor : field.getAdjacentCoordinates(coordinate)) {
      if (field.contains(neighbor) && field.get(neighbor).isBee()) {
        count++;
      }
    }
    return count;
  }

  public static GameField createRectangularGameField(int columns, int rows, int numBees) {
    if (columns <= 0 || rows <= 0 || numBees <= 0) {
      throw new IllegalArgumentException("Columns, rows, and numBees must be positive integers");
    }

    // Create a ShapeFactory for rectangular shapes
    ShapeFactory shapeFactory = new RectangularShapeFactory(columns, rows);

    // Create a CoordinateGenerator for placing bees
    CoordinateGenerator beeCoordinateGenerator = new RandomCoordinateGenerator();

    // Create a new GameFieldFactory with the ShapeFactory and CoordinateGenerator
    GameFieldFactory factory = new GameFieldFactory(shapeFactory, beeCoordinateGenerator);

    // Set the dimensions of the game field
    factory.setDimensions(rows, columns);

    // Use the factory to create a new GameField
    return factory.create(numBees, numBees); // Assuming 0 flowers initially available
  }

  public static GameField createCombGameField(int rows, int numBees) {
    throw new UnsupportedOperationException("Method not implemented yet");
  }

  /**
   * Creates a new comb-shaped game field with the specified number of rows and bees.
   *
   * @param rows the number of rows in the game field
   * @param numBees the number of bees to hide in the game field
   * @return a new comb-shaped game field
   */


}
