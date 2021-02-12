package test;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import util.Parser;

/**
 * Test for Parser.
 *
 * @author kanghung
 */
public class ParserTest {

  /**
   * Represents what user typed to JShell.
   */
  private String userInput;

  /**
   * Expected output in ArrayList of str.
   */
  private ArrayList<String> expectedList;

  /**
   * Constant "null" array of string that parser returns when error occurs.
   */
  private static final String[] emptyArr = new String[0];

  /**
   * Constant for "outfile".
   */
  private static final String OUTFILE = "outfile";

  /**
   * Get expectedList as an array.
   *
   * @return String[] array form of expectedList
   */
  private String[] getExpected() {
    return this.expectedList.toArray(new String[expectedList.size()]);
  }

  /**
   * Get an array returned from Parser.
   *
   * @return String[] actual array returned from Parser class
   */
  private String[] getActual() {
    return Parser.parseUserInput(this.userInput);
  }

  /**
   * Setup expectedList.
   */
  @Before
  public void setup() {
    expectedList = new ArrayList<>();
  }

  /**
   * Empty userInput.
   */
  @Test
  public void emptyInput() {
    userInput = "";
    assertArrayEquals(emptyArr, getActual());
  }

  /**
   * Basic usage of an invalid command.
   */
  @Test
  public void invalidCmdUsage() {
    userInput = "invalid arg1 arg2";
    assertArrayEquals(emptyArr, getActual());
  }

  /**
   * Basic Usage of a valid command.
   */
  @Test
  public void basicUsage() {
    userInput = "ls arg1 arg2 arg3";
    expectedList.add("ls");
    expectedList.add("arg1");
    expectedList.add("arg2");
    expectedList.add("arg3");
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Invalid redirection usage.
   */
  @Test
  public void invalidRedirectionUsage1() {
    userInput = "ls >";
    assertArrayEquals(emptyArr, getActual());
  }

  /**
   * Invalid redirection usage 2.
   */
  @Test
  public void invalidRedirectionUsage2() {
    userInput = "ls > outfile >> file";
    assertArrayEquals(emptyArr, getActual());
  }

  /**
   * Invalid redirection usage 3.
   */
  @Test
  public void invalidRedirectionUsage3() {
    userInput = "man man >>> man";
    assertArrayEquals(emptyArr, getActual());
  }

  /**
   * Invalid redirection usage 4.
   */
  @Test
  public void invalidRedirectionUsage4() {
    userInput = "man>>outfile>file";
    assertArrayEquals(emptyArr, getActual());
  }

  /**
   * Trivial case of redirection usage.
   */
  @Test
  public void trivialRedirection() {
    userInput = "ls > outfile";
    expectedList.add("ls");
    expectedList.add(">");
    expectedList.add(OUTFILE);
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Redirection case 1.1: single arrow at the start.
   */
  @Test
  public void singleArrowAtTheStart() {
    userInput = "man >outfile";
    expectedList.add("man");
    expectedList.add(">");
    expectedList.add(OUTFILE);
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Redirection case 1.2: double arrow at the start.
   */
  @Test
  public void doubleArrowAtTheStart() {
    userInput = "man >>outfile";
    expectedList.add("man");
    expectedList.add(">>");
    expectedList.add(OUTFILE);
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Redirection case 2.1: single arrow at the end.
   */
  @Test
  public void singleArrowAtTheEnd() {
    userInput = "man> outfile";
    expectedList.add("man");
    expectedList.add(">");
    expectedList.add(OUTFILE);
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Redirection case 2.2: double arrow at the end.
   */
  @Test
  public void doubleArrowAtTheEnd() {
    userInput = "echo STR>> outfile";
    expectedList.add("echo");
    expectedList.add("STR");
    expectedList.add(">>");
    expectedList.add(OUTFILE);
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Redirection case 3.1 single arrow in between.
   */
  @Test
  public void singleInBet() {
    userInput = "echo STR>outfile";
    expectedList.add("echo");
    expectedList.add("STR");
    expectedList.add(">");
    expectedList.add(OUTFILE);
    for (String each : getActual()) {
      System.out.println(each);
    }
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Redirection case 3.2 double arrow in between.
   */
  @Test
  public void doubleInBet() {
    userInput = "echo STR>>outfile";
    expectedList.add("echo");
    expectedList.add("STR");
    expectedList.add(">>");
    expectedList.add(OUTFILE);
    assertArrayEquals(getExpected(), getActual());
  }

  /**
   * Quotes usage for search command.
   */
  @Test
  public void quotesForSearch() {
    userInput = "search /a/b -type d -name \"xyz\"";
    expectedList.add("search");
    expectedList.add("/a/b");
    expectedList.add("-type");
    expectedList.add("d");
    expectedList.add("-name");
    expectedList.add("\"xyz\"");
    assertArrayEquals(getExpected(), getActual());
  }

}
