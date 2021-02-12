package test;

import static org.junit.Assert.assertEquals;

import commands.Tree;
import filesystem.Directory;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Tree command.
 *
 * @author kanghung
 */
public class TreeTest {

  /**
   * Tree will always print "/\n" as the first (might be the last) line.
   */
  private static final String DEFAULT = "/\n";

  /**
   * String literal for fs.checkPath() method.
   */
  private static final String DIR_LITERAL = "directory";

  /**
   * Command Tree object.
   */
  Tree treeObj;

  /**
   * FileSystem singleton.
   */
  FileSystem fs;

  /**
   * Arguments that are from the user.
   */
  String[] args;

  /**
   * Expected output of Tree command to compare with the actual output.
   */
  String expectedOutput;

  /**
   * Initialize needed variables.
   */
  @Before
  public void setup() {
    treeObj = new Tree();
    args = new String[0];
    fs = FileSystem.createFileSystem();
    expectedOutput = DEFAULT;
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
   * Empty file system.
   */
  @Test
  public void test1() {
    assertEquals(DEFAULT, treeObj.run(args));
  }

  /**
   * Empty file system with some args.
   */
  @Test
  public void test2() {
    args = new String[2];
    args[0] = "random";
    args[1] = "things";
    assertEquals(null, treeObj.run(args));
  }

  /**
   * Empty file system with no arg (correct usage).
   */
  @Test
  public void test3() {
    assertEquals(expectedOutput, treeObj.run(args));
  }

  /**
   * Two folders at root level.
   */
  @Test
  public void test4() {
    fs.createDirectoryWithParent("a", fs.getRoot());
    fs.createFileWithParent("b", fs.getRoot());
    expectedOutput += "\ta\n\tb\n";
    assertEquals(expectedOutput, treeObj.run(args));
  }

  /**
   * Multi-level sub-dirs and files. Also, current dir isn't root when running
   * Tree.
   */
  @Test
  public void test5() {
    // create file system with multiple depths
    int depth = 5;

    for (int i = 1; i <= depth; i++) {
      fs.createDirectoryCurrentDirectory("dir" + i);
      fs.createFileUnderCurrentDirectory("file" + i);
      fs.setCurrentDirectory((Directory) fs.checkPath("dir" + i, DIR_LITERAL));
    }

    for (int i = 1; i <= depth; i++) {
      String indent = "";
      for (int j = 0; j < i; j++) {
        indent += "\t";
      }
      expectedOutput += indent + "dir" + i + "\n";
    }

    for (int i = depth; i != 0; i--) {
      String indent = "";
      for (int j = 0; j < i; j++) {
        indent += "\t";
      }
      expectedOutput += indent + "file" + i + "\n";
    }

    assertEquals(expectedOutput, treeObj.run(args));
  }


  /**
   * Second example from the handout.
   */
  @Test
  public void test6() {
    fs.createDirectoryCurrentDirectory("A");
    fs.createDirectoryCurrentDirectory("B");
    fs.createDirectoryCurrentDirectory("C");

    fs.setCurrentDirectory((Directory) fs.checkPath("A", DIR_LITERAL));
    fs.createDirectoryCurrentDirectory("A1");
    fs.createDirectoryCurrentDirectory("A2");

    expectedOutput += "\tA\n\t\tA1\n\t\tA2\n\tB\n\tC\n";
    assertEquals(expectedOutput, treeObj.run(args));
  }


}
