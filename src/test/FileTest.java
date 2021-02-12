package test;

import static org.junit.Assert.assertEquals;

import filesystem.File;
import org.junit.Before;
import org.junit.Test;


public class FileTest {

  File file;
  String test;

  /**
   * set up.
   */
  @Before
  public void setUp() {
    file = new File("fileName", null);
    test = "";
  }

  // set empty string
  @Test
  public void setContentTest1() {
    file.setContent("");
    assertEquals(test, file.getContent());
  }

  // set non-empty string
  @Test
  public void setContentTest2() {
    file.setContent("I am good");
    test = "I am good";
    assertEquals(test, file.getContent());
  }

  // empty content append non-empty string
  @Test
  public void appendContentTest1() {
    file.appendContent("I am good");
    test = "I am good";
    assertEquals(test, file.getContent());
  }

  // non-empty content append empty string
  @Test
  public void appendContentTest2() {
    file.setContent("I am good");
    file.appendContent("");
    test = "I am good";
    assertEquals(test, file.getContent());
  }

  // non-empty content append non-empty string
  @Test
  public void appendContentTest3() {
    file.setContent("I am good");
    file.appendContent(" How are you?");
    test = "I am good How are you?";
    assertEquals(test, file.getContent());
  }

  // Clear content
  @Test
  public void clearContentTest() {
    file.setContent("I am good");
    file.clearContent();
    test = "";
    assertEquals(test, file.getContent());
  }

}
