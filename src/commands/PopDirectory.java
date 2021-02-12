package commands;

import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystem;

/**
 * Implementation of popd command in JShell.
 *
 * @author guirgisj
 * @author kanghung
 */
public class PopDirectory extends UnitCommand {

  /**
   * Return documentation for popd command.
   *
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for PopDirectory (popd) >";
    doc += "\nUsage: popd";
    doc += "\nChange current working directory back to";
    doc += " the most-recently saved directory.";
    return doc;
  }

  /**
   * Change current working directory to the top-most directory saved in
   * DirectoryStack if DirectoryStack is not empty.
   *
   * @param args an empty array of string with length 0
   */
  @Override
  public void executeCommand(String[] args) {
    if (args.length != 0) {
      sendErrMsg("Invalid number of arguments");
      return;
    }
    if (!(DirectoryStack.stack.isEmpty())) {
      // check if top-most dir in stack is valid (not removed)
      Directory topMost = DirectoryStack.stack.pop();
      String topMostPath = FileSystem.findPath(topMost);
      if (fs.checkPath(topMostPath, "directory") != null) {
        fs.setCurrentDirectory(topMost);
      } else {
        sendErrMsg("Could not find \'" + topMostPath + "\'");
      }

      // fs.setCurrentDirectory(DirectoryStack.stack.pop());
      return;
    }
    sendErrMsg("No directories on stack");
  }

}
