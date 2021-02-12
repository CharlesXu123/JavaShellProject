package commands;

/**
 * This class handles command: echo "String".
 *
 * @author xushengsong
 */
public class Echo extends UnitCommand {

  /**
   * Return the documentation of echo command.
   *
   * @return String, documentation of echo command
   */
  @Override
  public String toString() {
    String doc = "< Documentation for Echo (echo) >";
    doc += "\nUsage: echo STRING";
    doc += "\nPrints STRING.";
    doc += "\nSTRING is a string of characters wrapped in double quotation "
        + "marks";
    return doc;
  }

  /**
   * This method help execute command: echo "Str".
   * 
   * @param args
   *          input arguments
   * @return return input string from args if args is valid, an error message
   *         otherwise
   */
  public String executeEcho(String[] args) {
    // !need to check invalid first
    if (args.length != 1) {
      return "\"invalid input";
    } else {
      if (!AppendFile.checkValidStr(args[0])) {
        return "\"invalid input";
      } else {
        return (args[0].substring(1, args[0].length() - 1));
      }
    }
  }

  /**
   * Execute command: echo "String".
   * 
   * @param args
   *          user inputs
   */
  @Override
  public void executeCommand(String[] args) {
    String msg = this.executeEcho(args);
    if (msg.startsWith("\"")) {
      sendErrMsg(msg.substring(1));
    } else {
      sendOutput(msg);
    }
  }
  /*
   * public static void main(String[] args) { String[] a = { "String" };
   * System.out.println(a[0] + a[0].charAt(0)); executeCommand(a); }
   */
}
