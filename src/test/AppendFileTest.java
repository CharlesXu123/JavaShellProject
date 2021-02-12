package test;

import static org.junit.Assert.*;

import commands.AppendFile;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class AppendFileTest {

  AppendFile af;
  FileSystem fs;

  @Before
  public void setup() {
    af = new AppendFile();
    fs = FileSystem.createFileSystem();
  }
  
  /**
   * Tear down fileSystem instance. From Piazza post @579.
   */
  @After
  public void tearDown() throws Exception {
    Field f = (fs.getClass()).getDeclaredField("fs");
    f.setAccessible(true);
    f.set(null, null);
  }

  /*
   * test checkValidStr with invalid string expected to return false
   */
  @Test
  public void testcheckValidStr1() {
    String invalidInput = "invalid input";
    Boolean result = AppendFile.checkValidStr(invalidInput);
    assertEquals(false, result);
  }

  /*
   * test checkValidStr with valid string expected to return true
   */
  @Test
  public void testcheckValidStr2() {
    String validInput = "\"valid input\"";
    Boolean result = AppendFile.checkValidStr(validInput);
    assertEquals(true, result);
  }

  /*
   * test exectueEcho with invalid inputs expected to return an error message
   */
  @Test
  public void testexecuteEcho1() {
    String[] args1 = { "\"valid str\"", ">>" };
    String[] args2 = { "\"valid str\"", ">>", "file1", "extra input" };
    String[] args3 = { "invalid str", ">>", "file1" };
    String output1 = af.executeEcho(args1, ">>");
    String output2 = af.executeEcho(args2, ">>");
    String output3 = af.executeEcho(args3, ">>");
    assertEquals("invalid input", output1);
    assertEquals("invalid input", output2);
    assertEquals("invalid input", output3);
    String[] args4 = { "\"valid str\"", ">", "file1" };
    String output4 = af.executeEcho(args4, ">>");
    assertEquals("invalid input", output4);
  }

  /*
   * test exectueEcho with invalid file Name expected to return an error message
   */
  @Test
  public void testexecuteEcho2() {
    fs.createDirectoryCurrentDirectory("A");
    String[] args1 = { "\"valid str\"", ">>", "file1*" };
    String[] args2 = { "\"valid str\"", ">>", "A/file1*" };
    String output1 = af.executeEcho(args1, ">>");
    String output2 = af.executeEcho(args2, ">>");
    assertEquals("invalid file name", output1);
    assertEquals("invalid file name", output2);
  }

  /*
   * test exectueEcho with duplicated directory Name expected to return an error
   * message
   */
  @Test
  public void testexecuteEcho3() {
    fs.createDirectoryCurrentDirectory("A");
    String[] args = { "\"valid str\"", ">>", "A" };
    String output = af.executeEcho(args, ">>");
    assertEquals("duplicated directory name exists", output);
  }

  /*
   * test exectueEcho with invalid path expected to return an error message
   */
  @Test
  public void testexecuteEcho4() {
    fs.createDirectoryCurrentDirectory("A");
    String[] args = { "\"valid str\"", ">>", "someInvalidpath/file" };
    String output = af.executeEcho(args, ">>");
    assertEquals("invalid path", output);
  }

  /*
   * test exectueEcho with valid input and new file expected to return an empty
   * string, create the file with given contents
   */
  @Test
  public void testexecuteEcho5() {
    fs.createDirectoryCurrentDirectory("A");
    String[] args1 = { "\"file1: contents\"", ">>", "file1" };
    String[] args2 = { "\"file2: contents\"", ">>", "A/file2" };
    String output1 = af.executeEcho(args1, ">>");
    String output2 = af.executeEcho(args2, ">>");
    File file1 = (File) fs.checkPath("file1", "file");
    String contentsOfFile1 = file1.getContent();
    assertEquals("", output1);
    assertEquals("", output2);
    File file2 = (File) fs.checkPath("A/file2", "file");
    assertEquals("file1: contents", contentsOfFile1);
    String contentsOfFile2 = file2.getContent();
    assertEquals("file2: contents", contentsOfFile2);
  }

  /*
   * test exectueEcho with valid input and existing file expected to return an
   * empty string and append the existing file with given contents
   */
  @Test
  public void testexecuteEcho6() {
    testexecuteEcho5();
    String[] args1 = { "\" appended file1\"", ">>", "file1" };
    String[] args2 = { "\" appended file2\"", ">>", "A/file2" };
    String output1 = af.executeEcho(args1, ">>");
    String output2 = af.executeEcho(args2, ">>");
    File file1 = (File) fs.checkPath("file1", "file");
    String contentsOfFile1 = file1.getContent();
    assertEquals("", output1);
    assertEquals("", output2);
    File file2 = (File) fs.checkPath("A/file2", "file");
    assertEquals("file1: contents appended file1", contentsOfFile1);
    String contentsOfFile2 = file2.getContent();
    assertEquals("file2: contents appended file2", contentsOfFile2);
  }
}
