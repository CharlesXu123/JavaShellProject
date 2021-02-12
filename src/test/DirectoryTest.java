package test;

import static org.junit.Assert.assertEquals;

import filesystem.Directory;
import filesystem.UnitFileSystemComponent;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class DirectoryTest {

  Directory dir;
  String name;
  UnitFileSystemComponent com1;
  UnitFileSystemComponent com2;
  UnitFileSystemComponent com3;

  /**
   * set up.
   */
  @Before
  public void setUp() {
    name = "dirName";
    dir = new Directory(name, null);
    com1 = new UnitFileSystemComponent();
    com2 = new UnitFileSystemComponent();
    com3 = new UnitFileSystemComponent();

  }

  // Test for lsDir with 0 component
  @Test
  public void lsDirTest1() {
    ArrayList<String> nameLs = new ArrayList<String>();
    assertEquals(nameLs, dir.lsDir());
  }

  // Test for lsDir with 3 components
  @Test
  public void lsDirTest2() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");

    ArrayList<String> nameLs = new ArrayList<String>();
    nameLs.add("name1");
    nameLs.add("name2");
    nameLs.add("name3");

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    assertEquals(nameLs, dir.lsDir());

  }

  // remove 1 component
  @Test
  public void removeComponentTest1() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");

    ArrayList<UnitFileSystemComponent> testLs = new ArrayList<UnitFileSystemComponent>();
    testLs.add(com2);
    testLs.add(com3);

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    dir.removeComponent("name1");

    assertEquals(testLs, dir.getComponents());
  }

  // remove all component
  @Test
  public void removeComponentTest2() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    dir.removeComponent("name1");
    dir.removeComponent("name2");
    dir.removeComponent("name3");
    ArrayList<UnitFileSystemComponent> testLs = new ArrayList<UnitFileSystemComponent>();

    assertEquals(testLs, dir.getComponents());
  }

  // Contain the component
  @Test
  public void containComponentTest1() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    assertEquals(true, dir.containComponent("name1"));
  }

  // Does not contain the component
  @Test
  public void containComponentTest2() {
    com1.setName("name1");
    com2.setName("name2");
    com3.setName("name3");

    dir.getComponents().add(com1);
    dir.getComponents().add(com2);
    dir.getComponents().add(com3);

    assertEquals(false, dir.containComponent("name5"));
  }
}
