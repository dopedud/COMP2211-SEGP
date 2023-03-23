package uk.ac.soton.comp2211.team33.exceptions;

/**
 * The OutOfRangeException exception class that throws an exception when a value is out of its supposed range.
 */
public class OutOfRangeException extends Exception {

  /**
   * Class constructor.
   *
   * @param message message to show when exception is thrown
   */
  public OutOfRangeException(String message) {
    super(message);
  }
}
