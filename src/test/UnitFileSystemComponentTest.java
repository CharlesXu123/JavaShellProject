package test;

import static org.junit.Assert.assertEquals;

import filesystem.Directory;
import filesystem.UnitFileSystemComponent;
import org.junit.Before;
import org.junit.Test;

public class UnitFileSystemComponentTest {

  UnitFileSystemComponent ufsc;
  String name;
  String type;
  Directory parentDirectory;


  /**
   * set up.
   */
  @Before
  public void setUp() {
    ufsc = new UnitFileSystemComponent();
    name = "";
    type = "";
    parentDirectory = new Directory("name", null);
  }

  // Test for setName
  @Test
  public void setNameTest() {
    ufsc.setName("111");
    name = "111";
    assertEquals(name, ufsc.getName());
  }

  // Test for setType
  @Test
  public void setTypeTest() {
    ufsc.setType("file");
    type = "file";
    assertEquals(type, ufsc.getType());
  }

  // Test for setParentDirectory
  @Test
  public void setParentDirectoryTest() {
    ufsc.setParentDirectory(parentDirectory);
    assertEquals(parentDirectory, ufsc.getParentDirectory());
  }


}
