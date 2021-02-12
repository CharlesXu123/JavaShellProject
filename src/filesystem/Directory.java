package filesystem;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Directory class extends UnitFileSystemComponent with a ArrayList of UnitFileSystemComponents.
 * 
 * @author tanluowe
 * 
 */

public class Directory extends UnitFileSystemComponent {

  protected ArrayList<UnitFileSystemComponent> components;

  /**
   * Constructor of a Directory with name and parentDirectory.
   * 
   * @param n name to set
   * 
   * @param parentDirectory parentDirectory to set
   */
  public Directory(String n, Directory parentDirectory) {
    this.name = n;
    this.type = "directory";
    this.parentDirectory = parentDirectory;
    this.components = new ArrayList<>();
  }

  /**
   * Return ArrayList of directory and file names inside the given directory.
   * 
   * @return nameLs contains names of sub-directories and files inside dir.
   */
  public ArrayList<String> lsDir() {
    ArrayList<String> nameLs = new ArrayList<String>();
    for (int i = 0; i < this.components.size(); i++) {
      nameLs.add(this.components.get(i).getName());
    }
    return nameLs;
  }


  /**
   * Getter of the component of a directory.
   * 
   * @return the component of this directory
   */
  public ArrayList<UnitFileSystemComponent> getComponents() {
    return this.components;
  }

  /**
   * Remove a component of this directory.
   * 
   * @param name name of the component
   */
  public void removeComponent(String name) {
    Iterator<UnitFileSystemComponent> it = components.iterator();
    while (it.hasNext()) {
      UnitFileSystemComponent i = it.next();
      if (i.getName().equals(name)) {
        it.remove();
        break;
      }
    }
  }

  /**
   * Return true if this directory contain the component.
   * 
   * @param name name of the component
   * @return true or false
   */
  public boolean containComponent(String name) {
    Iterator<UnitFileSystemComponent> it = components.iterator();
    while (it.hasNext()) {
      UnitFileSystemComponent i = it.next();
      if (i.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

}

