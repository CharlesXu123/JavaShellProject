package util;

import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

// Tasks:
// - for A2a, print the output to console
// - for A2b, enable redirection

/**
 * Handle output with redirection.
 *
 * @author kanghung
 */
public final class OutputHandler {

  /**
   * Display output to Java console.
   *
   * @param output String to display for the user
   */
  public static void sendToStdOut(String output) {
    System.out.println(output);
  }

  /**
   * Send output to a file by overwriting any of its existing contents.
   *
   * @param output   string to output
   * @param filePath string of path that points to a file
   */
  public static void overWriteFile(String output, String filePath,
      FileSystem fs) {
    File outFile = openFile(filePath, fs);
    if (outFile != null) {
      outFile.clearContent();
      outFile.appendContent(output);
    }
  }

  /**
   * Send output to a file by appending its existing content with output.
   *
   * @param output   string to output
   * @param filePath string of path that points to a file
   */
  public static void appendFile(String output, String filePath, FileSystem fs) {
    File outFile = openFile(filePath, fs);
    if (outFile != null) {
      outFile.appendContent(output);
    }
  }

  /**
   * A helper method to open and return a file node if given path is valid. If
   * file does not exist, create one and return it.
   *
   * @param path path of a file to open
   * @return File node if path is valid
   */
  private static File openFile(String path, FileSystem fs) {
    fs.updatePath();
    String fullFilePath;
    if (path.startsWith("/")) {
      // path is abs
      fullFilePath = path;
    } else {
      // path is rel
      fullFilePath = fs.getCurrentPath() + path;
    }

    File targetFile = (File) fs.checkPath(fullFilePath, "file");
    if (targetFile == null) {
      // check for parent if it's null too
      String[] parentPathArr = fs.getParent(fullFilePath);
      if (!parentPathArr[0].startsWith("/")) {
        // convert parent path to absolute
        parentPathArr[0] = toAbsPathStr(parentPathArr[0], fs);
      }
      Directory parentDir =
          (Directory) fs.checkPath(parentPathArr[0], "directory");
      if (parentDir == null) {
        // invalid parent path => the given path is invalid
        ErrorHandler.getErrorMessage(fullFilePath + " is invalid path");
      } else {
        // create file under the parentDir if name is okay
        if (fs.isValidNameForFileSystem(parentPathArr[1])) {
          targetFile = fs.createFileWithParent(parentPathArr[1], parentDir);
        } else {
          ErrorHandler.getErrorMessage("invalid file name");
        }
      }
    }
    return targetFile;
  }

  /**
   * A helper method to convert relative path string into absolute path string.
   * <p>
   * Precondition: relPath points to existing node
   * </p>
   *
   * @param relPath String of relative path (does not start with "/")
   * @return String absolute path of same node
   */
  private static String toAbsPathStr(String relPath, FileSystem fs) {
    String retPath = "";
    Directory tmpDir = (Directory) fs.checkPath(relPath, "directory");
    while (!tmpDir.equals(fs.getRoot())) {
      retPath = "/" + tmpDir.getName() + retPath;
      tmpDir = tmpDir.getParentDirectory();
    }
    return retPath;
  }

}
