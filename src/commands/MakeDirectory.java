package commands;

import filesystem.Directory;
import java.util.Arrays;

/**
 * Implementation of mkdir command in JShell.
 *
 * @author guirgisj
 * @author kanghung
 */
public class MakeDirectory extends UnitCommand {

  /**
   * Return the documentation for mkdir command.
   *
   * @return String stored documentation
   */
  @Override
  public String toString() {
    // not the best wording tbh
    // please feel free to change this if this isn't clear enough
    String doc = "< Documentation for MakeDirectory (mkdir) >";
    doc += "\nUsage: mkdir DIR1 DIR2";
    doc += "\nCreates two new directories.";
    doc += "\nIf DIR1 can't be created, DIR2 won't be created.";
    return doc;
  }

  /**
   * Creates two directories inside the two given paths if they're valid.
   *
   * @param args String array of size two, contains two paths provided by the
   *             user
   */
  @Override
  public void executeCommand(String[] args) {
    if (args.length == 0) {
      sendErrMsg("No argument provided");
      return;
    }
    // simply loop through each arg
    for (String eachDir : args) {
      if ((fs.checkPath(eachDir, "directory") != null)
          || fs.checkPath(eachDir, "file") != null) {
        sendErrMsg(eachDir + " already exists");
        break;
      } else if (fs.checkPath(fs.getParent(eachDir)[0], "directory") == null) {
        // parent dir of directory to be created dne
        sendErrMsg(eachDir + " is invalid path");
        break;
      }
      makeDirectory(eachDir);
    }
  }

  /**
   * Creates a directory at given path, dir, if it is a valid path.
   *
   * @param dir path for a directory to create (does not exist yet)
   */
  private void makeDirectory(String dir) {

    String[] path = dir.split("/");
    String newPath = "";
    // get the path without the new directory
    if (path[0].equals("")) {
      path = Arrays.copyOfRange(path, 1, path.length);
    }
    for (int i = 0; i < path.length - 1; i++) {
      newPath += path[i] + "/";
    }
    if (!fs.isValidNameForFileSystem(path[path.length - 1])) {
      sendErrMsg("\'" + dir + "\'" + " contains illegal character");
      return;
    }
    Directory parent;
    if (path.length > 1) {
      parent = (Directory) fs.checkPath(newPath, "directory");
    } else {
      parent = fs.getCurrentDirectory();
    }
    if (parent != null) {
      fs.createDirectoryWithParent(path[path.length - 1], parent);
    } else {
      sendErrMsg(newPath + " is invalid path");
    }
  }
}
