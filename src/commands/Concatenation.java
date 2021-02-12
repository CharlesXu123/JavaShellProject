package commands;

import filesystem.File;

/**
 * This class handles command: cat File1 [File2 ...]
 *
 * @author tanluowe
 */

public class Concatenation extends UnitCommand {

  /**
   * Return documentation for Concatenation Command.
   *
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for Concatenation (cat) >";
    doc += "\nUsage: cat FILE1 [FILE2 ...]";
    doc += "\nDisplay the contents of FILE1 and other FILEs";
    return doc;
  }

  /**
   * Update status array if the path is valid. Otherwise, send a error message to ErrorHandler, and
   * set status array to "break".
   *
   * @param path path of the file that has the content to print
   * @param status status array to update
   * @return a array contents the content and status of break
   */
  public String[] printContent(String path, String[] status) {
    File newFile = (File) fs.checkPath(path, "file");
    if (newFile != null) {
      status[0] = newFile.getContent();
      status[1] = "safe";
    } else {
      sendErrMsg(path + " is not a file that exist");
      status[1] = "break";
    }
    return status;
  }

  /**
   * A helper method to return a content of file in String.
   *
   * @param args Arguments provided by the user
   * @return String of output sent to OutputHandler
   */
  public String run(String[] args) {
    String[] status = {"", ""};
    String output;
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < args.length; i++) {
      status = printContent(args[i], status);

      if (status[1].equals("break")) {
        break;
      } else {
        if (i > 0) {
          sb.append("\n\n\n\n");
        }
        sb.append(status[0]);
      }
    }
    output = sb.toString();
    if (output.equals("")) {
      output = null;
    }

    return output;

  }

  /**
   * Execute command: cat File1 [File2 ...]
   *
   * @param args User input
   */
  @Override
  public void executeCommand(String[] args) {
    // check if there's no argument from the user
    if (args.length == 0) {
      sendErrMsg("No files provided");
      return;
    }
    String output = this.run(args);
    sendOutput(output);
  }
}

