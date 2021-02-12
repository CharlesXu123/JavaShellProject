package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import commands.PushDirectory;
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import java.util.Stack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Also not much to test for pushd.
 *
 * @author kanghung
 */
public class PushDirectoryTest {

  /**
   * pushd command obj.
   */
  PushDirectory pushd;

  /**
   * FileSystem singleton instance.
   */
  FileSystem fs;

  /**
   * Prev cwd.
   */
  Directory prevCwd;

  /**
   * Expected stack of push-ed directories.
   */
  Stack<Directory> expectedDirStk;

  /**
   * Expected cwd after an execution of pushd.
   */
  Directory expectedCwd;

  /**
   * A helper method to print each item in a stack of directories.
   *
   * @param stk Stack of Dir nodes
   */
  private void printStack(Stack<Directory> stk) {
    for (Directory each : stk) {
      System.out.println("\t" + each.getName());
    }
  }

  /**
   * A helper method to print expected and actual stack.
   */
  private void printStackCmp() {
    // compare the stack
    System.out.println("Expected (Pushed):");
    printStack(expectedDirStk);
    System.out.println("Actual (dir stack):");
    printStack(DirectoryStack.stack);
  }

  /**
   * A helper to set each test vars when running pushd.
   *
   * @param dirName Valid path of dir to push in string
   */
  private void pushDir(String dirName) {
    // setup for pushd.executeCommand()
    Directory dirToPush = (Directory) fs.checkPath(dirName, "directory");
    if (dirToPush != null) {
      // pushd does not push when user's trying to cd into invalid dir
      // just to be safe ;)

      // set expectedCwd
      expectedCwd = dirToPush;

      // set prevCwd
      prevCwd = fs.getCurrentDirectory();

      // execute pushd
      String[] arg = {dirName};
      pushd.executeCommand(arg);

      // set pushed stack
      expectedDirStk.push(prevCwd);
    } else {
      // pass invalid dir to pushd.executeCommand()
      // Do not update: prevCwd, expectedDirStk, expectedCwd
      String[] invalidDirArg = {dirName};
      pushd.executeCommand(invalidDirArg);
    }
  }


  /**
   * Setup file system singleton and pushd command obj.
   * Also creates a set of directories to test pushd.
   * 
   * <p>
   * /dir1/
   * /dir1/dir11/
   * 
   * /dir2/
   * </p>
   */
  @Before
  public void setup() {
    pushd = new PushDirectory();
    fs = FileSystem.createFileSystem();
    expectedDirStk = new Stack<>();
    prevCwd = fs.getCurrentDirectory();
    expectedCwd = fs.getCurrentDirectory();

    // set up directories
    fs.createDirectoryWithParent("dir11",
        fs.createDirectoryCurrentDirectory("dir1"));
    fs.createDirectoryCurrentDirectory("dir2");
  }

  /**
   * Tear down fileSystem instance. From Piazza post @579.
   * Also clear dir stack.
   *
   * @throws Exception general exception, @579
   */
  @After
  public void tearDown() throws Exception {
    Field f = (fs.getClass()).getDeclaredField("fs");
    f.setAccessible(true);
    f.set(null, null);

    DirectoryStack.stack.clear();
  }

  /**
   * Test: invalid inputs (no arg).
   */
  @Test
  public void testNoArg() {
    String[] empty = new String[0];
    assertEquals(true, DirectoryStack.stack.isEmpty());
    pushd.executeCommand(empty);
  }

  /**
   * Test: invalid inputs (two arg).
   */
  @Test
  public void testTwoArgs() {
    DirectoryStack.stack.clear();
    String[] twoArgs = {"/dir1", "/dir2"};
    pushd.executeCommand(twoArgs);
    assertTrue(DirectoryStack.stack.isEmpty());
  }

  /**
   * Test single dir.
   */
  @Test
  public void testSingleDir() {
    pushDir("dir1");

    printStackCmp();

    assertEquals(expectedDirStk, DirectoryStack.stack);
    assertEquals(expectedCwd, fs.getCurrentDirectory());
  }

  /**
   * Test pushing multiple dir.
   */
  @Test
  public void testMultDirs() {
    pushDir("/dir1");
    pushDir("..");
    pushDir(".");
    pushDir("/dir1/dir11");
    pushDir("../../dir2");

    printStackCmp();

    assertEquals(expectedDirStk, DirectoryStack.stack);
    assertEquals(expectedCwd, fs.getCurrentDirectory());
  }

  /**
   * Test pushing invalid dir.
   */
  @Test
  public void testInvalidDir() {
    pushDir("/non_existent_dir");

    assertTrue(DirectoryStack.stack.isEmpty());
    assertEquals(expectedCwd, fs.getCurrentDirectory());
  }
}