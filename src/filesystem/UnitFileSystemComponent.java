package filesystem;


/**
 * UnitFileSystemComponent class is the unit node of FileSystem with name, type, and
 * parentDirectory.
 * 
 * @author tanluowe
 *
 */

public class UnitFileSystemComponent {

  protected String name;
  protected String type;
  protected Directory parentDirectory;


  /**
   * get the name of a UnitFileSystemComponent.
   * 
   * @return name of the UnitFileSystemComponent
   */
  public String getName() {
    return name;
  }

  /**
   * set the name of a UnitFileSystemComponent.
   * 
   * @param n name to set
   */
  public void setName(String n) {
    name = n;

  }

  /**
   * get the type of a UnitFileSystemComponent.
   * 
   * @return type of the UnitFileSystemComponentf
   */
  public String getType() {
    return type;
  }

  /**
   * set the type of a UnitFileSystemComponent.
   * 
   * @param t type to set
   */
  public void setType(String t) {
    type = t;

  }

  /**
   * get the parentDirectory of a UnitFileSystemComponent.
   * 
   * @return parentDirectory of the UnitFileSystemComponent
   */
  public Directory getParentDirectory() {
    return parentDirectory;
  }

  /**
   * set the parentDirectory of a UnitFileSystemComponent.
   * 
   * @param pd parentDirectory of the UnitFileSystemComponent
   */
  public void setParentDirectory(Directory pd) {
    parentDirectory = pd;

  }
}
