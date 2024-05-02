package beesweeper.model;

import beesweeper.model.field.GameField;

/**
 * Full state of a {@link BeeSweeper} game. Consists of the current state of the game field and the
 * current game status (running, lost, won).
 */
public final class GameState {

  /** Status of the BeeSweeper game. */
  public enum GameStatus {
    /** The game is still running. */
    ALIVE,
    /** The game is over and lost. */
    LOSE,
    /** The game is over and won. */
    WIN
  }

  private final GameField gameField;
  private final GameStatus gameStatus;

  private GameState(GameField gameField, GameStatus gameStatus) {
    this.gameField = gameField;
    this.gameStatus = gameStatus;
  }

  /** Initialize a fresh new GameState with the given GameField. */
  static GameState create(GameField gameField) {
    return new GameState(gameField, GameStatus.ALIVE);
  }

  /**
   * Create a new instance of GameState where GameStatus is as given, other members are as in this
   * instance.
   */
  GameState with(GameStatus status) {
    return new GameState(gameField, status);
  }

  /** Return the game field. */
  public GameField getField() {
    return gameField;
  }

  /** Return whether the game is still running. */
  public boolean isGameAlive() {
    return gameStatus == GameStatus.ALIVE;
  }

  /** Return whether the game is won. */
  public boolean isGameWon() {
    return gameStatus == GameStatus.WIN;
  }

  /** Return whether the game is over (lose). */
  public boolean isGameOver() {
    return gameStatus == GameStatus.LOSE;
  }
}
