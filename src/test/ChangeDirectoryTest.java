package test;

import static org.junit.Assert.assertEquals;

import commands.ChangeDirectory;
import filesystem.Directory;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class ChangeDirectoryTest {

  FileSystem fs;
  String path;
  ChangeDirectory cd;

  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    path = "";
    cd = new ChangeDirectory();
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



  /**
   * test "cd /".
   */
  @Test
  public void executeCommandTest1() {
    String[] args = {"/"};

    cd.executeCommand(args);

    assertEquals(fs.getCurrentDirectory(), fs.getRoot());

  }

  /**
   * test "cd ".
   */
  @Test
  public void executeCommandTest2() {
    String[] args = {""};
    Directory dir1 = fs.getCurrentDirectory();
    cd.executeCommand(args);

    assertEquals(dir1, fs.getCurrentDirectory());
  }

  /**
   * test "cd dir1 dir2".
   */
  @Test
  public void executeCommandTest3() {
    String[] args = {"dir1", "dir2"};
    Directory dir1 = fs.getCurrentDirectory();
    cd.executeCommand(args);

    assertEquals(dir1, fs.getCurrentDirectory());
  }

  // current directory's parent is not null
  @Test
  public void executeCommandTest4() {
    String[] args = {"/dir1/dir11"};
    Directory dir1 = fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    Directory dir11 = fs.createDirectoryWithParentWithReturn("dir11", dir1);

    cd.executeCommand(args);
    assertEquals(dir11, fs.getCurrentDirectory());

  }

}
