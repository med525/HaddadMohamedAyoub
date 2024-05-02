package beesweeper.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Tests for erronous calls to {@link BeeSweeper}. */
public class BeeSweeperRectangularShapeErrorsTest {

  @Test
  public void testRectGameNegativeRows() {
    try {
      BeeSweeper.newRectangularGame(5, -2, 10);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testRectGameNoRows() {
    try {
      BeeSweeper.newRectangularGame(5, 0, 10);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testRectGameNegativeColumns() {
    try {
      BeeSweeper.newRectangularGame(-3, 3, 2);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testRectGameNoColumns() {
    try {
      BeeSweeper.newRectangularGame(0, 3, 2);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testRectGameNegativeBees() {
    try {
      BeeSweeper.newRectangularGame(10, 11, -6);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testRectGameNoBees() {
    try {
      BeeSweeper.newRectangularGame(10, 11, 0);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testRectGameTooManyBees() {
    try {
      BeeSweeper.newRectangularGame(9, 7, 64);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }
}
