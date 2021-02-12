package test;

import static org.junit.Assert.*;

import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystem;
import org.junit.Before;
import org.junit.Test;

public class DirectoryStackTest {

  FileSystem fs;

  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
  }

  /*
   * test toStrDirectoryStack when directoryStack is empty expected to return an
   * empty string
   */
  @Test
  public void testtoStrDirectoryStack1() {
    String output = DirectoryStack.toStrDirectoryStack();
    assertEquals("", output);
  }

  /*
   * test toStrDirectoryStack when directoryStack contains directories expected
   * to return an string representation of directoryStack
   */
  @Test
  public void testtoStrDirectoryStack2() {
    Directory a = fs.createDirectoryWithParentWithReturn("A",
        fs.getCurrentDirectory());
    Directory b = fs.createDirectoryWithParentWithReturn("B",
        fs.getCurrentDirectory());
    fs.setCurrentDirectory(a);
    Directory c = fs.createDirectoryWithParentWithReturn("C",
        fs.getCurrentDirectory());
    Directory d = fs.createDirectoryWithParentWithReturn("D",
        fs.getCurrentDirectory());
    DirectoryStack.stack.push(a);
    DirectoryStack.stack.push(b);
    DirectoryStack.stack.push(c);
    DirectoryStack.stack.push(d);
    String output = DirectoryStack.toStrDirectoryStack();
    assertEquals("/A\n" + "/B\n" + "/A/C\n" + "/A/D\n", output);
  }

}
