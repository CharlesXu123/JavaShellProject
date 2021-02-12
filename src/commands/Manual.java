package commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Implementation of man command for JShell.
 *
 * @author kanghung
 */
public class Manual extends UnitCommand {

  /**
   * Return the documentation of man command.
   *
   * @return String the stored documentation
   */
  @Override
  public String toString() {
    String doc = "< Documentation for Manual (man) >";
    doc += "\nUsage: man CMD [CMD2 ...]";
    doc += "\nPrints documentation for specified commands.";
    return doc;
  }

  /**
   * A helper method to return a documentation of a command in string.
   *
   * @param args Arguments provided by the user
   * @return String of documentation of a command, null if invalid command
   */
  public static String run(String[] args) {
    if (args.length != 1) {
      // invalid number of arguments
      sendErrMsg("Invalid number of argument");
      return null;
    }
    String cmdClassName = util.CommandExecution.returnCommandClassName(args);
    if (cmdClassName == null) {
      // invalid command
      sendErrMsg(args[0] + " is invalid command");
      return null;
    }
    // have valid command to print its doc, guaranteed no
    // "ClassNotFoundException"
    try {
      Class<?> cmdClassCls = Class.forName("commands." + cmdClassName);
      Class<?>[] zeroParameters = {}; // toString takes no parameters
      UnitCommand cmdObj =
          (UnitCommand) cmdClassCls.getDeclaredConstructor().newInstance();
      Method toStringMethod;
      toStringMethod =
          cmdClassCls.getDeclaredMethod("toString", zeroParameters);
      String storedDoc = "\n";
      storedDoc += (String) toStringMethod.invoke(cmdObj, (Object[]) null);
      return storedDoc;
    } catch (InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException
        | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Prints documentation for each command in the array 'args' if valid. If an
   * item inside the array args is not a valid command, prints an error message
   * and stop printing any more documentation.
   *
   * @param args array of command names in strings
   */
  @Override
  public void executeCommand(String[] args) {
    // check if there's no argument from the user
    if (args.length == 0) {
      sendErrMsg("No argument provided");
    } else if (args.length > 1) {
      sendErrMsg("Too many arguments");
    } else {
      // valid use case
      sendOutput(run(args));
    }
  }
}
