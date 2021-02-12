package filesystem;

import java.util.Stack;

/**
 * Stores stack of user-saved directories for pushd and popd commands.
 *
 * @author guirgisj
 */
public class DirectoryStack {
  /**
   * Stack variable to store multiple directory locations to be accessed by
   * PushDirectory and PopDirectory classes.
   */
  public static Stack<Directory> stack = new Stack<Directory>();


  /**
   * Generate string representation of Directory stack.
   * @return a string representation of directory stack
   */
  public static String toStrDirectoryStack() {
    String str = "";
    if (stack == null) {
      return str;
    }
    for (Directory dir : stack) {
      str += FileSystem.findPath(dir) + "\n";
    }
    return str;
  }
}
