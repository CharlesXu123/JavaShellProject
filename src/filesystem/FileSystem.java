package filesystem;

import java.util.Arrays;

/**
 * Represents the mock file system.
 *
 * @author tanluowe
 * @author xushengs
 * @author guirgisj
 * @author kanghung
 */
public class FileSystem {

  /**
   * FileSystem instance for the constructor.
   */
  private static FileSystem fs;

  /**
   * Array of invalid characters for our mock file system.
   */
  protected static char[] invalidCharacter = {'/', '.', ' ', '!', '@', '#', '$', '$', '^', '&', '*',
      '(', ')', '{', '}', '~', '|', '<', '>', '?', '\t'};

  /**
   * The root of our mock file system tree.
   */
  protected Directory root = new Directory("/", null);

  /**
   * The current directory of the user during the session.
   */
  private Directory currentDirectory = root;

  /**
   * The current path of the user during the session.
   */
  private String currentPath = "";

  /**
   * Factory method to construct FileSystem object only if it has not been created before.
   *
   * @return created FileSystem object
   */
  public static FileSystem createFileSystem() {
    if (fs == null) {
      fs = new FileSystem();
    }
    return fs;
  }

  /**
   * Create and return a file node with given name under parent directory.
   *
   * @param fileName a name of the file to create
   * @param parent the directory node of parent
   * @return File the node of created file
   */
  public File createFileWithParent(String fileName, Directory parent) {
    File newFile = new File(fileName, parent);
    parent.components.add(newFile);
    return newFile;
  }

  /**
   * Create and return a file node with given name under current working directory.
   *
   * @param fileName a name of the file to create
   * @return File the node of created file
   */
  public File createFileUnderCurrentDirectory(String fileName) {
    File newFile = new File(fileName, currentDirectory);
    currentDirectory.components.add(newFile);
    return newFile;
  }

  /**
   * Create a directory with the given name under given parent directory node.
   *
   * @param directoryName a name of the directory to create
   * @param parent the directory node of parent
   */
  public void createDirectoryWithParent(String directoryName, Directory parent) {
    Directory newDirectory = new Directory(directoryName, parent);
    parent.components.add(newDirectory);
  }

  /**
   * Create a directory with the given name under given parent directory node.
   *
   * @param directoryName a name of the directory to create
   * @param parent the directory node of parent
   * @return the directory created
   */
  public Directory createDirectoryWithParentWithReturn(String directoryName, Directory parent) {
    Directory newDirectory = new Directory(directoryName, parent);
    parent.components.add(newDirectory);
    return newDirectory;
  }

  /**
   * Create and return a directory with given name under current working directory.
   *
   * @param directoryName name of the directory to create
   * @return Directory created directory node
   */
  public Directory createDirectoryCurrentDirectory(String directoryName) {
    Directory newDirectory = new Directory(directoryName, currentDirectory);
    currentDirectory.components.add(newDirectory);
    return newDirectory;
  }

  /**
   * Setter for currentDirectory.
   *
   * @param newWorkingDirectory new directory node to set as current working directory
   */
  public void setCurrentDirectory(Directory newWorkingDirectory) {
    currentDirectory = newWorkingDirectory;
  }

  /**
   * Getter for currentDirectory.
   *
   * @return Directory current working directory
   */
  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  /**
   * Update instance variable currentPath to match the user's currentDirectory.
   */
  public void updatePath() {
    Directory traverse = currentDirectory;
    currentPath = "";
    while (!traverse.equals(root)) {
      currentPath = "/" + traverse.getName() + currentPath;
      traverse = traverse.getParentDirectory();
    }
    if (currentPath.equals("")) {
      currentPath = "/";
    }
  }

  /**
   * Getter for currentPath.
   *
   * @return String currentPath
   */
  public String getCurrentPath() {
    return currentPath;
  }

  /**
   * Getter for root directory node.
   *
   * @return Directory root
   */
  public Directory getRoot() {
    return root;
  }

  /**
   * A helper method to check if a path has consecutive forward slashes.
   *
   * @param path string to check
   * @return Boolean true iff path has consecutive forward slashes
   */
  private Boolean hasConsecutiveSlashes(String path) {
    for (int i = 0; i < path.length() - 1; i++) {
      if ((path.charAt(i) == '/') && (path.charAt(i + 1) == '/')) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return a directory or a file node that inPath points to, iff inPath is a valid path. Return
   * null if inPath is an invalid or non-existent path.
   *
   * @param inPath path string to check out, can be absolute or relative
   * @param type type of node to search for, either "file" or "directory"
   * @return UnitFileSystemComponent node with path inPath iff inPath is valid
   */
  public UnitFileSystemComponent checkPath(String inPath, String type) {
    String dirLiteral = "directory";
    // check for multiple slashes
    if (Boolean.TRUE.equals(hasConsecutiveSlashes(inPath))) {
      return null;
    }
    // check for empty string
    if (inPath.equals("")) {
      return null;
    }
    String[] splittedPath = inPath.split("/");
    // invalid path
    if (inPath.equals("/")) {
      if (type.equals(dirLiteral)) {
        return root;
      }
      return null;
    } else if (splittedPath.length == 0) {
      return null;
    }
    if (splittedPath.length > 0 && splittedPath[0].equals("")) {
      // removing "" at splittedPath[0]
      splittedPath = Arrays.copyOfRange(splittedPath, 1, splittedPath.length);
    }
    Directory workingDir;
    if (inPath.startsWith("/")) {
      workingDir = root;
    } else {
      workingDir = currentDirectory;
    }
    Directory finalDir = iterateSplittedPath(splittedPath, workingDir, dirLiteral);
    return checkForMatch(splittedPath[splittedPath.length - 1], finalDir, type, dirLiteral);
  }

  /**
   * A helper method to iterate and update workingDir inside checkPath() method.
   *
   * @param splittedPath String array after splitting userInput by '/'
   * @param workingDir starting directory node of inPath from checkPath()
   * @param dirLiteral String "directory" to use for type checking
   */
  private Directory iterateSplittedPath(String[] splittedPath, Directory workingDir,
      String dirLiteral) {
    Directory startedDir = workingDir;
    // iterate through splittedPath
    for (int i = 0; i < splittedPath.length - 1; i++) {
      // traverse until i == splittedPath.length - 1
      if (splittedPath[i].equals("..")) {
        workingDir = workingDir.parentDirectory;
      } else if (splittedPath[i].equals(".")) {
        // skip check below;
        continue;
      } else {
        for (int k = 0; k < workingDir.components.size(); k++) {
          UnitFileSystemComponent eachCompo = workingDir.components.get(k);
          if (eachCompo.type.equals(dirLiteral) && eachCompo.name.equals(splittedPath[i])) {
            workingDir = (Directory) eachCompo;
          }
        }
        if (startedDir == workingDir) {
          // same memory addr
          return null;
        }
      }
    }
    return workingDir;
  }

  /**
   * A helper method to check for a match. Returns a wanted node, either file or directory, if it
   * exists. Returns null otherwise.
   *
   * @param pathLastItem name of the item to look for
   * @param workingDir directory to look for pathLastItem in
   * @param type wanted type
   * @param dirLiteral String "directory" to use for type checking
   * @return UnitFileSystemComponent wanted file/directory node, or null if not found
   */
  private UnitFileSystemComponent checkForMatch(String pathLastItem, Directory workingDir,
      String type, String dirLiteral) {
    // check for a match
    if (workingDir == null) {
      return null;
    }
    if (type.equals(dirLiteral)) {
      if (pathLastItem.equals(".")) {
        return workingDir;
      } else if (pathLastItem.equals("..")) {
        return workingDir.parentDirectory;
      }
    }
    for (int j = 0; j < workingDir.components.size(); j++) {
      UnitFileSystemComponent eachCompo = workingDir.components.get(j);
      if (eachCompo.type.equals(type) && eachCompo.name.equals(pathLastItem)) {
        return eachCompo;
      }
    }
    return null;
  }

  /**
   * Check if given name contain any illegal characters. Return true iff given name is valid for the
   * mock file system.
   *
   * @param name string to check for validity
   * @return Boolean true iff name is a valid name
   */
  public Boolean isValidNameForFileSystem(String name) {
    for (char eachChar : invalidCharacter) {
      if (name.contains(String.valueOf(eachChar))) {
        // contains illegal character
        return false;
      }
    }
    return true;
  }

  /**
   * A helper method that returns a path in string of a directory node.
   *
   * @param dir Directory node to get path from
   * @return path of directory node in string
   */
  public static String findPath(Directory dir) {
    if (dir.equals(fs.getRoot())) {
      return "/";
    }
    Directory temp = dir;
    String path = "";

    while (!temp.equals(fs.getRoot())) {
      path = "/" + temp.getName() + path;
      temp = temp.getParentDirectory();
    }
    return path;
  }

  /**
   * This method get path of parent directory and file name.
   *
   * @param outFile path with a file at the end
   * @return path of parent directory and file name stored in String[] in order
   */
  public String[] getParent(String outFile) {
    String[] path = outFile.split("/");
    String[] newPath = {"", ""};

    if (outFile.startsWith("/")) {
      // if outFile is absolute path, return parent path as absolute?
      newPath[0] = "/";
    }
    // get the path without the new directory
    for (int i = 0; i < path.length - 1; i++) {
      if (!path[i].equals("")) {
        newPath[0] += path[i] + "/";
      }
    }
    if (newPath[0].equals("")) {
      // path of curr dir is the parent dir of outFile
      newPath[0] = "/";
    }
    newPath[1] = path[path.length - 1];
    return newPath;
  }

}
