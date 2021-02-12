package test;

import static org.junit.Assert.assertEquals;

import commands.Move;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveTest {

  FileSystem fs;
  Move mv;
  String oldPath;
  String newPath;

  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    mv = new Move();
    oldPath = "";
    newPath = "";
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

  // move exist file to an exist file
  @Test
  public void moveFileIntoFileTest1() {
    File oldFile = fs.createFileWithParent("file1", fs.getRoot());
    File newFile = fs.createFileWithParent("file2", fs.getRoot());
    oldFile.setContent("I love you");
    newFile.setContent("I hate you");

    oldPath = "/file1";
    newPath = "/file2";

    mv.moveFileIntoFile(oldPath, newPath);

    File oldFile1 = (File) fs.checkPath(oldPath, "file");
    File newFile1 = (File) fs.checkPath(newPath, "file");


    assertEquals("I love you", newFile1.getContent());
    assertEquals(null, oldFile1);

  }

  // move exist file to an exist directory
  @Test
  public void moveFileIntoDirTest1() {
    File oldFile = fs.createFileWithParent("file1", fs.getRoot());
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    oldFile.setContent("I love you");

    oldPath = "/file1";
    newPath = "/dir1";

    mv.moveFileIntoDir(oldPath, newPath);

    String newPath1 = "/dir1/file1";

    File oldFile1 = (File) fs.checkPath(oldPath, "file");
    File newFile1 = (File) fs.checkPath(newPath1, "file");

    assertEquals("I love you", newFile1.getContent());
    assertEquals(null, oldFile1);


    
  }
  
  /**
   * test run invalid number of argument.
   */
  @Test
  public void runTest1() {
    String[] args = {""};
    String expected = "Invalid number of arguments";

    assertEquals(expected, mv.run(args));

  }

  /**
   * test run move a directory to file.
   */
  @Test
  public void runTest2() {
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    fs.createFileWithParent("file1", fs.getRoot());
    String[] args = {"/dir1", "/file1"};
    String expected = "Can not move a directory to a file";

    assertEquals(expected, mv.run(args));

  }

  /**
   * test run move a directory to its children.
   */
  @Test
  public void runTest3() {
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    String[] args = {"/", "/dir1"};
    String expected = 
        "Can not move a parent directory into its children directory";

    assertEquals(expected, mv.run(args));

  }
  
  /**
   * test run move a directory recursively to exist directory.
   */
  @Test
  public void runTest4() {
    Directory dir1 = fs.createDirectoryWithParentWithReturn(
        "dir1", fs.getRoot());
    Directory dir2 = fs.createDirectoryWithParentWithReturn(
        "dir2", fs.getRoot());
    fs.createDirectoryWithParentWithReturn("dir11", dir1);
    String[] args = {"/dir1", "/dir2"};
    mv.run(args);

    assertEquals(true, dir2.containComponent("dir1"));
  }

}
