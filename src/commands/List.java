package commands;

import filesystem.Directory;
import filesystem.File;
import filesystem.UnitFileSystemComponent;
import java.util.ArrayList;
import java.util.Stack;

/**
 * This class handles command: ls [PATH...]
 * 
 * @author xushengsong
 */
public class List extends UnitCommand {

  /**
   * Return the documentation of ls command.
   * 
   * @return String, documentation of ls command
   */
  @Override
  public String toString() {
    String doc = "< Documentation for List (ls) >";
    doc += "\nUsage: ls [PATH ...]";
    doc += "\nIf no path is given, ";
    doc += "prints the contents of the current working directory.";
    doc += "\nIf given path specifies a file, ";
    doc += "prints the path if specified file exists.";
    doc += "\nIf given path specifies a directory, ";
    doc += "prints the path and contents of the path.";
    return doc;
  }

  /**
   * Print the name of directories and files in dir, print path at the beginning
   * if argsEmpty equals false.
   * 
   * @param path
   *          path
   * @param dir
   *          Directory for checking
   * @param argsEmpty
   *          whether user input is empty or not
   */
  private String listPath(String path, Directory dir, boolean argsEmpty,
      boolean needFContent) {
    // go through the ArrayList attach to the path
    ArrayList<UnitFileSystemComponent> dirLs = dir.getComponents();
    String nameLs = "";
    for (int i = 0; i < dirLs.size(); i++) {
      if (argsEmpty) {
        nameLs += dirLs.get(i).getName() + "\n";
      } else {
        if (!needFContent || dirLs.get(i).getType().equals("directory")) {
          nameLs += dirLs.get(i).getName() + "\n";
        } else {
          nameLs += "\"" + dirLs.get(i).getName();
          String content = ((File) dirLs.get(i)).getContent();
          nameLs += "\"" + content + "\n";
        }
      }
    }
    if (!argsEmpty) {
      return (path + ":\n" + nameLs);
    }
    return nameLs;
  }

  /**
   * This is a helper method for recurList.
   * 
   * @param subDir
   *          a directory for checking
   * @param subPath
   *          path of subDir
   * @param needF
   *          whether need the contents of files under subDir
   * @return String representation of subDir
   */
  private String checkDir(Directory subDir, String subPath, boolean needF) {
    if (subDir.getComponents().size() != 0) {
      return this.listPath(subPath, (Directory) subDir, false, needF) + "\n";
    }
    return "";
  }

  /**
   * This method return a recursive string representation of a directory dir.
   * 
   * @param path
   *          path of dir
   * @param dir
   *          a directory for checking
   * @param needF
   *          whether need the contents of files under subDir
   * @return String representation of dir
   */
  public String recurList(String path, Directory dir, boolean needF) {
    ArrayList<UnitFileSystemComponent> dirLs = dir.getComponents();
    String output = (this.listPath(path, dir, false, needF) + "\n");
    if (path.equals("/")) {
      path = "";
    }
    Stack<Directory> dirStack = new Stack<Directory>();
    Stack<String> pathNameStack = new Stack<String>();
    for (int i = 0; i < dirLs.size(); i++) {
      UnitFileSystemComponent sub = dirLs.get(i);
      if (sub.getType().equals("directory")) {
        dirStack.add((Directory) sub);
        pathNameStack.add(path + "/" + sub.getName());
      }
    }
    while (!dirStack.isEmpty()) {
      Directory subDir = dirStack.pop();
      String subPath = pathNameStack.pop();
      output += this.checkDir(subDir, subPath, needF);
      for (int i = 0; i < subDir.getComponents().size(); i++) {
        UnitFileSystemComponent sub = subDir.getComponents().get(i);
        if (sub.getType().equals("directory")) {
          dirStack.add((Directory) sub);
          pathNameStack.add(subPath + "/" + sub.getName());
        }
      }
    }
    return output;
  }

  /**
   * Output path.
   * 
   * @param path
   *          output path
   */
  private String lsfilePath(String path) {
    // Print out the file
    return (path + "\n");
  }

  /**
   * This method organized args and call the corresponding list method.
   * 
   * @param args
   *          input arguments
   * @param isRecur
   *          whether want recursive or not
   * @return A list of string representation of path in args
   */
  private String checkArgs(String[] args, boolean isRecur) {
    int beginIndex = 0;
    if (isRecur) {
      beginIndex = 1;
    }
    String output = "";
    for (int i = beginIndex; i < args.length; i++) {
      UnitFileSystemComponent pathNode = fs.checkPath(args[i], "directory");
      if (pathNode == null) {
        pathNode = fs.checkPath(args[i], "file");
      }
      if (pathNode == null) {
        if (output.equals("")) {
          return "\"ls: " + args[i] + ": No such file or directory";
        }
        return (output += "ls: " + args[i] + ": No such file or directory");
      } else {
        if (pathNode.getType().equals("directory")) {
          if (!isRecur) {
            output += listPath(args[i], (Directory) pathNode, false, false);
          } else {
            output += recurList(args[i], (Directory) pathNode, false);
          }
        } else {
          output += lsfilePath(args[i]);
        }
      }
    }
    return output;
  }

  /**
   * This method help execute command: ls [PATH...].
   * 
   * @param args
   *          input arguments
   * @return A list of string representation of path in args if args is valid,
   *         an error message otherwise
   */
  public String executeList(String[] args) {
    String msg = "";
    if (args.length == 0) {
      msg = listPath("", fs.getCurrentDirectory(), true, false);
    } else if (args.length == 1 && args[0].equals("-R")) {
      msg = this.recurList("/", fs.getCurrentDirectory(), false);
    } else if (args[0].equals("-R")) {
      msg = this.checkArgs(args, true);
    } else {
      msg = this.checkArgs(args, false);
    }
    return msg;
  }

  /**
   * Execute command: ls [PATH...]
   * 
   * @param args
   *          user inputs
   */
  @Override
  public void executeCommand(String[] args) {
    String msg = this.executeList(args);
    if (msg.startsWith("\"")) {
      sendErrMsg(msg);
    } else {
      if (msg.endsWith("\n")) {
        msg = msg.substring(0, msg.length() - 1);
      }
      sendOutput(msg);
    }
  }
}
