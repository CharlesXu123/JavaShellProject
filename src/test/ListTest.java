package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Test;

import commands.List;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

import org.junit.Before;

public class ListTest {

  FileSystem fs;
  List ls;

  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    ls = new List();
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
   * Setup fileSystem.
   */
  public void setupFileSystem() {
    fs.createDirectoryCurrentDirectory("A");
    fs.createDirectoryCurrentDirectory("B");
    fs.setCurrentDirectory((Directory) fs.checkPath("A", "directory"));
    fs.createDirectoryCurrentDirectory("E");
    File b = fs.createFileUnderCurrentDirectory("b");
    b.appendContent("contents of b");
    fs.setCurrentDirectory(fs.getRoot());
  }

  // when fileSystem is empty;
  @Test
  public void testrecurList1() {
    String output = ls.recurList("/", fs.getRoot(), false);
    String outputWithContent = ls.recurList("/", fs.getRoot(), true);
    String expected = "/:\n\n";
    assertEquals(expected, output);
    assertEquals(expected, outputWithContent);
  }

  // when fileSystem contains directories and files;
  @Test
  public void testrecurList2() {
    setupFileSystem();
    // Directory s = (Directory) fs.checkPath("/A", "directory");
    // assertEquals(s==null, false);
    String output = ls.recurList("/", fs.getRoot(), false);
    String outputWithContent = ls.recurList("/", fs.getRoot(), true);
    String expected = "/:\n" + "A\n" + "B\n" + "\n" + "/A:\n" + "E\n" + "b\n\n";
    String expectedWithContent = "/:\n" + "A\n" + "B\n" + "\n" + "/A:\n" + "E\n"
        + "\"b\"contents of b\n\n";
    assertEquals(expected, output);
    assertEquals(expectedWithContent, outputWithContent);
  }

  // when the first given path does not exist;
  @Test
  public void testexecuteList1() {
    setupFileSystem();
    String[] args = {"this path does not exist"};
    String[] argsOfFullPath = {"A/this path does not exist"};
    String[] multiArgs = {"this path does not exist", "A", "B"};
    String[] recurArgs = {"-R", "this path does not exist"};
    String output = ls.executeList(args);
    String outputOfFullPath = ls.executeList(argsOfFullPath);
    String outputWithMultiArgs = ls.executeList(multiArgs);
    assertEquals("\"ls: " + args[0] + ": No such file or directory", output);
    assertEquals("\"ls: " + argsOfFullPath[0] + ": No such file or directory",
        outputOfFullPath);
    assertEquals("\"ls: " + multiArgs[0] + ": No such file or directory",
        outputWithMultiArgs);
    String outputRecur = ls.executeList(recurArgs);
    assertEquals("\"ls: " + recurArgs[1] + ": No such file or directory",
        outputRecur);
  }

  // when the path points to a file;
  @Test
  public void testexecuteList2() {
    setupFileSystem();
    fs.createFileUnderCurrentDirectory("c");
    String[] args = {"c"};
    String[] argsOfFullPath = {"A/b"};
    String[] multiArgs = {"c", "A/b"};
    String[] recurArgs = {"-R", "c"};
    String output = ls.executeList(args);
    String outputOfFullPath = ls.executeList(argsOfFullPath);
    String outputWithMultiArgs = ls.executeList(multiArgs);
    assertEquals("c\n", output);
    assertEquals("A/b\n", outputOfFullPath);
    assertEquals("c\nA/b\n", outputWithMultiArgs);
    String outputRecur = ls.executeList(recurArgs);
    assertEquals("c\n", outputRecur);
  }

  // when no path is given;
  @Test
  public void testexecuteList3() {
    setupFileSystem();
    String[] args = {};
    String[] recurArgs = {"-R"};
    String output = ls.executeList(args);
    String outputRecur = ls.executeList(recurArgs);
    assertEquals("A\nB\n", output);
    assertEquals("/:\n" + "A\n" + "B\n" + "\n" + "/A:\n" + "E\n" + "b\n\n",
        outputRecur);
  }

  // when the path points to a directory
  @Test
  public void testexecuteList4() {
    setupFileSystem();
    String[] args = {"A"};
    String[] argsOfFullPath = {"A/E"};
    String[] multiArgs = {"A", "A/E"};
    String[] recurArgs = {"-R", "A"};
    String output = ls.executeList(args);
    String outputOfFullPath = ls.executeList(argsOfFullPath);
    String outputWithMultiArgs = ls.executeList(multiArgs);
    assertEquals("A:\nE\nb\n", output);
    assertEquals("A/E:\n", outputOfFullPath);
    assertEquals("A:\nE\nb\nA/E:\n", outputWithMultiArgs);
    String outputRecur = ls.executeList(recurArgs);
    assertEquals("A:\n" + "E\n" + "b\n\n", outputRecur);
  }

  // when given a list of paths points to file, directory or invalid location
  @Test
  public void testexecuteList5() {
    setupFileSystem();
    String[] args = {"A", "B", "A/b", "this path does not exist", "should stop",
        "/"};
    String[] recurArgs = {"-R", "A", "B", "A/b", "this path does not exist",
        "should stop", "/"};
    String output = ls.executeList(args);
    String outputRecur = ls.executeList(recurArgs);
    assertEquals(
        "A:\n" + "E\n" + "b\n" + "B:\n" + "A/b\n"
            + "ls: this path does not exist: No such file or directory",
        output);
    // assertEquals("", output);
    assertEquals(
        "A:\n" + "E\n" + "b\n" + "\n" + "B:\n" + "\n" + "A/b\n"
            + "ls: this path does not exist: No such file or directory",
        outputRecur);
  }

}