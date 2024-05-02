package beesweeper.model.shape;

import beesweeper.model.field.Coordinate;
import java.util.Collection;

/**
 * Classes implementing this interface support the sampling of distinct coordinates from a given
 * {@link FieldShape}.
 */
public interface CoordinateGenerator {

  /**
   * Returns coordinates for the given {@link FieldShape}. All returned coordinates are distinct.
   *
   * @param n number of coordinates to create
   * @param shape shape to create coordinates for.
   * @return collection of n distinct coordinates
   * @throws IllegalArgumentException if the given shape does not contain n distinct coordinates to
   *     sample from
   */
  Collection<Coordinate> getCoordinates(int n, FieldShape shape);
}
