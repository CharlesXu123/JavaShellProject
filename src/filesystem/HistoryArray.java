package filesystem;

import java.util.ArrayList;

/**
 * @author xushengsong This class stores and manages all the user inputs
 */
public class HistoryArray implements HistoryArrayI {
  /**
   * location for HistoryArray instance
   */
  private static HistoryArray ha = null;
  /**
   * array list for storing the user inputs
   */
  private ArrayList<String> historyLs = new ArrayList<String>();

  /**
   * ensure exactly one HistoryArray instance exist
   * 
   * @return returning a new HistoryArray instance if ha is null, else returning
   *         ha
   */
  public static HistoryArray createHistoryArray() {
    if (ha == null) {
      ha = new HistoryArray();
    }
    return ha;
  }

  /**
   * This is a getter for historyLs
   * 
   * @return history array
   */
  public ArrayList<String> getHistoryLs() {
    return this.historyLs;
  }

  /**
   * This method clean historyLs
   */
  public void cleanHistoryArray() {
    historyLs.clear();
  }

  /**
   * adds history to historyLs
   * 
   * @param history,
   *          user input to be added
   */
  @Override
  public void addHistory(String history) {
    this.historyLs.add(history);
  }

  /**
   * generate a string that contains the last num number of user inputs in
   * historyLs
   * 
   * @param num,
   *          expected number of user inputs from historyLs
   * @param historyLs,
   *          stored inputs
   * @return a string representation of the last num number of user inputs in
   *         historyLs
   */
  private static String makeHistory(int num, ArrayList<String> historyLs) {
    if (historyLs == null) {
      return "";
    }
    String history = "";
    int j = historyLs.size() - num;
    if (j < 0) {
      j = 0;
    }
    for (int i = j; i < historyLs.size(); i++) {
      if (history == "") {
        history = String.valueOf(i + 1) + ". " + historyLs.get(i);
      } else {
        history = history + "\n" + String.valueOf(i + 1) + ". "
            + historyLs.get(i);
      }
    }
    return history;
  }

  /**
   * generate a string representation of the user inputs history
   * 
   * @return an string representation of the user inputs history
   */
  @Override
  public String checkHistory() {
    String history = makeHistory(this.historyLs.size(), this.historyLs);
    return history;
  }

  /**
   * generate a string representation of the last num number of the user inputs
   * history
   * 
   * @param num,
   *          expected number of history, safely assume to be not negative
   * @return an string representation of the last num number of the user inputs
   *         history
   */
  @Override
  public String checkHistory(int num) {
    String history = makeHistory(num, this.historyLs);
    return history;
  }

  /*
   * public static void mainTest(String[] args) { HistoryArray ha =
   * createHistoryArray(); ha.addHistory("history1"); ha.addHistory("history2");
   * ha.addHistory("history3"); ha.addHistory("history4");
   * ha.addHistory("history5"); ha.addHistory("history6"); String hs =
   * ha.checkHistory(); System.out.print(hs); }
   * 
   * public static HistoryArray getHa() { return ha; }
   */
}
