package test;

import filesystem.HistoryArrayI;

public class MockHistoryArray implements HistoryArrayI {

  /**
   * Mock the history list.
   */
  private int historyLs;

  /**
   * Constructor for MockHistoryArray.
   */
  public MockHistoryArray() {
    historyLs = 0;
  }

  /**
   * Add a history to MockHistoryArray.
   * 
   * @param history
   *          history to be added
   */
  @Override
  public void addHistory(String history) {
    historyLs = historyLs + 1;
  }

  /**
   * This method return a string representation of MockHistoryArray.
   * 
   * @return a string representation of MockHistoryArray
   */
  @Override
  public String checkHistory() {
    if (historyLs == 0) {
      return "";
    } else {
      return (Integer.toString(historyLs) + " recent history");
    }
  }

  /**
   * Generate a string representation of the last num number of the user inputs
   * history.
   * 
   * @param num
   *          expected number of history, safely assume to be not negative
   * @return a string representation of recent num number of history in
   *         MockHistory Array
   */
  @Override
  public String checkHistory(int num) {
    if (historyLs == 0) {
      return "";
    } else if (num > historyLs) {
      return (Integer.toString(historyLs) + " recent history");
    } else {
      return (Integer.toString(num) + " recent history");
    }
  }

}
