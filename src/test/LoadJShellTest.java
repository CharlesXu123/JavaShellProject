package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import commands.LoadJShell;
import filesystem.DirectoryStack;
import filesystem.FileSystem;
import filesystem.HistoryArray;
import commands.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class LoadJShellTest {

  LoadJShell lj;
  FileSystem fs;
  HistoryArray ha;
  List ls;
  java.io.File realFile;
  String[] savedFile1 = new String[1];
  String[] savedFile2 = new String[1];
  String[] savedFile3 = new String[1];
  String[] wrongSavedFile = new String[1];

  /**
   * Setup for testing.
   */
  @Before
  public void setup() {
    lj = new LoadJShell();
    ls = new List();
    fs = FileSystem.createFileSystem();
    ha = HistoryArray.createHistoryArray();
    savedFile1[0] = "JShellFile1";
    savedFile2[0] = "JShellFile2";
    savedFile3[0] = "JShellFile3";
    wrongSavedFile[0] = "JShellFileWrong";
    ha.addHistory("loadJShell" + "some path");
  }

  /**
   * Tear down fileSystem instance. From Piazza post @579.
   */
  @After
  public void tearDown() throws Exception {
    Field f = (fs.getClass()).getDeclaredField("fs");
    f.setAccessible(true);
    f.set(null, null);
    ha.cleanHistoryArray();
    DirectoryStack.stack.clear();
  }

  /*
   * Test executeLoadJShell when given wrong number of arguments expected to
   * return an error message
   */
  @Test
  public void testexecuteLoadJShell1() {
    String[] wrongArgs = {"input1", "input2"};
    String output = lj.executeLoadJShell(wrongArgs);
    assertEquals("invalid number of input", output);
  }

  /*
   * Test executeLoadJShell when given path is invalid expected to return an
   * error message
   */
  @Test
  public void testexecuteLoadJShell2() {
    String[] wrongArgs = {"some/wrong/path/file"};
    String output = lj.executeLoadJShell(wrongArgs);
    assertEquals("\"File Not Found", output);
  }

  /*
   * Test executeLoadJShell when the file is not valid expected to return an
   * error message
   */
  @Test
  public void testexecuteLoadJShell3() {
    String output = lj.executeLoadJShell(wrongSavedFile);
    assertEquals("invalid file contents", output);
  }

  /*
   * Test executeLoadJShell when already modifies JShell expected to return an
   * error message
   */
  @Test
  public void testexecuteLoadJShell4() {
    ha.addHistory("some history to show modified JShell");
    ha.addHistory("loadJShell" + savedFile1[0]);
    String output = lj.executeLoadJShell(savedFile1);
    assertEquals("modified this JShell already", output);
  }

  /*
   * Test executeLoadJShell when load a saveJShell File expected to reinitialize
   * everything as in saveJShell File
   */
  @Test
  public void testexecuteLoadJShell5() {
    String output = lj.executeLoadJShell(savedFile1);
    assertEquals(null, output);
    String fileSystemToString = ls.recurList("/", fs.getRoot(), true);
    assertEquals("/:\n\n", fileSystemToString);
    String currentDirToString = fs.getCurrentDirectory().getName();
    assertEquals("/", currentDirToString);
    String historyArrayToString = ha.checkHistory();
    assertEquals("1. saveJShell JShellFile1\n" + "2. loadJShellsome path",
        historyArrayToString);
    String directoryStackToString = DirectoryStack.toStrDirectoryStack();
    assertEquals("", directoryStackToString);
  }

  /*
   * Test executeLoadJShell when load a saveJShell File expected to reinitialize
   * everything as in saveJShell File
   */
  @Test
  public void testexecuteLoadJShell6() {
    String output = lj.executeLoadJShell(savedFile2);
    assertEquals(null, output);
    String fileSystemToString = ls.recurList("/", fs.getRoot(), true);
    assertEquals("/:\n" + "dir1\n" + "\"file1\"file1 contents\n" + "\n"
        + "/dir1:\n" + "\"file1\"file2 contents\n\n", fileSystemToString);
    String currentDirToString = fs.getCurrentDirectory().getName();
    assertEquals("/", currentDirToString);
    String historyArrayToString = ha.checkHistory();
    assertEquals(
        "1. mkdir dir1\n" + "2. echo \"file1 contents\" > file1\n"
            + "3. echo \"file2 contents\" > dir1/file1\n"
            + "4. saveJShell JShellFile2\n" + "5. loadJShellsome path",
        historyArrayToString);
    String directoryStackToString = DirectoryStack.toStrDirectoryStack();
    assertEquals("", directoryStackToString);
  }

  /*
   * Test executeLoadJShell when load a saveJShell File expected to reinitialize
   * everything as in saveJShell File
   */
  @Test
  public void testexecuteLoadJShell7() {
    String output = lj.executeLoadJShell(savedFile3);
    assertEquals(null, output);
    String fileSystemToString = ls.recurList("/", fs.getRoot(), true);
    assertEquals("/:\n" + "dir1\n" + "\"file1\"Str\n" + "\n" + "/dir1:\n"
        + "\"file2\"Str\n\n", fileSystemToString);
    String currentDirToString = fs.getCurrentDirectory().getName();
    assertEquals("dir1", currentDirToString);
    String historyArrayToString = ha.checkHistory();
    assertEquals("1. mkdir dir1\n" + "2. echo \"Str\" > file1\n"
        + "3. pushd dir1\n" + "4. echo \"Str\" >file2\n"
        + "5. saveJShell JShellFile3\n" + "6. loadJShellsome path",
        historyArrayToString);
    String directoryStackToString = DirectoryStack.toStrDirectoryStack();
    assertEquals("/\n", directoryStackToString);
  }

}
