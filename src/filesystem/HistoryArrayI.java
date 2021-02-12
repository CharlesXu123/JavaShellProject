package filesystem;

public interface HistoryArrayI {
  /**
   * This method add history to History Array.
   * 
   * @param history
   *          history for adding
   */
  void addHistory(String history);
  /**
   * generate a string representation of the user inputs history.
   * 
   * @return a string representation of History Array
   */
  String checkHistory();
  /**
   * generate a string representation of the last num number of the user inputs
   * history.
   * 
   * @param num
   *          expected number of history, safely assume to be not negative
   * @return a string representation of recent num number of history in History
   *         Array
   */
  String checkHistory(int num);
}
