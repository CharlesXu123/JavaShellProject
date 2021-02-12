package test;

import static org.junit.Assert.*;

import commands.History;
import org.junit.Before;
import org.junit.Test;

public class HistoryTest {
  MockHistoryArray ha;
  History hs;

  @Before
  public void setup() {
    ha = new MockHistoryArray();
    hs = new History(ha);
  }

  /**
   * test executeHistory when historyArray is empty. expected to return an empty
   * string
   */
  @Test
  public void testexecuteHistory1() {
    String[] args = {};
    String[] argsWithNumber = {"10"};
    String output = hs.executeHistory(args);
    String outputWithNumber = hs.executeHistory(argsWithNumber);
    assertEquals("", output);
    assertEquals("", outputWithNumber);
  }

  /**
   * test executeHistory when input is invalid. expected to return an "\"invalid
   * Input"
   */
  @Test
  public void testexecuteHistory2() {
    String[] args1 = {"invalid input"};
    String[] args2 = {"-1"};
    String output1 = hs.executeHistory(args1);
    String output2 = hs.executeHistory(args2);
    assertEquals("\"invalid Input", output1);
    assertEquals("\"invalid Input", output2);
  }

  /**
   * test executeHistory with HistoryArray contains history. expected to return
   * given number of history in HistoryArray
   */
  @Test
  public void testexecuteHistory3() {
    ha.addHistory("history1");
    ha.addHistory("history2");
    ha.addHistory("history3");
    String[] args = {};
    String[] argsLess = {"2"};
    String[] argsMore = {"100"};
    String output = hs.executeHistory(args);
    String outputLess = hs.executeHistory(argsLess);
    String outputMore = hs.executeHistory(argsMore);
    assertEquals("3 recent history", output);
    assertEquals("2 recent history", outputLess);
    assertEquals("3 recent history", outputMore);
  }

}
