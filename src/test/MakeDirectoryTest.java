package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import commands.MakeDirectory;
import filesystem.Directory;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for mkdir command.
 *
 * @author kanghung
 */
public class MakeDirectoryTest {

  /**
   * String literal for fs.checkPath() method.
   */
  private static final String DIR_LITERAL = "directory";

  /**
   * Command MakeDirectory object.
   */
  MakeDirectory mkdir;

  /**
   * FileSystem singleton.
   */
  FileSystem fs;

  /**
   * Flag to help testHelper() by indicating we're expecting null.
   */
  boolean[] expectingNull;

  /**
   * Arg list to feed in.
   */
  ArrayList<String> args;

  /**
   * A helper method to convert ArrayList args to an array.
   *
   * @return Array of args
   */
  private String[] getArgArr() {
    return this.args.toArray(new String[args.size()]);
  }

  /**
   * A helper method to compare if each path inside args is created, or not
   * created.
   *
   * @param expectingNullIndex Array of int to mark which arg is invalid path.
   */
  private void testHelper(int[] expectingNullIndex) {
    // run
    this.mkdir.executeCommand(getArgArr());
    // get expectingNull
    expectingNull = new boolean[args.size()];
    if (expectingNullIndex != null) {
      for (int index : expectingNullIndex) {
        expectingNull[index] = true;
      }
    } else {
      for (int i = 0; i < expectingNull.length; i++) {
        expectingNull[i] = false;
      }
    }
    // compare
    for (int i = 0; i < args.size(); i++) {
      String eachDirPath = args.get(i);
      Directory tmpDir = (Directory) fs.checkPath(eachDirPath, DIR_LITERAL);
      if (!expectingNull[i] && tmpDir == null) {
        fail(eachDirPath + " does not exist :(");
      } else if (expectingNull[i]) {
        // for eachDirPath only
        assertEquals(null, tmpDir);
      } else {
        assertEquals(tmpDir.getName(), fs.getParent(eachDirPath)[1]);
      }
    }
  }

  /**
   * A helper method that run List command to visualize fs singleton instance
   * during testing.
   *
   * @param msg Msg to print in debug console
   */
  private void runLs(String msg) {
    commands.List ls = new commands.List();
    String[] emptyArr = new String[0];
    System.out.println(msg);
    ls.executeCommand(emptyArr);
    System.out.println("\t" + fs);
  }

  /**
   * Setup for tests. Init mkdir obj, fs instance, and set args to a default
   * (empty array of strings).
   */
  @Before
  public void setup() {
    // set to default.
    mkdir = new MakeDirectory();
    fs = FileSystem.createFileSystem();
    args = new ArrayList<>();
  }

  /**
   * Tear down fileSystem instance. From Piazza post @579.
   *
   * @throws Exception general exception, @579
   */
  @After
  public void tearDown() throws Exception {
    Field f = (fs.getClass()).getDeclaredField("fs");
    f.setAccessible(true);
    f.set(null, null);
  }


  /**
   * Test when given empty args array.
   */
  @Test
  public void testEmptyArgs() {
    testHelper(null);
    runLs("after test1, empty args");
  }

  /**
   * Test when given a single abs path to create.
   */
  @Test
  public void testSingleArgAbsPath() {
    this.args.add("/abc");
    testHelper(null);
    runLs("after test2, single valid abs path: /abc");
  }

  /**
   * Test when given a single invalid abs path.
   */
  @Test
  public void testSingleArgInvalidAbsPath() {
    this.args.add("/abc/def/");
    int[] tmp = {0};
    testHelper(tmp);
    runLs("after test3, single invalid abs path: /abc/def/");
  }

  /**
   * Test when given a single rel path to create.
   */
  @Test
  public void testSingleArgValidRelPath() {
    this.args.add("a/");
    testHelper(null);
    runLs("after test4, single valid rel path: a/");
  }

  /**
   * Test when given single invalid rel path.
   */
  @Test
  public void testSingleArgInvalidRelPath() {
    args.add("abc/def/");
    int[] tmp = {0};
    testHelper(tmp);
    runLs("after test5, single invalid rel path: abc/def/");
  }

  /**
   * Test when given an invalid path in between valid paths.
   */
  @Test
  public void testInvalidPathInBetValidPaths() {
    args.add("a");
    args.add("abc/def");
    args.add("c");
    int[] tmp = {1, 2};
    testHelper(tmp);
    runLs("after test6");
  }

  /**
   * Test when given all valid paths.
   */
  @Test
  public void testMultipleValidPaths() {
    args.add("a");
    args.add("a/a1");
    args.add("b");
    args.add("b/b1");
    args.add("help");
    testHelper(null);
    runLs("after test7");
  }
}
