package beesweeper;

import beesweeper.model.BeeSweeper;
import beesweeper.model.GameState;
import beesweeper.model.field.Cell;
import beesweeper.model.field.Coordinate;
import beesweeper.model.field.GameField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/** Command-line interface for {@link BeeSweeper} game. */
public class Shell {
  private static final String PROMPT = "MW > ";

  private static final String PRINT_COMMAND = "PRINT";
  private static final String NEWREC_COMMAND = "NEWREC";
  private static final String NEWCOMB_COMMAND = "NEWCOMB";
  private static final String REVEAL_COMMAND = "REVEAL";
  private static final String MARK_COMMAND = "MARK";
  private static final String UNMARK_COMMAND = "UNMARK";
  private static final String QUIT_COMMAND = "QUIT";

  private static final String INVALID_COMMAND_MESSAGE = "Invalid command.";
  private static final String INVALID_ARGUMENTS_MESSAGE = "Invalid arguments.";
  private static final String INVALID_INPUT_MESSAGE = "Invalid input.";
  private static final String INVALID_COMB_DIMENSION_MESSAGE =
      "Honeycomb with these dimensions not possible.";
  private static final String INVALID_COMB_BEE_MESSAGE = "Not enough fields for bees.";
  private static final String NO_ACTIVE_GAME_MESSAGE = "No active game running.";
  private static final String NOT_POSSIBLE_MESSAGE = "Not possible.";

  // maximum dimension of the game board
  private static final int MAX_RECT_WIDTH = 26;
  private static final int MAX_RECT_HEIGHT = 99;
  private static final int MAX_COMB_HEIGHT = 17;
  private static final char COLUMN_START_CHAR = 'A';
  private static final int ROW_DISPLAY_OFFSET = 1;

  private BeeSweeper game;

  /**
   * Read and process input until the quit command has been entered.
   *
   * @param args Command line arguments.
   * @throws IOException Error reading from stdin.
   */
  public static void main(String[] args) throws IOException {
    final Shell shell = new Shell();
    shell.run();
  }

  /**
   * Run the beesweeper shell. Shows prompt 'MW> ', takes commands from the user and executes them.
   */
  public void run() throws IOException {
    BufferedReader in =
        new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    boolean quit = false;

    while (!quit) {
      System.out.print(PROMPT);

      String input = in.readLine();
      if (input == null) {
        break;
      }

      // remove all empty tokens
      String[] tokens =
          Arrays.stream(input.split("\\s+")).filter(e -> e.length() > 0).toArray(String[]::new);

      if (tokens.length == 0) {
        displayError("No Command given");
        continue;
      }

      String command = tokens[0].toUpperCase();
      String[] arguments = Arrays.copyOfRange(tokens, 1, tokens.length);

      // Consider using enums instead (Effective Java Item 34)
      switch (command) {
        case PRINT_COMMAND:
          handlePrintCommand(arguments);
          break;
        case NEWREC_COMMAND:
          handleNewRectangleCommand(arguments);
          break;
        case NEWCOMB_COMMAND:
          handleNewCombCommand(arguments);
          break;
        case REVEAL_COMMAND:
          handleRevealCommand(arguments);
          break;
        case MARK_COMMAND:
          handleMarkCommand(arguments);
          break;
        case UNMARK_COMMAND:
          handleUnmarkCommand(arguments);
          break;
        case QUIT_COMMAND:
          if (arguments.length != 0) {
            displayError(INVALID_ARGUMENTS_MESSAGE);
            continue;
          }
          quit = true;
          break;
        default:
          displayError(INVALID_COMMAND_MESSAGE);
      }
    }
  }

  private void printActionResult(BeeSweeper.OperationStatus operationStatus) {
    GameState gameState = game.getGameState();
    if (operationStatus == BeeSweeper.OperationStatus.SUCCESS) {
      printCurrentGameField();
      if (gameState.isGameWon()) {
        System.out.println("Congratulations, you win!");
        game = null;
      } else if (gameState.isGameOver()) {
        System.out.println("Sorry, you loose.");
        game = null;
      }
    } else if (operationStatus == BeeSweeper.OperationStatus.INDEX_OOB) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
    } else { // operationStatus == GameState.OperationStatus.FAIL
      assert operationStatus == BeeSweeper.OperationStatus.FAIL;
      displayError(NOT_POSSIBLE_MESSAGE);
    }
  }

  private void handlePrintCommand(String[] arguments) {
    if (arguments.length != 0) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    if (!isGameRunning()) {
      displayError(NO_ACTIVE_GAME_MESSAGE);
      return;
    }

    printCurrentGameField();
  }

  private void handleNewRectangleCommand(String[] arguments) {
    if (arguments.length != 3) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    int columns = Integer.parseInt(arguments[0]);
    int rows = Integer.parseInt(arguments[1]);
    int bees = Integer.parseInt(arguments[2]);

    if (columns < 0 || rows < 0 || bees < 0) {
      displayError(INVALID_INPUT_MESSAGE);
      return;
    }

    if (columns * rows <= bees) {
      displayError(INVALID_INPUT_MESSAGE);
      return;
    }

    if (columns > MAX_RECT_WIDTH || rows > MAX_RECT_HEIGHT) {
      displayError(INVALID_INPUT_MESSAGE);
      return;
    }

    game = BeeSweeper.newRectangularGame(columns, rows, bees);
    printCurrentGameField();
  }

  private void handleNewCombCommand(String[] arguments) {
    if (arguments.length != 2) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    int rows = Integer.parseInt(arguments[0]);
    int bees = Integer.parseInt(arguments[1]);

    if (rows < 3 || rows % 2 == 0 || rows > MAX_COMB_HEIGHT) {
      displayError(INVALID_COMB_DIMENSION_MESSAGE);
      return;
    }

    int maxBees = rows * rows - (rows - 1) / 2;
    if (bees > maxBees) {
      displayError(INVALID_COMB_BEE_MESSAGE);
      return;
    }

    game = BeeSweeper.newCombGame(rows, bees);
    printCurrentGameField();
  }

  private void handleRevealCommand(String[] arguments) {
    if (!isGameRunning()) {
      displayError(NO_ACTIVE_GAME_MESSAGE);
      return;
    }

    if (arguments.length != 2) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    if (arguments[0].length() != 1) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    int col = arguments[0].charAt(0) - COLUMN_START_CHAR;
    int row = Integer.parseInt(arguments[1]) - ROW_DISPLAY_OFFSET;

    BeeSweeper.OperationStatus status = game.reveal(Coordinate.of(row, col));
    printActionResult(status);
  }

  private void handleMarkCommand(String[] arguments) {
    if (!isGameRunning()) {
      displayError(NO_ACTIVE_GAME_MESSAGE);
      return;
    }

    if (arguments.length != 2) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    if (arguments[0].length() != 1) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    int col = arguments[0].charAt(0) - COLUMN_START_CHAR;
    int row = Integer.parseInt(arguments[1]) - 1;

    BeeSweeper.OperationStatus status = game.mark(Coordinate.of(row, col));
    printActionResult(status);
  }

  private void handleUnmarkCommand(String[] arguments) {
    if (!isGameRunning()) {
      displayError(NO_ACTIVE_GAME_MESSAGE);
      return;
    }

    if (arguments.length != 2) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    if (arguments[0].length() != 1) {
      displayError(INVALID_ARGUMENTS_MESSAGE);
      return;
    }

    int col = arguments[0].charAt(0) - COLUMN_START_CHAR;
    int row = Integer.parseInt(arguments[1]) - 1;

    BeeSweeper.OperationStatus operationStatus = game.unmark(Coordinate.of(row, col));
    printActionResult(operationStatus);
  }

  void printCurrentGameField() {
    assert isGameRunning();

    GameField field = game.getGameState().getField();
    int numColumns = field.getMaxColumn();

    // print column index (A~Z)
    StringBuilder line = new StringBuilder("   ");
    for (int colNum = 0; colNum < numColumns; ++colNum) {
      line.append(" ").append((char) (colNum + COLUMN_START_CHAR));
    }
    System.out.println(line);

    // print each row with row index
    for (int rowNum = 0; rowNum < field.getMaxRow(); ++rowNum) {
      String numberPadding = "";
      int rowNumToDisplay = rowNum + ROW_DISPLAY_OFFSET;
      if (rowNumToDisplay < 10) {
        numberPadding += " ";
      }
      if (rowNumToDisplay < 100) {
        numberPadding += " ";
      }
      line = new StringBuilder(numberPadding + rowNumToDisplay);
      for (int colNum = 0; colNum < numColumns; ++colNum) {
        line.append(" ");
        Coordinate coord = Coordinate.of(rowNum, colNum);
        if (field.contains(coord)) {
          Cell currCell = field.get(coord);
          line.append(cellToString(currCell));
        } else {
          line.append(" ");
        }
      }
      System.out.println(line);
    }

    // print remaining flowers
    System.out.println("Flowers left: " + field.getFlowersAvailable());
  }

  private String cellToString(Cell cell) {
    if (cell.isRevealed()) {
      if (cell.isBee()) {
        return "B";
      } else {
        return String.valueOf(cell.getNumberOfBeesSurrounding());
      }
    } else if (cell.isMarked()) {
      return "F";
    } else {
      return "*";
    }
  }

  private void displayError(String message) {
    System.out.println("Error! " + message);
  }

  private boolean isGameRunning() {
    return game != null && game.getGameState().isGameAlive();
  }
}
