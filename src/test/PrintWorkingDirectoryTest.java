package test;

import static org.junit.Assert.assertEquals;

import commands.PrintWorkingDirectory;
import filesystem.Directory;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PrintWorkingDirectoryTest {

  FileSystem fs;
  String path;
  PrintWorkingDirectory pwd;

  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    path = "";
    pwd = new PrintWorkingDirectory();
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

  // current directory is root
  @Test
  public void getCurrentPathTest1() {
    fs.setCurrentDirectory(fs.getRoot());
    path = "/";
    assertEquals(path, pwd.getCurrentPath());
  }

  // current directory is not root
  @Test
  public void getCurrentPathTest2() {
    Directory dir1 = new Directory("dir1", fs.getRoot());
    Directory dir11 = new Directory("dir11", dir1);
    fs.setCurrentDirectory(dir11);

    path = "/dir1/dir11";

    assertEquals(path, pwd.getCurrentPath());
  }

}
