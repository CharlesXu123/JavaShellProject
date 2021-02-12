package commands;

import filesystem.FileSystem;


/**
 * This class handles command: pwd.
 * 
 * @author tanluowe
 *
 */
public class PrintWorkingDirectory extends UnitCommand {

  /**
   * Return documentation for PrintWorkingDirectory Command.
   * 
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for PrintWorkingDirectory (pwd) >";
    doc += "\nUsage: pwd";
    doc += "\nPrint the current working directory (including the whole path).";
    return doc;
  }

  /**
   * Return an absolute path of current working directory.
   * 
   * @return String full path of current working directory
   */
  public String getCurrentPath() {
    return FileSystem.findPath(fs.getCurrentDirectory());
  }

  /**
   * Execute command: pwd.
   * 
   * @param args User input
   */
  @Override
  public void executeCommand(String[] args) {

    if (args.length != 0) {
      sendErrMsg("Invalid input");
      return;
    }
    sendOutput(this.getCurrentPath());
  }

}
