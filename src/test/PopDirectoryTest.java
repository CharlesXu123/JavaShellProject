package test;

import static org.junit.Assert.assertEquals;

import commands.PopDirectory;
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Not much to test for popd.
 *
 * @author kanghung
 */
public class PopDirectoryTest {

  /**
   * Popd command object.
   */
  private PopDirectory popd;

  /**
   * FileSystem singleton instance.
   */
  private FileSystem fs;

  /**
   * Dir node that was previously current working dir (cwd).
   */
  Directory prevCwd;

  /**
   * A helper method to simulate what pushd does inside JShell.
   *
   * @param pathStr String path of an existing dir node
   */
  private void pushDir(String pathStr) {
    Directory pathDir = (Directory) fs.checkPath(pathStr, "directory");
    if (pathDir != null) {
      // for safety
      DirectoryStack.stack.push(pathDir);
    }
  }

  /**
   * Compare cwd with prevCwd.
   */
  private void cmpCurrDir(String[] customArg, String expectingCwdPath,
      boolean printComparison) {
    // before switching
    prevCwd = fs.getCurrentDirectory();

    String[] arg;
    if (customArg == null) {
      arg = new String[0];
    } else {
      arg = customArg;
    }

    // do the switching
    popd.executeCommand(arg);

    // compare before and after
    if (expectingCwdPath == null) {
      if (printComparison) {
        System.out.println("Expected:\t" + FileSystem.findPath(prevCwd));
        System.out.println(
            "Actual:\t" + FileSystem.findPath(fs.getCurrentDirectory()));
      }
      assertEquals(prevCwd, fs.getCurrentDirectory());
    } else {
      if (printComparison) {
        System.out.println("Expected:\t" + FileSystem
            .findPath((Directory) fs.checkPath(expectingCwdPath, "directory")));
        System.out.println(
            "Actual:\t" + FileSystem.findPath(fs.getCurrentDirectory()));
      }

      Directory expectingCwd =
          (Directory) fs.checkPath(expectingCwdPath, "directory");
      assertEquals(expectingCwd, fs.getCurrentDirectory());
    }
  }

  /**
   * Setup file system singleton object and popd command obj. Also create a set
   * of directories to test against popd.
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
    popd = new PopDirectory();
    fs = FileSystem.createFileSystem();
    prevCwd = fs.getCurrentDirectory();

    // set up directories
    fs.createDirectoryWithParent("dir11",
        fs.createDirectoryCurrentDirectory("dir1"));
    fs.createDirectoryCurrentDirectory("dir2");
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
   * Try popd when dir stack is empty.
   */
  @Test
  public void testEmptyPop() {
    // redundant but bleh
    DirectoryStack.stack.clear();

    cmpCurrDir(null, null, false);
  }

  /**
   * When there's single dir inside dir stack.
   */
  @Test
  public void testSingleDir() {
    pushDir("/dir1");
    cmpCurrDir(null, "/dir1", false);
  }

  /**
   * Check if the order of pop is correct.
   */
  @Test
  public void testPopdOrder() {
    pushDir("/dir1");
    pushDir("/dir1/dir11");
    pushDir("dir2");  // relative

    cmpCurrDir(null, "/dir2", false);
    cmpCurrDir(null, "/dir1/dir11", false);
    cmpCurrDir(null, "/dir1", false);
  }
}
