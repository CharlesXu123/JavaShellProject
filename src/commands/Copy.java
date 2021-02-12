package commands;

import filesystem.Directory;
import filesystem.File;
import filesystem.UnitFileSystemComponent;
import java.util.Iterator;

/**
 * This class handles command: cp OLDPATH NEWPATH.
 * 
 * @author tanluowe
 *
 */

public class Copy extends UnitCommand {

  /**
   * Return the stored doc of copy command.
   *
   * @return String the stored doc
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for Copy (cp) >");
    doc.append("\nUsage: cp OLDPATH NEWPATH");
    doc.append("\nCopy item OLDPATH to NEWPATH.");
    doc.append(
        " If NEWPATH is a directory, then copy item into the directory.");
    return doc.toString();
  }

  /**
   * Create a file with a path, it the file exist at the path provided,
   * then overwrite it with the
   * content provided.
   * 
   * @param content the content of the file to be created
   * @param path path with a file at the end
   */
  public void creatFile(String content, String path) {
    // check if OUTFILE exist use static method from FileSystem
    String[] ppath = fs.getParent(path);

    File outFile = (File) fs.checkPath(path, "file");
    // if OUTFILE DNE, create OUTFILE
    if (outFile == null) {
      if (!fs.isValidNameForFileSystem(ppath[1])) {
        sendErrMsg("invalid file name");
        return;
      }
      if (ppath[0].equals("")) {
        outFile = fs.createFileUnderCurrentDirectory(path);
      } else {
        Directory dir = (Directory) fs.checkPath(ppath[0], "directory");
        if (dir == null) {
          sendErrMsg("invalid path");
          return;
        }
        outFile = fs.createFileWithParent(ppath[1], dir);
      }
    } else {
      outFile.clearContent();
    }
    outFile.appendContent(content);
  }

  /**
   * Copy the content of the file at oldPath to that at newPath.
   * 
   * @param oldPath path with a file at the end
   * @param newPath path with a file at the end
   */
  public void copyFile(String oldPath, String newPath) {
    File of = (File) fs.checkPath(oldPath, "file");
    this.creatFile(of.getContent(), newPath);

  }

  /**
   * Copy a file at oldPath into the directory at newPath.
   * 
   * @param oldPath path with a file at the end
   * @param newPath path with a directory at the end
   */
  public void copyFileIntoDir(String oldPath, String newPath) {
    File of = (File) fs.checkPath(oldPath, "file");
    Directory dir = (Directory) fs.checkPath(newPath, "directory");
    if (dir.containComponent(of.getName())) {
      sendErrMsg(of.getName() + " has already exist");
      return;
    }
    File outfile = fs.createFileWithParent(of.getName(), dir);
    outfile.appendContent(of.getContent());
  }


  /**
   * Check if oldDir is a parent directory of newDir.
   * 
   * @param oldDir a directory
   * @param newDir a directory
   * @return true or false
   */
  public boolean isParentDir(Directory oldDir, Directory newDir) {
    if (newDir.getParentDirectory() != fs.getRoot()) {
      newDir = newDir.getParentDirectory();

      if (newDir.equals(oldDir)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Recursively copy a Directory into other Directory.
   * 
   * @param oldDir directory to be moved
   * @param parentDir new parent of the oldDIr
   */
  public void recursiveCopyDir(Directory oldDir, Directory parentDir) {
    Directory tempParent =
        fs.createDirectoryWithParentWithReturn(oldDir.getName(), parentDir);
    Iterator<UnitFileSystemComponent> it = oldDir.getComponents().iterator();

    while (it.hasNext()) {
      UnitFileSystemComponent temp = it.next();
      if (temp.getType().equals("directory")) {
        recursiveCopyDir((Directory) temp, tempParent);
      } else if (temp.getType().equals("file")) {
        fs.createFileWithParent(temp.getName(), tempParent);
      }
    }
    return;
  }

  /**
   * Recursively copy a Directory's children into other Directory.
   * 
   * @param oldDir directory to be moved
   * @param parentDir new parent of the oldDIr
   */
  private void recursiveCopyDirWithoutParent(Directory oldDir,
      Directory parentDir) {
    Iterator<UnitFileSystemComponent> it = oldDir.getComponents().iterator();

    while (it.hasNext()) {
      UnitFileSystemComponent temp = it.next();
      if (temp.getType().equals("directory")) {
        recursiveCopyDir((Directory) temp, parentDir);
      } else if (temp.getType().equals("file")) {
        fs.createFileWithParent(temp.getName(), parentDir);
      }
    }
    return;
  }

  /**
   * A helper method to return a content of file in String. 
   * It returns works if the file or directory.
   * is copied successfully. It returns a errorMessage if error is cached
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
      if (this.isParentDir(oldDir, newDir) || oldDir.equals(fs.getRoot())) {
        return ("Can not copy a parent directory into its children directory");
      }
      this.recursiveCopyDir(oldDir, newDir);
      // Copy file.
    } else if (oldDir != null) {
      if (!(args[1].endsWith("/"))) {
        return ("Can not copy a directory to a file");
      } else {
        String[] ppath = fs.getParent(args[1]);
        Directory copyDir = fs.createDirectoryWithParentWithReturn(ppath[1],
            oldDir.getParentDirectory());
        this.recursiveCopyDirWithoutParent(oldDir, copyDir);
      }
    } else if (newDir != null) {
      copyFileIntoDir(args[0], args[1]);
    } else {
      copyFile(args[0], args[1]);
    }
    return "works";
  }

  /**
   * Execute command: cp OLDPATH NEWPATH.
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
