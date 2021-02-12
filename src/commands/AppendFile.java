package commands;

import filesystem.Directory;
import filesystem.File;

/**
 * This class handles command: echo "String" >> OUTFILE.
 *
 * @author xushengsong
 */
public class AppendFile extends UnitCommand {

  /**
   * Check if arg is a valid string.
   *
   * @param arg
   *          string for checking
   * @return true if arg is a valid string, return false otherwise
   */
  public static boolean checkValidStr(String arg) {
    if (arg.charAt(0) != '"' || arg.charAt(arg.length() - 1) != '"') {
      return false;
    }
    for (int i = 1; i < arg.length() - 1; i++) {
      if (arg.charAt(i) == '"') {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if args is a valid input for echoType echo.
   *
   * @param args
   *          arguments
   * @param echoType
   *          ">" or ">>"
   * @return true if args is a valid input for echoType echo, return false
   *         otherwise
   */
  private boolean checkInput(String[] args, String echoType) {
    if (args.length != 3) {
      return false;
    } else {
      if (!args[1].equals(echoType) || !checkValidStr(args[0])) {
        return false;
      }
      return true;
    }
  }

  /**
   * This method help execute command: echo "String" >> OUTFILE or echo "String"
   * > OUTFILE.
   *
   * @param str
   *          input String
   * @param outPath
   *          path with a file at the end
   * @param echoType
   *          ">>" or ">"
   */
  private String echoFile(String str, String outPath, String echoType) {
    // check if OUTFILE exist use static method from FileSystem
    String[] parentPath = fs.getParent(outPath);
    Directory dir = (Directory) fs.checkPath(outPath, "directory");
    if (dir != null) {
      return ("duplicated directory name exists");
    }
    File outFile = (File) fs.checkPath(outPath, "file");
    if (outFile == null) {
      if (!fs.isValidNameForFileSystem(parentPath[1])) {
        return ("invalid file name");
      }
      if (parentPath[0].equals("")) {
        outFile = fs.createFileUnderCurrentDirectory(outPath);
      } else {
        Directory tmpDir = (Directory) fs.checkPath(parentPath[0], "directory");
        if (tmpDir == null) {
          return ("invalid path");
        }
        outFile = fs.createFileWithParent(parentPath[1], tmpDir);
      }
    }
    if (echoType.equals(">")) {
      outFile.clearContent();
    }
    outFile.appendContent(str.substring(1, (str.length()) - 1));
    return "";
  }

  /**
   * This method help execute command echo "str" > FILE or echo "Str" >> FIlE.
   * @param args
   *          input arguments
   * @param echoType
   *          ">>" for append or ">" for overwrite
   * @return return a error message if an error occur, an empty string otherwise
   */
  public String executeEcho(String[] args, String echoType) {
    if (!checkInput(args, echoType)) {
      return "invalid input";
    } else {
      return this.echoFile(args[0], args[2], echoType);
    }
  }

  /**
   * Execute command: echo "String" >> OUTFILE.
   *
   * @param args
   *          user inputs
   */
  @Override
  public void executeCommand(String[] args) {
    String msg = this.executeEcho(args, ">>");
    if (!msg.equals("")) {
      sendErrMsg(msg);
    }

  }

  /*
   * test public static void main (String[] args) { String[] input = new
   * String[3]; input[0] = "\"string\""; boolean a = !checkValidStr(input[0]);
   * //System.out.println(input[0] + " /" + a); input[1] = ">"; input[2] =
   * "OUTFILE"; executeCommand(input); }
   */
}
