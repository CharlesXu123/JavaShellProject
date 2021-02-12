package test;

import static org.junit.Assert.assertEquals;

import commands.Concatenation;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConcatenationTest {
  FileSystem fs;
  Directory dir;
  String name;
  File file1;
  File file2;
  Concatenation ca;

  /**
   * set up.
   */
  @Before
  public void setUp() {
    name = "dirName";
    fs = FileSystem.createFileSystem();
    dir = new Directory(name, null);
    file1 = fs.createFileWithParent("file11", fs.getRoot());
    file1.setContent("I love you");
    file2 = fs.createFileWithParent("file2", fs.getRoot());
    file2.setContent("I hate you");
    ca = new Concatenation();
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

  // file exist
  @Test
  public void printContentTest1() {
    String path = "/file11";
    String[] status = new String[2];
    String[] expected = {"I love you", "safe"};
    status = ca.printContent(path, status);

    assertEquals(expected[0], status[0]);
    assertEquals(expected[1], status[1]);


  }

  // file does not exist
  @Test
  public void printContentTest2() {
    String path = "/file3";
    String[] status = new String[2];
    String[] expected = {null, "break"};
    status = ca.printContent(path, status);

    assertEquals(expected[0], status[0]);
    assertEquals(expected[1], status[1]);


  }

  // run two exist file path
  @Test
  public void runTest1() {
    String path1 = "/file11";
    String path2 = "/file2";
    String[] args = {path1, path2};
    String expected = "I love you" + "\n\n\n\n" + "I hate you";
    String actual = ca.run(args);

    assertEquals(expected, actual);


  }

  // run three file path which the second one does not exist
  @Test
  public void runTest2() {
    String path1 = "/file11";
    String path2 = "/file2";
    String path3 = "/file3";
    String[] args = {path1, path3, path2};
    String expected = "I love you";
    String actual = ca.run(args);

    assertEquals(expected, actual);

  }

}
