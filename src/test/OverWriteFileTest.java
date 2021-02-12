package test;

import static org.junit.Assert.*;

import commands.OverWriteFile;
import filesystem.File;
import filesystem.FileSystem;
import org.junit.Before;
import org.junit.Test;

public class OverWriteFileTest {

  OverWriteFile of;
  FileSystem fs;

  @Before
  public void setup() {
    of = new OverWriteFile();
    fs = FileSystem.createFileSystem();
  }

  /*
   * test executeOverWrite with invalid inputs expected to return an error message
   */
  @Test
  public void testexecuteOverWrite1() {
    String[] args1 = { "\"valid str\"", ">" };
    String[] args2 = { "\"valid str\"", ">", "file1", "extra input" };
    String[] args3 = { "invalid str", ">", "file1" };
    String[] args4 = { "\"valid str\"", ">>", "file1" };
    String output1 = of.executeOverWrite(args1);
    String output2 = of.executeOverWrite(args2);
    String output3 = of.executeOverWrite(args3);
    String output4 = of.executeOverWrite(args4);
    assertEquals("invalid input", output1);
    assertEquals("invalid input", output2);
    assertEquals("invalid input", output3);
    assertEquals("invalid input", output4);
  }

  /*
   * test executeOverWrite with invalid file Name expected to return an error message
   */
  @Test
  public void testexecuteOverWrite2() {
    fs.createDirectoryCurrentDirectory("A");
    String[] args1 = { "\"valid str\"", ">", "file1*" };
    String[] args2 = { "\"valid str\"", ">", "A/file1*" };
    String output1 = of.executeOverWrite(args1);
    String output2 = of.executeOverWrite(args2);
    assertEquals("invalid file name", output1);
    assertEquals("invalid file name", output2);
  }

  /*
   * test executeOverWrite with duplicated directory Name expected to return an error
   * message
   */
  @Test
  public void testexecuteOverWrite3() {
    String[] args = { "\"valid str\"", ">", "A" };
    String output = of.executeOverWrite(args);
    assertEquals("duplicated directory name exists", output);
  }

  /*
   * test executeOverWrite with invalid path expected to return an error message
   */
  @Test
  public void testexecuteOverWrite4() {
    String[] args = { "\"valid str\"", ">", "someInvalidpath/file" };
    String output = of.executeOverWrite(args);
    assertEquals("invalid path", output);
  }

  /*
   * test executeOverWrite with valid input and new file expected to return an empty
   * string, create the file with given contents
   */
  @Test
  public void testexecuteOverWrite5() {
    String[] args1 = { "\"file1: contents\"", ">", "file1" };
    String[] args2 = { "\"file2: contents\"", ">", "A/file2" };
    String output1 = of.executeOverWrite(args1);
    String output2 = of.executeOverWrite(args2);
    File file1 = (File) fs.checkPath("file1", "file");
    String contentsOfFile1 = file1.getContent();
    File file2 = (File) fs.checkPath("A/file2", "file");
    String contentsOfFile2 = file2.getContent();
    assertEquals("", output1);
    assertEquals("", output2);
    assertEquals("file1: contents", contentsOfFile1);
    assertEquals("file2: contents", contentsOfFile2);
  }

  /*
   * test executeOverWrite with valid input and existing file expected to return an
   * empty string and append the existing file with given contents
   */
  @Test
  public void testexecuteOverWrite6() {
    String[] args1 = { "\"overwrite file1\"", ">", "file1" };
    String[] args2 = { "\"overwrite file2\"", ">", "A/file2" };
    String output1 = of.executeOverWrite(args1);
    String output2 = of.executeOverWrite(args2);
    File file1 = (File) fs.checkPath("file1", "file");
    String contentsOfFile1 = file1.getContent();
    File file2 = (File) fs.checkPath("A/file2", "file");
    String contentsOfFile2 = file2.getContent();
    assertEquals("", output1);
    assertEquals("", output2);
    assertEquals("overwrite file1", contentsOfFile1);
    assertEquals("overwrite file2", contentsOfFile2);
  }
}
