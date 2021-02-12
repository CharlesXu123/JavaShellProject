package test;

import static org.junit.Assert.assertEquals;

import commands.Copy;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CopyTest {

  FileSystem fs;
  Copy cp;
  String oldPath;
  String newPath;

  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    cp = new Copy();
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

  // file does not exist
  @Test
  public void creatFileTest1() {
    newPath = "/file2";

    cp.creatFile("I love you", newPath);

    File newFile = (File) fs.checkPath(newPath, "file");

    assertEquals("I love you", newFile.getContent());

  }

  // file exist
  @Test
  public void creatFileTest2() {
    File newFile = fs.createFileWithParent("file2", fs.getRoot());
    newFile.setContent("I hate you");

    newPath = "/file2";

    cp.creatFile("I love you", newPath);

    File newFile1 = (File) fs.checkPath(newPath, "file");

    assertEquals("I love you", newFile1.getContent());

  }

  // copy exist file to an exist file
  @Test
  public void copyFileTest1() {
    File oldFile = fs.createFileWithParent("file1", fs.getRoot());
    File newFile = fs.createFileWithParent("file2", fs.getRoot());
    oldFile.setContent("I love you");
    newFile.setContent("I hate you");

    oldPath = "/file1";
    newPath = "/file2";

    cp.copyFile(oldPath, newPath);

    File newFile1 = (File) fs.checkPath(newPath, "file");


    assertEquals("I love you", newFile1.getContent());

  }

  // copy exist file to an exist directory
  @Test
  public void copyFileIntoDirTest1() {
    File oldFile = fs.createFileWithParent("file1", fs.getRoot());
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    oldFile.setContent("I love you");

    oldPath = "/file1";
    newPath = "/dir1";

    cp.copyFileIntoDir(oldPath, newPath);
    String newPath1 = "/dir1/file1";

    File newFile1 = (File) fs.checkPath(newPath1, "file");

    assertEquals("I love you", newFile1.getContent());

  }

  // true case
  @Test
  public void isParentDirTest1() {
    Directory dir1 = fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    Directory dir11 = fs.createDirectoryWithParentWithReturn("dir11", dir1);


    assertEquals(true, cp.isParentDir(dir1, dir11));

  }

  // false case
  @Test
  public void isParentDirTest2() {
    Directory dir1 = fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    Directory dir2 = fs.createDirectoryWithParentWithReturn("dir2", fs.getRoot());


    assertEquals(false, cp.isParentDir(dir1, dir2));

  }


  /**
   * test run invalid number of argument.
   */
  @Test
  public void runTest1() {
    String[] args = {""};
    String expected = "Invalid number of arguments";

    assertEquals(expected, cp.run(args));

  }

  /**
   * test run move a directory to file.
   */
  @Test
  public void runTest2() {
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    fs.createFileWithParent("file1", fs.getRoot());
    String[] args = {"/dir1", "/file1"};
    String expected = "Can not copy a directory to a file";

    assertEquals(expected, cp.run(args));

  }

  /**
   * test run move a directory to its children.
   */
  @Test
  public void runTest3() {
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    String[] args = {"/", "/dir1"};
    String expected = 
        "Can not copy a parent directory into its children directory";

    assertEquals(expected, cp.run(args));

  }
  
  /**
   * test run copy a directory recursively to exist directory.
   */
  @Test
  public void runTest4() {
    Directory dir1 = fs.createDirectoryWithParentWithReturn(
        "dir1", fs.getRoot());
    Directory dir2 = fs.createDirectoryWithParentWithReturn(
        "dir2", fs.getRoot());
    fs.createDirectoryWithParentWithReturn("dir11", dir1);
    String[] args = {"/dir1", "/dir2"};
    cp.run(args);

    assertEquals(true, dir2.containComponent("dir1"));
  }
}
