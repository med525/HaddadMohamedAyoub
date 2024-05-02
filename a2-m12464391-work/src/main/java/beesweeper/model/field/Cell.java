package beesweeper.model.field;

/**
 * Cell on a game field. Each cell can contain a bee or be empty. If a cell is empty, it has a
 * number of bees surrounding it on the game field. In addition, each cell can be revealed or
 * unrevealed (cloaked), and marked or unmarked.
 */
public final class Cell {

  private int numberOfBeesSurrounding;
  private boolean isBee;
  private boolean isRevealed;
  private final boolean isMarked;

  public Cell(boolean isBee, int numberOfBeesSurrounding) {
    this.isBee = isBee;
    this.numberOfBeesSurrounding = numberOfBeesSurrounding;
    this.isRevealed = false;
    this.isMarked = false;
  }



    /** Get the number of bees surrounding this cell. */
  public int getNumberOfBeesSurrounding() {
    // TODO!
    return numberOfBeesSurrounding;
  }

  public void setBee(boolean isBee) {
    this.isBee = isBee;
  }

  public void setNumberOfBeesSurrounding(int numberOfBeesSurrounding) {
    this.numberOfBeesSurrounding = numberOfBeesSurrounding;
  }


  public boolean isBee() {
    // TODO!
    return isBee;
  }

  public boolean isRevealed() {
    // TODO!
    return isRevealed;
  }

  public boolean isMarked() {
    // TODO!
    return isMarked;
  }

  public void setRevealed(boolean isRevealed) {
    this.isRevealed = isRevealed;
  }

}
