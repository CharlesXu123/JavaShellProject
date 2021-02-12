package commands;

import filesystem.HistoryArray;
import filesystem.HistoryArrayI;

/**
 * This class handles History command.
 *
 * @author xushengsong
 */
public class History extends UnitCommand {
  HistoryArrayI ha;
  
  public History() {
    ha = HistoryArray.createHistoryArray();
  }
  
  public History(HistoryArrayI ha) {
    this.ha = ha;
  }

  /**
   * Return the documentation of history command.
   *
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for History (history) >";
    doc += "\nUsage: history [number]";
    doc += "\nPrints recent user inputs.";
    doc += "\nThe output can be truncated by providing a number.";
    doc += "\nThis will only print the last [number] of most recent user inputs.";
    return doc;
  }

  /**
   * Check if the content of the given arg is a valid integer.
   *
   * @param arg
   *          argument for checking
   * @return true if the content of arg is a valid integer, return false
   *         otherwise
   */
  private static boolean checkValidInt(String arg) {
    try {
      int d = Integer.parseInt(arg);
      if (d < 0) {
        return false;
      }
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * This method help execute command: history [NUM].
   * 
   * @param args
   *          input arguments
   * @return String representation of history array if args is valid, an error
   *         message otherwise
   */
  public String executeHistory(String[] args) {
    if (args.length == 0) {
      String output = ha.checkHistory();
      return output;
    } else if (args.length != 1 || !checkValidInt(args[0])) {
      return ("\"invalid Input");
    } else {
      String output = ha.checkHistory(Integer.parseInt(args[0]));
      return output;
    }
  }

  /**
   * Execute the command: history [number].
   *
   * @param args
   *          user inputs
   */
  @Override
  public void executeCommand(String[] args) {
    String msg = executeHistory(args);
    if (msg.startsWith("\"")) {
      sendErrMsg(msg.substring(1));
    } else {
      sendOutput(msg);
    }
  }
}
