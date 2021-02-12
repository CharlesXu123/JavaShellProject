package test;

import static org.junit.Assert.assertEquals;

import commands.ClientUrl;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ClientUrlTest {

  FileSystem fs;
  String path;
  ClientUrl curl;

  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    path = "";
    curl = new ClientUrl();
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
   * test run, invalid URL.
   */
  @Test
  public void runTest1() {
    path = "asasasas";
    String[] args = {path};
    String expected = "Invalid URL is provided";

    assertEquals(expected, curl.run(args)[0]);
  }

  /**
   * test run, URL does not direct to a file.
   */
  @Test
  public void runTest2() {
    path = "http://www.cs.cmu.edu/~spok/grimmtmp/fileNa";
    String[] args = {path};
    String expected = args[0] + " does not direct to a file";

    assertEquals(expected, curl.run(args)[0]);

  }

  /**
   * test run, invalid number of arguments.
   */
  @Test
  public void runTest3() {
    String[] args = {"", ""};
    String expected = "Invalid number of arguments";

    assertEquals(expected, curl.run(args)[0]);
  }

  /**
   * test run, file has already exist.
   */
  @Test
  public void runTest4() {
    String fileName = "073txt";
    fs.createFileUnderCurrentDirectory(fileName);
    path = "http://www.cs.cmu.edu/~spok/grimmtmp/073.txt";
    String[] args = {path};
    String expected = (fileName + " has already exist");

    assertEquals(expected, curl.run(args)[0]);
  }

  /**
   * test run, file has already exist.
   */
  @Test
  public void runTest5() {
    path = "http://www.cs.cmu.edu/~spok/grimmtmp/073.txt";
    String[] args = {path};
    String expected = ("073txt");

    assertEquals(expected, curl.run(args)[0]);
  }

  /**
   * test executeCommand.
   */
  @Test
  public void executeCommandTest1() {
    path = "http://www.cs.cmu.edu/~spok/grimmtmp/073.txt";
    String[] args = {path};
    String expected = "073txt";
    curl.executeCommand(args);

    File newFile = (File) fs.checkPath("/073txt", "file");

    assertEquals(expected, newFile.getName());
  }


}
