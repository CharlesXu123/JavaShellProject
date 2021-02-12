package commands;


import filesystem.FileSystem;
import util.ErrorHandler;
import util.OutputHandler;


/**
 * Parent class of all individual commands. Allow the commands to send
 * errors/outputs and access the mock file system more easily.
 *
 * @author kanghung
 */

public abstract class UnitCommand {

  /**
   * JShell's mock FileSystem instance.
   */
  protected FileSystem fs = FileSystem.createFileSystem();

  /**
   * A flag to identify redirection usage.
   * <p>
   * "" indicates no redirection.
   * </p>
   * <p>
   * ">" indicates overwrite redirection case.
   * </p>
   * <p>
   * ">>" indicates append redirection case.
   * </p>
   */
  private String redirectionFlag = "";

  public void setRedirectionFlag(String redirectionFlag) {
    this.redirectionFlag = redirectionFlag;
  }

  /**
   * Variable to hold file name of OUTFILE for redirection.
   */
  private String outFilePath = "";

  public void setOutFilePath(String outFilePath) {
    this.outFilePath = outFilePath;
  }

  /**
   * Piazza post @470. Refer to ErrorHandler.getErrorMessage for its JavaDoc.
   *
   * @see util.ErrorHandler#getErrorMessage(String)
   */
  public static void sendErrMsg(String msg) {
    ErrorHandler.getErrorMessage(msg);
  }

  /**
   * Sends output string to a selected stream, STDOUT, or to a file.
   *
   * @param output String to output
   */
  public void sendOutput(String output) {
    String flag = this.redirectionFlag;
    if (output != null) {
      if (flag.equals(">")) {
        // overwrite redirection case
        OutputHandler.overWriteFile(output, this.outFilePath, this.fs);
      } else if (flag.equals(">>")) {
        // append redirection case
        OutputHandler.appendFile(output, this.outFilePath, this.fs);
      } else {
        // no redirection
        OutputHandler.sendToStdOut(output);
      }
    }
  }

  /**
   * Implement this method in each command class. CommandExecution will call this
   * method.
   *
   * @param args array of arguments in the format of: {"argument_1", ...}
   */
  public abstract void executeCommand(String[] args);

}
