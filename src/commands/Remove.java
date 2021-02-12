package commands;

import filesystem.Directory;
import filesystem.UnitFileSystemComponent;

/**
 * This class handles command: rm DIR.
 * 
 * @author tanluowei
 *
 */
public class Remove extends UnitCommand {

  /**
   * Returns documentation for rm command.
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for Remove (rm) >");
    doc.append("\nUsage: rm DIR");
    doc.append("\nRecursively remove the DIR and its");
    doc.append(" children from the fle system.");
    return doc.toString();
  }

  /**
   * Remove a components from its parent directory.
   * 
   * @param remove the component to be removed
   */
  public void removeComponents(UnitFileSystemComponent remove) {
    Directory parentDir = remove.getParentDirectory();
    parentDir.removeComponent(remove.getName());

  }

  /**
   * A helper method to return the error msg.
   *
   * @param args Arguments provided by the user
   * @return error msg or works
   */
  public String run(String[] args) {
    if (args.length != 1) {
      return ("Invalid number of arguments");
    }
    if ((fs.checkPath(args[0], "directory") == null)) {
      return (args[0] + "is not a directory that exist ");

    } else if ((fs.checkPath(args[0], "directory") == fs.getRoot())) {
      return ("Root can not be removed");

    } else if ((fs.checkPath(args[0], "directory") == fs
        .getCurrentDirectory())) {
      return ("CurrentDirectory can not be removed");
    }
    return "works";
  }

  /**
   * Execute command: rm DIR.
   *
   * @param args User input
   */
  public void executeCommand(String[] args) {
    String msg = this.run(args);
    if (msg != "works") {
      sendErrMsg(msg);
      return;
    }
    UnitFileSystemComponent remove = fs.checkPath(args[0], "directory");
    this.removeComponents(remove);

  }

}
