package commands;

import filesystem.Directory;
import filesystem.File;
import filesystem.UnitFileSystemComponent;
import java.util.ArrayList;

/**
 * Implementation of search command.
 *
 * @author kanghung
 */
public class Search extends UnitCommand {

  /**
   * String literal of desired type.
   */
  private String srchType;

  /**
   * String array of paths to search.
   */
  private ArrayList<String> srchPaths;

  /**
   * String of name that Search is searching for.
   */
  private String srchName;

  /**
   * Dir to start searching recursively.
   */
  private Directory srchStartingDir;

  /**
   * Number of times search found a match.
   */
  private int srchCount;

  /**
   * Constructor to initialize searchPaths.
   */
  public Search() {
    srchPaths = new ArrayList<>();
    srchName = null;
    srchType = null;
    srchStartingDir = null;
    srchCount = 0;
  }

  /**
   * String literal for dir type.
   */
  private static final String DIR_LIT = "directory";

  /**
   * String literal for file type.
   */
  private static final String FILE_LIT = "file";

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
    doc.append("This will output existing paths of EXPRESSION if found.");
    doc.append("If search failed, will output as such.");
    return doc.toString();
  }

  /**
   * Runs search from JShell.
   *
   * @param args Arguments from user
   */
  @Override
  public void executeCommand(String[] args) {
    // reset?
    this.srchName = null;
    this.srchPaths.clear();
    this.srchType = null;
    String output = run(args);
    sendOutput(output);
  }

  /**
   * A helper method to run Search and return the output as a string.
   * 33 lines of code: 2 empty lines + 4 lines with comments.
   *
   * @param args User-provided arguments
   * @return output string
   */
  public String run(String[] args) {
    if (!setParameters(args)) {
      sendErrMsg("Invalid input");
      return null;
    }
    if (this.srchType.equals("d")) {
      this.srchType = DIR_LIT;
    } else if (this.srchType.equals("f")) {
      this.srchType = FILE_LIT;
    }

    if (!this.srchName.equals("/") && this.srchName.contains("/")) {
      // check name: must not be a path: Post @715
      sendErrMsg(this.srchName + " is invalid name expression");
      return null;
    }
    String output = "";
    for (String each : this.srchPaths) {
      Directory startDir = (Directory) fs.checkPath(each, DIR_LIT);
      if (startDir == null) {
        // don't search inside non-existent directories
        sendErrMsg("\'" + each + "\' is invalid path");
        return null;
      }
      // set srchStartingDir before each initial call of searchHelper()
      srchStartingDir = startDir;

      output = searchHelper(0, startDir, each);
    }
    if (srchCount == 0) {
      // no match found after recursive search
      return "Could not find " + "\'" + srchName + "\'\n";
    }
    return output;
  }

  /**
   * A helper method to search for a File/Dir node with given name and type
   * inside dir and sub-dirs of given dir.
   *
   * @param depth   Int value indicating depth of recursion
   * @param dir     A directory node to search
   * @param dirPath String representation of path of node dir
   * @return String Output of search command in string
   */
  private String searchHelper(int depth, Directory dir, String dirPath) {
    String helperOutput = "";

    // Base cases:
    if (dir.getComponents().isEmpty()
        || dir.getComponents().size() == getFiles(dir).size()) {
      return helperOutput;
    }

    // Recursive case: go to each sub dir
    for (UnitFileSystemComponent each : dir.getComponents()) {
      if (each.getType().equals(DIR_LIT)) {
        // each is a sub-dir, don't output 'not found' msg for recursive calls
        String relPath = getRelPath((Directory) each);
        // put srchStartingDir.name in front of relPath plz
        relPath = addPaths(srchStartingDir.getName(), relPath);
        helperOutput += searchHelper(depth + 1, (Directory) each, relPath);
      }
    }

    // Base cases:
    if (dir.containComponent(this.srchName)) {
      // match found
      srchCount++;
      helperOutput += addPaths(dirPath, this.srchName);
      return helperOutput + "\n";
    }

    return helperOutput;
  }

  /**
   * A helper method to process user arguments and set search parameters.
   *
   * @param args User-provided arguments
   * @return true iff success
   */
  private boolean setParameters(String[] args) {
    int i;
    boolean isPath = true;
    for (i = 0; i < args.length; i++) {
      String each = args[i];
      String nextEach = null;
      if (i + 1 < args.length) {
        nextEach = args[i + 1];
      }

      if (isPath) {
        if (each.equals("-type")) {
          isPath = false;
        } else {
          this.srchPaths.add(each);
        }
      }

      if (each.equals("-type")) {
        // type
        this.srchType = nextEach;
      } else if (each.equals("-name") && nextEach != null
          && nextEach.contains("\"")) {
        // name
        this.srchName = nextEach.substring(1, nextEach.length() - 1);
      }
    }
    return !(srchName == null || srchPaths.isEmpty() || srchType == null);
  }


  /**
   * A helper method to get path representation of a directory node. Returned
   * string is a path relative to the original calling func.
   *
   * @param dir A directory node to get path string
   * @return String path representation of given dir node
   */
  private String getRelPath(Directory dir) {
    String ret = "";
    Directory tmpTraverse = dir;
    // pointing to currDir
    // while (tmpTraverse != fs.getCurrentDirectory()) {
    while (tmpTraverse != srchStartingDir) {
      if (ret.equals("")) {
        // fist iteration
        ret = tmpTraverse.getName();
      } else {
        ret = tmpTraverse.getName() + "/" + ret;
      }
      tmpTraverse = tmpTraverse.getParentDirectory();
    }

    // if (srchStartingDir.getName().equals("/")) {
    // ret = "/" + ret;
    // } else {
    // ret = srchStartingDir.getName() + "/" + ret;
    // }
    return ret;
  }

  /**
   * A helper method that returns an ArrayList of files inside directory dir.
   *
   * @param dir Parent directory to get list of files from
   * @return ArrayList of all file nodes inside dir node
   */
  private static ArrayList<File> getFiles(Directory dir) {
    ArrayList<File> ret = new ArrayList<>();
    for (UnitFileSystemComponent each : dir.getComponents()) {
      if (each.getType().equals(FILE_LIT)) {
        ret.add((File) each);
      }
    }
    return ret;
  }

  /**
   * A helper method to concatenate two path string such that there's only one
   * "/" between two joined path strings.
   *
   * @param path1 First path string
   * @param path2 Second path string
   * @return String after concatenating path1 and path2
   */
  private static String addPaths(String path1, String path2) {
    String ret = path1;
    if (path1.endsWith("/")) {
      ret += path2;
    } else {
      ret += "/" + path2;
    }
    return ret;
  }
}
