package commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import filesystem.DirectoryStack;
import filesystem.FileSystem;

/**
 * This class responsible for command: saveJShell PATH.
 * 
 * @author xushengsong
 *
 */
public class SaveJShell extends UnitCommand {
  /**
   * List instance.
   */
  private List ls;
  /**
   * History instance.
   */
  private History hs;

  /**
   * Return the stored doc of SaveJShell command.
   *
   * @return String the stored doc
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for SaveJShell (saveJShell) >");
    doc.append("\nUsage: saveJShell FileName");
    doc.append(
        "\nSave the entire state of current JShell session into FileName");
    doc.append(" on the actual file system on your computer.");
    return doc.toString();
  }

  /**
   * SaveJShell constructor.
   */
  public SaveJShell() {
    this.ls = new List();
    this.hs = new History();
  }

  /**
   * This method generates string representation of the directory stack.
   * 
   * @return String representation of the directory stack
   */
  private String saveDirectoryStack() {
    String dsStr = DirectoryStack.toStrDirectoryStack();
    if (dsStr.equals("")) {
      return "DirectoryStack empty\n";
    }
    return dsStr;
  }

  /**
   * This method generates the string representation of the history array.
   * 
   * @return String representation of history array
   */
  private String saveHistory() {
    String[] args = new String[0];
    String hsStr = hs.executeHistory(args);
    if (hsStr.equals("")) {
      return "History empty";
    }
    return hsStr;
  }

  /**
   * This method generate the string representation of the fileSystem.
   * 
   * @return String representation of the fileSystem.
   */
  private String saveFileSystem() {
    String fsStr = ls.recurList("/", fs.getRoot(), true);
    if (fsStr.endsWith("\n")) {
      fsStr = fsStr.substring(0, fsStr.length() - 1);
    }
    return fsStr;
  }

  /**
   * This method generates the string representation of JShell.
   * 
   * @param args
   *          input arguments
   * @return String representation of JShell if args is valid, an error message
   *         otherwise
   */
  private String getSaveJShellFile(String[] args) {
    if (args.length != 1) {
      return ("\"invalid number of input");
    } else {
      String output = "this is a saved JShell\nnext.\n";
      output += saveFileSystem();
      output += "next.\n";
      output += saveHistory();
      output += "\nnext.\n";
      output += saveDirectoryStack();
      output += "next.\n";
      output += FileSystem.findPath(fs.getCurrentDirectory());
      return output;
    }
  }

  /**
   * This method save session to file with path location.
   * 
   * @param session
   *          contents for saving
   * @param location
   *          location of the file
   * @return An error message if unable to save session, null otherwise
   */
  private String saveSession(String session, String location) {
    File saveJshell = new File(location);
    try {
      saveJshell.createNewFile();
      FileWriter writer = new FileWriter(location, false);
      writer.write(session);
      writer.close();
      return null;
    } catch (IOException e) {
      return "unable to create file under given path";
    }
  }

  /**
   * This is a helper method for executeCommand.
   * 
   * @param args
   *          input arguments
   * @return An error message if args is invalid, null otherwise
   */
  public String executeSaveJShell(String[] args) {
    String msg = this.getSaveJShellFile(args);
    if (msg.startsWith("\"")) {
      return (msg.substring(1));
    } else {
      String output = this.saveSession(msg, args[0]);
      return output;
    }
  }

  /**
   * This method execute command: saveJShell PATH.
   * 
   * @param args
   *          input arguments
   */
  public void executeCommand(String[] args) {
    String msg = executeSaveJShell(args);
    if (msg != null) {
      sendErrMsg(msg);
    }
  }

}
