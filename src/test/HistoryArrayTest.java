package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import filesystem.HistoryArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HistoryArrayTest {

  HistoryArray ha;

  @Before
  public void setup() {
    ha = HistoryArray.createHistoryArray();
  }

  /**
   * Tear down fileSystem instance. From Piazza post @579.
   */
  @After
  public void tearDown() throws Exception {
    Field f = (ha.getClass()).getDeclaredField("ha");
    f.setAccessible(true);
    f.set(null, null);
  }

  /*
   * test checkHistory when historyArray is empty expected to return empty
   * String
   */
  @Test
  public void testCheckHistory1() {
    String output1 = ha.checkHistory();
    String output2 = ha.checkHistory(10);
    assertEquals("", output1);
    assertEquals("", output2);
  }

  /**
   * test checkHistory with HistoryArray contains history. expected to return
   * given number of history in HistoryArray
   */
  @Test
  public void testCheckHistory2() {
    ha.addHistory("history1");
    ha.addHistory("history2");
    ha.addHistory("history3");
    String output = ha.checkHistory();
    String outputLess = ha.checkHistory(2);
    String outputMore = ha.checkHistory(10);
    assertEquals("1. history1\n2. history2\n3. history3", output);
    assertEquals("2. history2\n3. history3", outputLess);
    assertEquals("1. history1\n2. history2\n3. history3", outputMore);
  }

}
