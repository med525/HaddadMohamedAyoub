package beesweeper.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeeSweeperCombedShapeErrorsTest {
  @Test
  public void testCombGameEvenRows() {
    try {
      BeeSweeper.newCombGame(4, 2);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testCombGameNoRows() {
    try {
      BeeSweeper.newCombGame(0, 2);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testCombGameNegativeRows() {
    try {
      BeeSweeper.newCombGame(-1, 2);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testCombGameNegativeBees() {
    try {
      BeeSweeper.newCombGame(5, -1);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testCombGameNoBees() {
    try {
      BeeSweeper.newCombGame(5, 0);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }

  @Test
  public void testCombGameTooManyBees() {
    try {
      BeeSweeper.newCombGame(7, 47);
      Assertions.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected: test successful
    }
  }
}
