package test;

import static org.junit.Assert.assertEquals;

import commands.Remove;
import filesystem.Directory;
import filesystem.FileSystem;
import filesystem.UnitFileSystemComponent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoveTest {

  String path;
  FileSystem fs;
  Remove rm;
  Directory dir;
  UnitFileSystemComponent com1;
  UnitFileSystemComponent com2;
  UnitFileSystemComponent com3;


  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
    rm = new Remove();
    dir = new Directory("", null);
    com1 = new UnitFileSystemComponent();
    com2 = new UnitFileSystemComponent();
    com3 = new UnitFileSystemComponent();
    path = "";
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

  // remove 1 component
  @Test
  public void removeComponentsTest1() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");
    com1.setParentDirectory(dir);
    com2.setParentDirectory(dir);
    com3.setParentDirectory(dir);

    ArrayList<UnitFileSystemComponent> testLs = new ArrayList<UnitFileSystemComponent>();
    testLs.add(com2);
    testLs.add(com3);

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    rm.removeComponents(com1);


    assertEquals(testLs, dir.getComponents());
  }

  // remove all component
  @Test
  public void removeComponentsTest2() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");
    com1.setParentDirectory(dir);
    com2.setParentDirectory(dir);
    com3.setParentDirectory(dir);

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    rm.removeComponents(com1);
    rm.removeComponents(com2);
    rm.removeComponents(com3);
    ArrayList<UnitFileSystemComponent> testLs = new ArrayList<UnitFileSystemComponent>();
    assertEquals(testLs, dir.getComponents());
  }

  /**
   * test run, invalid path.
   */
  @Test
  public void runTest1() {
    path = "asasasas";
    String[] args = {path};
    String expected = (args[0] + "is not a directory that exist ");

    assertEquals(expected, rm.run(args));
  }

  /**
   * test run, remove root.
   */
  @Test
  public void runTest2() {
    path = "/";
    String[] args = {path};
    String expected = "Root can not be removed";

    assertEquals(expected, rm.run(args));

  }

  /**
   * test run, invalid number of arguments.
   */
  @Test
  public void runTest3() {
    String[] args = {"", ""};
    String expected = "Invalid number of arguments";

    assertEquals(expected, rm.run(args));
  }

  /**
   * test run, remove current directory.
   */
  @Test
  public void runTest4() {
    fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    fs.setCurrentDirectory(dir);
    path = ".";
    String[] args = {path};
    String expected = "CurrentDirectory can not be removed";

    assertEquals(expected, rm.run(args));

  }
}

