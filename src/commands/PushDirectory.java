package commands;

import filesystem.Directory;
import filesystem.DirectoryStack;

/**
 * Implementation of pushd command for JShell.
 *
 * @author guirgisj
 * @author kanghung
 */
public class PushDirectory extends UnitCommand {

  /**
   * Return documentation for pushd command.
   *
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for PushDirectory (pushd) >";
    doc += "\nUsage: pushd DIR";
    doc +=
        "\nSaves current working directory before changing into new directory DIR.";
    doc += "\nSaved directories can be restored by popd command.";
    return doc;
  }

  /**
   * Push the current directory into DirectoryStack and change into new given
   * directory if given directory is valid.
   *
   * @param args array of string with length of one, containing a valid path to
   *             change into
   */
  @Override
  public void executeCommand(String[] args) {
    if (args.length != 1) {
      sendErrMsg("Invalid number of arguments");
      return;
    }
    Directory newDir = (Directory) fs.checkPath(args[0], "directory");
    if (newDir == null || newDir.getType().equals("file")) {
      sendErrMsg(args[0] + " is not a valid directory");
      return;
    }
    DirectoryStack.stack.push(fs.getCurrentDirectory());
    fs.setCurrentDirectory(newDir);
  }
}
