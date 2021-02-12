package commands;

import filesystem.Directory;

/**
 * This class handles command: cd DIR.
 * 
 * @author tanluowe
 *
 */
public class ChangeDirectory extends UnitCommand {

  /**
   * Return documentation for ChangeDirectory Command.
   * 
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for ChangeDirectory (cd) >";
    doc += "\nUsage: cd DIR ";
    doc += "\nChange directory to DIR,";
    doc += "\nwhich may be relative to the current ";
    doc += "directory or may be a full path.";
    doc += "\n.. means a parent directory and . means the current directory. ";
    return doc;
  }

  /**
   * Set CurrentDirectory to the path if it is a valid path. Otherwise,
   *  send a error message to ErrorHandler
   * 
   * @param path path of the directory to change
   */
  private void changeDirectoryWithPath(String path) {
    Directory newDir = (Directory) fs.checkPath(path, "directory");
    if (newDir != null) {
      fs.setCurrentDirectory(newDir);
    } else {
      sendErrMsg("Invalid path");
    }
  }

  /**
   * Set currentDirectory to the parent directory.
   */
  private void changeDirectoryToParent() {
    if (fs.getCurrentDirectory().getParentDirectory() != null) {
      fs.setCurrentDirectory((fs.getCurrentDirectory().getParentDirectory()));
    }
  }

  /**
   * Execute command: cd DIR.
   * 
   * @param args User input
   */
  @Override
  public void executeCommand(String[] args) {

    if (args.length > 1) {
      sendErrMsg("Invalid number of arguments");
      return;
    }
    if (args.length == 0) {
      assert true;
    } else if (args[0].equals("..")) {
      changeDirectoryToParent();
    } else if (args[0].equals("/")) {
      fs.setCurrentDirectory(fs.getRoot());
    } else {
      changeDirectoryWithPath(args[0]);
    }
  }

}
