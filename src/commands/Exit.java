package commands;

import driver.JShell;

/**
 * Implementation of exit command in JShell.
 *
 * @author guirgisj
 */
public class Exit extends UnitCommand {


  /**
   * Return the documentation of exit command.
   *
   * @return String stored documentation
   */
  @Override
  public String toString() {
    String doc = "<Documentation for Exit (exit)>";
    doc += "\nUsage: exit";
    doc += "\nSafely close and exit current session.";
    return doc;
  }


  /**
   * Safely exits and ends the current session of JShell.
   *
   * @param args array or user-provided arguments, should be empty
   */
  @Override
  public void executeCommand(String[] args) {
    if (args.length != 0) {
      sendErrMsg("Invalid number of arguments");
      return;
    }
    JShell.scanner.close();
    System.exit(0);
  }

}
