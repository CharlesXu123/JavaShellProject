package test;

import static org.junit.Assert.assertEquals;

import commands.History;
import commands.SaveJShell;
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.File;
import filesystem.FileSystem;
import filesystem.HistoryArray;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SaveJShellTest {

  SaveJShell sj;
  FileSystem fs;
  History hs;
  HistoryArray ha;
  java.io.File realFileCls;
  String[] args;

  /**
   * Setup for testing.
   */
  @Before
  public void setup() {
    this.sj = new SaveJShell();
    this.fs = FileSystem.createFileSystem();
    this.hs = new History();
    this.ha = HistoryArray.createHistoryArray();
    this.args = new String[1];
    args[0] = "JShellFile";
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
    ha.cleanHistoryArray();
    DirectoryStack.stack.clear();
  }

  @Test
  public void testexecuteSaveJShell1() {
    sj.executeCommand(args);
    String contents = "";
    Scanner reader;
    try {
      realFileCls = new java.io.File(args[0]);
      reader = new Scanner(realFileCls);
      while (reader.hasNextLine()) {
        String content = reader.nextLine();
        contents += content + "\n";
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expected = "this is a saved JShell\n" + "next.\n" + "/:\n"
        + "next.\n" + "History empty\n" + "next.\n"
        + "DirectoryStack empty\nnext.\n" + "/\n";
    assertEquals(expected, contents);

  }

  @Test
  public void testexecuteSaveJShell2() {
    ha.addHistory("his1");
    ha.addHistory("his2");
    assertEquals("", DirectoryStack.toStrDirectoryStack());
    sj.executeCommand(args);
    String contents = "";
    Scanner reader;
    try {
      realFileCls = new java.io.File(args[0]);
      reader = new Scanner(realFileCls);
      while (reader.hasNextLine()) {
        String content = reader.nextLine();
        contents += content + "\n";
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expected = "this is a saved JShell\n" + "next.\n" + "/:\n"
        + "next.\n" + "1. his1\n" + "2. his2\n" + "next.\n"
        + "DirectoryStack empty\nnext.\n" + "/\n";
    assertEquals(expected, contents);

  }

  @Test
  public void testexecuteSaveJShell3() {
    ha.addHistory("his1");
    ha.addHistory("his2");
    fs.createDirectoryCurrentDirectory("A");
    fs.createDirectoryCurrentDirectory("B");
    fs.setCurrentDirectory((Directory) fs.checkPath("A", "directory"));
    fs.createDirectoryCurrentDirectory("E");
    File b = fs.createFileUnderCurrentDirectory("b");
    b.appendContent("content of b");
    sj.executeCommand(args);
    String contents = "";
    Scanner reader;
    try {
      realFileCls = new java.io.File(args[0]);
      reader = new Scanner(realFileCls);
      while (reader.hasNextLine()) {
        String content = reader.nextLine();
        contents += content + "\n";
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expected = "this is a saved JShell\n" + "next.\n" + "/:\n" + "A\n"
        + "B\n" + "\n" + "/A:\n" + "E\n" + "\"b\"content of b\n" + "next.\n"
        + "1. his1\n" + "2. his2\n" + "next.\n"
        + "DirectoryStack empty\nnext.\n" + "/A\n";
    assertEquals(expected, contents);
  }

  @Test
  public void testexecuteSaveJShell4() {
    testexecuteSaveJShell3();
    fs.setCurrentDirectory(fs.getRoot());
    DirectoryStack.stack.push((Directory) fs.checkPath("A", "directory"));
    DirectoryStack.stack.push((Directory) fs.checkPath("B", "directory"));
    fs.setCurrentDirectory((Directory) fs.checkPath("A", "directory"));
    sj.executeCommand(args);
    String contents = "";
    Scanner reader;
    try {
      realFileCls = new java.io.File(args[0]);
      reader = new Scanner(realFileCls);
      while (reader.hasNextLine()) {
        String content = reader.nextLine();
        contents += content + "\n";
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expected = "this is a saved JShell\n" + "next.\n" + "/:\n" + "A\n"
        + "B\n" + "\n" + "/A:\n" + "E\n" + "\"b\"content of b\n" + "next.\n"
        + "1. his1\n" + "2. his2\n" + "next.\n" + "/A\n" + "/B\n" + "next.\n"
        + "/A\n";
    assertEquals(expected, contents);

  }

  /*
   * test executeCommand when take invalid number of input expected to return an
   * error message
   */
  @Test
  public void testexecuteSaveJShell5() {
    String[] wrongArgs1 = {};
    String[] wrongArgs2 = {"/path/file", "extra input"};
    String output1 = sj.executeSaveJShell(wrongArgs1);
    String output2 = sj.executeSaveJShell(wrongArgs2);
    assertEquals("invalid number of input", output1);
    assertEquals("invalid number of input", output2);
  }

  /*
   * test executeCommand when take an wrong path expected to return an error
   * message
   */
  @Test
  public void testexecuteSaveJShell6() {
    String[] wrongArgs = {"wrong/path/file"};
    String output = sj.executeSaveJShell(wrongArgs);
    assertEquals("unable to create file under given path", output);
  }

}
