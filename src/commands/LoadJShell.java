package commands;

import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.File;
import filesystem.HistoryArray;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This method response for command: loadJShell PATH.
 * 
 * @author xushengsong
 *
 */
public class LoadJShell extends UnitCommand {
  /**
   * HistoryArray instance.
   */
  private HistoryArray ha;
  /**
   * java.io.File instance.
   */
  private java.io.File jfile;

  /**
   * Returns documentation for search command.
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for Search (search) >");
    doc.append("\nUsage: search PATH [PATH ...] -type [f|d] -name EXPRESSION");
    doc.append("\nRecursively search for the name given by EXPRESSION");
    doc.append(" (must be wrapped in double quotes)");
    doc.append(" inside given path(s) that matches given type");
    doc.append(" (f: file, d: directory)");
    return doc.toString();
  }

  /**
   * LoadJShell constructor.
   */
  public LoadJShell() {
    this.ha = HistoryArray.createHistoryArray();
  }

  /**
   * This method check if JShell have been modified.
   * 
   * @param historyLs
   *          list that store all the history
   * @return True if JShell have not been modified, false otherwise
   */
  private boolean ifmodifiedJShell(ArrayList<String> historyLs) {
    if (historyLs.size() != 1) {
      return false;
    }
    if (!historyLs.get(0).contains("loadJShell")) {
      return false;
    }
    return true;
  }

  /**
   * This method reinitialize directory stack with savedJShell file.
   * 
   * @param dirStack
   *          part of savedJShell file that stores string for directory stack
   */
  private void loadDirectoryStack(String dirStack) {
    if (dirStack.equals("DirectoryStack empty")) {
      return;
    }
    String[] dirStackE = dirStack.split("\n");
    for (String e : dirStackE) {
      DirectoryStack.stack.push((Directory) fs.checkPath(e, "directory"));
    }
  }

  /**
   * This method reinitialize history array with savedJShell file.
   * 
   * @param history
   *          part of savedJShell file that stores string for history array
   */
  private void loadHistory(String history) {
    if (history.equals("History empty")) {
      return;
    }
    ArrayList<String> loadJShellHs = ha.getHistoryLs();
    String loadHs = loadJShellHs.get(0);
    ha.cleanHistoryArray();
    String[] historyE = history.split("\n");
    for (String e : historyE) {
      ha.addHistory(e.substring(3));
    }
    ha.addHistory(loadHs);
  }

  /**
   * Helper method that create directory dir under parentDir.
   * 
   * @param parentDir
   *          parent directory
   * @param dir
   *          directory's name
   */
  private void constructDir(String parentDir, String dir) {
    fs.createDirectoryWithParent(dir,
        (Directory) fs.checkPath(parentDir, "directory"));
  }

  /**
   * Helper method that create file file under parentDir.
   * 
   * @param parentDir
   *          parent directory
   * @param file
   *          file's name
   */
  private void constructFile(String parentDir, String file) {
    String[] fileE = file.split("\"");
    File f = fs.createFileWithParent(fileE[0],
        (Directory) fs.checkPath(parentDir, "directory"));
    if (fileE.length == 1) {
      f.appendContent("");
    } else {
      f.appendContent(fileE[1]);
    }
  }

  /**
   * This method reinitialize file system with savedJShell file.
   * 
   * @param fileSystem
   *          part of savedJShell file that stores string for file system
   */
  private void loadFileSystem(String fileSystem) {
    if (fileSystem.equals("/:")) {
      return;
    }
    String[] fs = fileSystem.split("\n");
    String parentDir = "/";
    for (String i : fs) {
      if (i.endsWith(":")) {
        parentDir = i.substring(0, i.length() - 1);
      } else if (i.startsWith("\"")) {
        constructFile(parentDir, i.substring(1));
      } else if (!i.equals("")) {
        constructDir(parentDir, i);
      }
    }
  }

  /**
   * This is a helper method for executeCommand.
   * 
   * @param args
   *          input arguments
   * @return Null if args is valid, an error message otherwise
   */
  public String executeLoadJShell(String[] args) {
    if (!ifmodifiedJShell(ha.getHistoryLs())) {
      return ("modified this JShell already");
    } else if (args.length != 1) {
      return ("invalid number of input");
    } else {
      String contents = loadSession(args[0]);
      if (contents.startsWith("\"")) {
        return contents;
      }
      if (!contents.startsWith("this is a saved JShell")) {
        return ("invalid file contents");
      }
      String[] sessions = contents.split("\nnext.\n");
      if (sessions.length != 5) {
        return ("invalid file contents");
      }
      loadFileSystem(sessions[1]);
      loadHistory(sessions[2]);
      loadDirectoryStack(sessions[3]);
      String currentDir = sessions[4].substring(0, sessions[4].length() - 1);
      fs.setCurrentDirectory((Directory) fs.checkPath(currentDir, "directory"));
    }
    return null;
  }

  /**
   * This method read the file in location.
   * 
   * @param location
   *          location of savedJShell file
   * @return Contents of savedJShell file
   */
  private String loadSession(String location) {
    Scanner reader;
    try {
      String contents = "";
      jfile = new java.io.File(location);
      reader = new Scanner(jfile);
      while (reader.hasNextLine()) {
        String content = reader.nextLine();
        contents = contents + content + "\n";
      }
      reader.close();
      return contents;
    } catch (FileNotFoundException e) {
      return ("\"File Not Found");
    }
  }

  /**
   * This method execute command: loadJShell PATH.
   * 
   * @param args
   *          input arguments
   *
   */
  public void executeCommand(String[] args) {
    String msg = this.executeLoadJShell(args);
    if (msg != null) {
      sendErrMsg(msg);
    }
  }

}
