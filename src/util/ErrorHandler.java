package util;

/**
 * ErrorHandler class handles errors and provides appropriate error messages to user.
 * 
 * @author tanluowe
 *
 */

public class ErrorHandler {

  /**
   * Prefix of error message.
   */
  private static String prefix = "ERROR: ";

  /**
   * Print the error message.
   * 
   * @param errorMessage error message to print
   */
  public static void getErrorMessage(String errorMessage) {
    System.out.println(prefix + errorMessage);
  }

  public static String getErrorMessageForTesting(String errorMessage) {
    return prefix + errorMessage;
  }
}
