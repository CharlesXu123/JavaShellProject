package commands;

import filesystem.Directory;
import filesystem.File;

/**
 * This class handles command: mv OLDPATH NEWPATH.
 * 
 * @author tanluowe
 *
 */
public class Move extends UnitCommand {
  private Copy cp = new Copy();
  private Remove mv = new Remove();

  /**
   * Return the documentation for mv command.
   *
   * @return String stored documentation
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for Move (mv) >");
    doc.append("\nUsage: mv OLDPATH NEWPATH");
    doc.append("\nMove item OLDPATH to NEWPATH.");
    doc.append(
        " If NEWPATH is a directory, then move th item into the directory.");
    return doc.toString();
  }

  /**
   * Copy a file at oldPath into the directory at newPath, 
   * and remove the old file.
   * 
   * @param oldPath path with file at the end
   * @param newPath path with directory at the end
   */
  public void moveFileIntoDir(String oldPath, String newPath) {
    cp.copyFileIntoDir(oldPath, newPath);
    File file = (File) fs.checkPath(oldPath, "file");
    mv.removeComponents(file);
  }

  /**
   * Copy the content of the file at oldPath to that at newPath, 
   * and remove the old file.
   * 
   * @param oldPath path with a file at the end
   * @param newPath path with a file at the end
   */
  public void moveFileIntoFile(String oldPath, String newPath) {
    cp.copyFile(oldPath, newPath);
    File file = (File) fs.checkPath(oldPath, "file");
    mv.removeComponents(file);
  }

  /**
   * Recursively move a Directory into other Directory, 
   * and remove the old directory.
   * 
   * @param oldDir directory to be moved
   * @param parentDir new parent of the oldDIr
   */
  private void recursiveMoveDir(Directory oldDir, Directory parentDir) {
    cp.recursiveCopyDir(oldDir, parentDir);
    mv.removeComponents(oldDir);
  }

  /**
   * A helper method to return a content of file in String. 
   * It returns works if the file or directory is moved successfully. 
   * It returns a errorMessage if error is cached
   * 
   * @param args user inputs
   * @return a String of works or errorMessage
   */
  public String run(String[] args) {

    if (args.length != 2) {
      return ("Invalid number of arguments");
    } else if ((fs.checkPath(args[0], "directory") == null)
        && fs.checkPath(args[0], "file") == null) {
      return (args[0] + " is not exist ");
    }
    Directory oldDir = (Directory) fs.checkPath(args[0], "directory");
    Directory newDir = (Directory) fs.checkPath(args[1], "directory");
    // Copy directory.
    if (oldDir != null && newDir != null) {
      if (oldDir.equals(newDir)) {
        return "works";
      }
      if (newDir.containComponent(oldDir.getName())) {
        return (oldDir.getName() + " has already exist");
      }
      if (cp.isParentDir(oldDir, newDir) || oldDir.equals(fs.getRoot())) {
        return ("Can not move a parent directory into its children directory");
      }
      this.recursiveMoveDir(oldDir, newDir);
      // Copy file.
    } else if (oldDir != null) {
      if (!(args[1].endsWith("/"))) {
        return ("Can not move a directory to a file");
      } else {
        String[] ppath = fs.getParent(args[1]);
        oldDir.setName(ppath[1]);
      }
    } else if (newDir != null) {
      this.moveFileIntoDir(args[0], args[1]);
    } else {
      this.moveFileIntoFile(args[0], args[1]);
    }
    return "works";
  }

  /**
   * Execute command: mv OLDPATH NEWPATH.
   *
   * @param args User input
   */
  public void executeCommand(String[] args) {
    String message = this.run(args);
    if (!message.equals("works")) {
      sendErrMsg(message);
    }

  }
}
